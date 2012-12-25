(ns frog.service
  [:use frog.db])

;;;;; Services

(declare make-commit spool)

(def *services* (atom {}))

(defn register-service [service fn]
  (swap! *services* #(assoc % service fn)))

(defn service-data [service]
  (@*services* service))

(defmacro defservice [name & body]
  `(register-service ~name
                     (fn [~'ticket] 
                       ~@body)))

(defn boot-service [ticket]
  ((@*services* (ticket :service)) ticket))

(defn boot-services [tickets] (map boot-service tickets))


;;;;;;;;;; Transactions

(defn getUUID []
  (.toString (java.util.UUID/randomUUID)))


(defn serialize [x]
  (binding [*print-dup* true]
    (pr-str x)))

(defn to-db-entry [x]
  {:data (serialize (dissoc x :uuid))
   :uuid (x :uuid)})

(defn forward [caller tickets]
  (db-commit-record (caller :uuid) (map to-db-entry tickets))
  (boot-services tickets))

(defn make-commit [service next args]
  (conj {:service service :next next :uuid (getUUID)} args))

(defn enter-tickets [tickets]
  (db-commit-record-initial (map to-db-entry tickets))
  (boot-services tickets))

; The stage macro allows us to define execution stages
; 
; It will bind two anaphoric variables, "go" and "caller"
; Go will add a note to the transaction saying that a certain
; STAGE is to be executed with a certain set of ARGS,
; and then another stage NEXT is to be executed after, with
; CALLER bound to the final state of STAGE

(defmacro stage [& body]
  `(fn [~'caller]
     (let [trxn# (atom [])
           ~'go  #(go* trxn# %1 %2 %3) ]
       ~@body
       (forward ~'caller @trxn#))))

(defn go* [trxn service next args]
  (swap! trxn #(conj % (make-commit service next args))))

(def *stages*
  {:ftp-done (stage 
              (println "[STAGE] ftp-done"))})

(defn call-stage [ticket]
  ((*stages* (ticket :next)) ticket))

;;;;;;;;;;;;;;;;

(def *test-transaction*
  [(make-commit :ftp :ftp-done {:host "ftp.whatever.dom"
                                :user "memememe"
                                :pass "password"})])

(defn test-ftp []
  (enter-tickets *test-transaction*))


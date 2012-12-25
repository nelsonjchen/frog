
(ns frog.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/database.db"})

(defn db-create []
  (try
    (jdbc/with-connection db
      (jdbc/create-table :tickets
                    [:data :text]
                    [:uuid :text]))
    (catch Exception e (println e))))

(comment
(defn db-commit-record [caller-to-delete records]
  (println caller-to-delete)
  (if (= caller-to-delete nil)
    (throw (IllegalArgumentException. "Caller UUID not supplied")))
  (jdbc/with-connection db (jdbc/transaction
   (jdbc/delete-rows :tickets ["uuid=?" caller-to-delete])
   (apply jdbc/insert-records :tickets records))))

(defn db-commit-record-initial [records]
  (jdbc/with-connection db (jdbc/transaction
   (apply jdbc/insert-records :tickets records)))))


(defn db-commit-record [caller-to-delete records]
  (println "[TRANSACTION:DB] Removing old ticket " caller-to-delete)
  (println "[TRANSACTION:DB] Adding tickets " records))

(defn db-commit-record-initial [records]
  (println "[TRANSACTION:DB] Adding tickets " records))

(def list-db-ents
  (jdbc/with-connection db
    (jdbc/with-query-results rows ["SELECT * FROM tickets"]
      rows)))

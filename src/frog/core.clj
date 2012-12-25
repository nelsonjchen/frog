(ns frog.core
  (:use compojure.core
        [ring.adapter.jetty :only [run-jetty]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response])
  )

(def doop "hellllo")

(defroutes main-routes
  (GET "/" [] doop)
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app main-routes)


(defn start-server
  []
  (run-jetty app {:port 8085 :join? false}))
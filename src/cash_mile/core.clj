(ns cash-mile.core
  (:require [cash-mile.api :refer [api]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn -main []
  (println "Starting cashMile API server on port 3000")
  (run-jetty api {:port 3000 :join? true}))

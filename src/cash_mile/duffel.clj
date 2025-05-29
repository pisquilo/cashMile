(ns cash-mile.duffel
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def duffel-api-url "https://api.duffel.com/")

(def headers
  {:Authorization (str "Bearer " (System/getenv "DUFFEL_API_KEY"))
   :Content-Type "application/json"
   :Duffel-Version "v2"})

(defn time-interval [from to]
  {:from from
   :to to})

(defn get-passengers [passenger-data]
  (map (fn [[family-name given-name age]]
         {:age age
          :family_name family-name
          :given_name given-name})
       passenger-data))

(defn get-slices [slices-data]
  (map (fn [[origin destination date arrival_time departure_time]]
         (cond-> {:origin origin
                  :destination destination
                  :departure_date date}
           arrival_time (assoc :arrival_time arrival_time)
           departure_time (assoc :departure_time departure_time)))
       slices-data))

(defn call-duffel-api [endpoint payload]
  (println "Endpoint:" endpoint)
  (println "Payload:" payload)
  (let [api-url (str duffel-api-url endpoint)
        response (client/post api-url
                              {:headers headers
                               :body payload
                               :throw-exceptions false})]
    (when (not= (:status response) 201)
      (println "Response body:" (:body response)))
    response))

(defn get-flight-offers [request]
  (let [endpoint "air/offer_requests"
        payload (cheshire/generate-string
                 {:data
                  {:slices (get-slices (:trips request))
                   :passengers (get-passengers (:passengers request))
                   :max_connections (:max-connections request)
                   :cabin_class (:cabin-class request)}})
        duffel-response (call-duffel-api endpoint payload)]
    duffel-response))
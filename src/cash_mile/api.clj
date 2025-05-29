(ns cash-mile.api
  (:require [cheshire.core :as cheshire]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [cash-mile.duffel :as duffel]))

(defrecord Request [trips passengers max-connections cabin-class])

(defn parse-trips [trips]
  (let [{:keys [origin destination departure_date]} trips
        origin-list (if (string? origin) [origin] origin)
        destination-list (if (string? destination) [destination] destination)
        res (for [origin origin-list
                  destination destination-list]
              [origin destination departure_date nil nil])]
    res))

(defn parse-passengers [passengers]
  (let [{:keys [family_name given_name age]} passengers
        family_names (if (string? family_name) [family_name] family_name)
        given_names (if (string? given_name) [given_name] given_name)
        ages (if (string? age) [age] age)
        res (map vector family_names given_names ages)]
    res))


(defroutes app-routes
  (GET "/cash-flight-offers" {params :params}
    (println "Query Params:" params)
    (let [{:keys [trips passengers cabin_class max_connections]} params
          trips-list (parse-trips trips)
          passengers-list (parse-passengers passengers)
          request (->Request trips-list passengers-list max_connections cabin_class)
          cash-flights (duffel/get-flight-offers request)]
      (response {:status "success",
                 :request {:status (:status cash-flights),
                           :body (cheshire/parse-string (:body cash-flights) true)}})))

  (route/not-found {:status 404
                    :body {:error "Not Found"}}))

(def api
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response
      wrap-keyword-params
      wrap-nested-params
      wrap-params))

(ns cashMile.core
  (:require [cashMile.duffel :as duffel]))

(defn -main []
  (let [slices (duffel/get-slices [["LHR" "JFK" "2025-12-15" nil nil]
                                   ["MUC" "GIG" "2025-12-15" (duffel/time-interval "17:00" "18:00") nil]])
        passengers (duffel/get-passengers [["Ferreira" "Pedro" 27]
                                           ["Capelli" "Iza" 28]])
        response (duffel/get-flight-offers slices passengers 0 "economy")]
    (:status response)))

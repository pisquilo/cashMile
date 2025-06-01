(ns cash-mile.api-test
  (:require [clojure.test :refer [deftest are]]
            [cash-mile.api :refer [parse-passengers parse-trips]]))

(deftest test-parse-passengers
  (are [input expected]
       (= expected (parse-passengers input))

    {:family_name "Smith"
     :given_name "John"
     :age "30"}
    [["Smith" "John" "30"]]

    {:family_name ["Smith" "Doe"]
     :given_name ["John" "Jane"]
     :age ["30" "25"]}
    [["Smith" "John" "30"]
     ["Doe" "Jane" "25"]]))

(deftest test-parse-trips
  (are [input expected]
       (= expected (parse-trips input))

    {:origin "JFK"
     :destination "LAX"
     :departure_date "2023-10-01"}
    [["JFK" "LAX" "2023-10-01" nil nil]]

    {:origin ["JFK" "NYC"]
     :destination "LAX"
     :departure_date "2023-10-01"}
    [["JFK" "LAX" "2023-10-01" nil nil]
     ["NYC" "LAX" "2023-10-01" nil nil]]

    {:origin "JFK"
     :destination ["LAX" "SFO"]
     :departure_date "2023-10-01"}
    [["JFK" "LAX" "2023-10-01" nil nil]
     ["JFK" "SFO" "2023-10-01" nil nil]]

    {:origin ["JFK" "NYC"]
     :destination ["LAX" "SFO"]
     :departure_date "2023-10-01"}
    [["JFK" "LAX" "2023-10-01" nil nil]
     ["JFK" "SFO" "2023-10-01" nil nil]
     ["NYC" "LAX" "2023-10-01" nil nil]
     ["NYC" "SFO" "2023-10-01" nil nil]]))
(ns cash-mile.duffel-test
  (:require [clojure.test :refer [deftest are is]]
            [cash-mile.duffel :refer [time-interval get-passengers get-slices]]))

(deftest test-time-interval
  (is (= {:from "17:00" :to "18:00"}
         (time-interval "17:00" "18:00"))))

(deftest test-get-passengers
  (are [input expected]
       (= expected (vec (get-passengers input)))

    [["Smith" "John" "30"]]
    [{:age "30" :family_name "Smith" :given_name "John"}]

    [["Smith" "John" "30"]
     ["Doe" "Jane" "25"]]
    [{:age "30" :family_name "Smith" :given_name "John"}
     {:age "25" :family_name "Doe" :given_name "Jane"}]))

(deftest test-get-slices
  (are [input expected]
       (= expected (vec (get-slices input)))

    [["JFK" "LAX" "2023-10-01" nil nil]]
    [{:origin "JFK" :destination "LAX" :departure_date "2023-10-01"}]

    [["JFK" "LAX" "2023-10-01" nil nil] ["NYC" "MUC" "2023-10-03" nil nil]]
    [{:origin "JFK" :destination "LAX" :departure_date "2023-10-01"}
     {:origin "NYC" :destination "MUC" :departure_date "2023-10-03"}]

    [["JFK" "LAX" "2023-10-01" "10:00" "08:00"]]
    [{:origin "JFK" :destination "LAX" :departure_date "2023-10-01"
      :arrival_time "10:00" :departure_time "08:00"}]))
(ns hospital.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [hospital.person :as person]))

(def tom {:id 0 :name "tom" :age 23 :height 1.83 :weight 80.0 :ldl 10.0 :hdl 10.0})

(deftest bmi
  (testing "BMI adds incorrectly."
    (let [updated-tom (person/add-bmi tom)]
      (is (= 23.888440980620498 (:bmi updated-tom))))))

(deftest cholesterol
  (testing "Cholesterol adds incorrectly."
    (let [updated-tom (person/add-total-cholesterol tom)]
      (is (= 20.0 (:total-cholesterol updated-tom))))))

(deftest risk-score
  (testing "Risk score is incorrect."
    (let [updated-tom (-> tom person/add-bmi person/add-total-cholesterol person/add-risk-score)]
      (is (= 43.8884409806205 (:risk-score updated-tom))))))

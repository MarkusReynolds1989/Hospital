(ns hospital.core
  (:gen-class)
  (:require [hospital.person :as person]
            [hospital.clean :as clean]
            [clojure.string :as string]
            [cheshire.core :as json]
            [json-schema.core :as jsc]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(def health-data-path "C:/Users/marku/home/code/clojure/hospital/test_data/person_health_data.csv")
(def config-schema-path "C:/Users/marku/home/code/clojure/hospital/config/config.schema.json")
(def config-path "C:/Users/marku/home/code/clojure/hospital/config/config.json")

(defn read-json
  [file]
  (with-open [reader (io/reader file)]
    (json/parse-stream reader true)))

(defn validate-json [data schema]
  (let [results (jsc/validate schema data)]
    (if (empty? (:errors results))
      :valid
      (:errors results))))

(def config (read-json config-path))

(def config-schema (read-json config-schema-path))

(def validation-results (validate-json config config-schema))

(def risk-threshold (:risk-threshold config))

(def health-data
  (string/split (slurp health-data-path) #"\r\n"))

(defn -main
  [& _]
  (if
   (= validation-results :valid)
    (->> health-data
         (clean/health-data-lines)
         (clean/generate-people)
         (map (fn [person] (person/add-bmi person)))
         (map (fn [person] (person/add-total-cholesterol person)))
         (map (fn [person] (person/add-risk-score person)))
         (filter (fn [person] (> (:risk-score person) risk-threshold)))
         (run! (fn [person] (pprint person))))
    (pprint validation-results)))



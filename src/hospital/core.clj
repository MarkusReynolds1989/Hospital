(ns hospital.core
  (:gen-class)
  (:require [hospital.person :as person]
            [hospital.clean :as clean]
            [clojure.string :as string]
            [cheshire.core :as json]
            [json-schema.core :as jsc]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
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

;(def risk-threshold (:risk-threshold config))

(def health-data
  (string/split (slurp health-data-path) #"\r\n"))

(def db {:classname "org.sqlite.JDBC" :subprotocol "sqlite" :subname "hospital.db"})

(defn create-table
  []
  (jdbc/db-do-commands db (jdbc/create-table-ddl
                           :person [[:name "Text"]
                                    [:age "Integer"]
                                    [:height "Real"]
                                    [:weight "Real"]
                                    [:ldl "Real"]
                                    [:hdl "Real"]
                                    [:bmi "Real"]
                                    [:total_cholesterol "Real"]
                                    [:risk_score "Real"]])))

(defn insert-person [person]
  (jdbc/insert! db
                :person
                {:name (:name person)
                 :age (:age person)
                 :height (:height person)
                 :weight (:weight person)
                 :ldl (:ldl person)
                 :hdl (:hdl person)
                 :bmi (:bmi person)
                 :total_cholesterol (:total-cholesterol person)
                 :risk_score (:risk-score person)}))

(defn query-person []
  (jdbc/query db ["Select rowid,* from person"]))

(defn -main
  [& _]
  (if
   (= validation-results :valid)
    (let [people (->> health-data
                      (clean/health-data-lines)
                      (clean/generate-people)
                      (map (fn [person] (person/add-bmi person)))
                      (map (fn [person] (person/add-total-cholesterol person)))
                      (map (fn [person] (person/add-risk-score person))))]
      (create-table)
      (run! (fn [person] (insert-person person)) people)
      (pprint (query-person)))
    (pprint validation-results)))



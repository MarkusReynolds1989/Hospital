(ns hospital.clean
  (:require [hospital.person :as person]
            [clojure.string :as string]))

(defn health-data-lines
  [data]
  (->> data
       (drop 1)
       (map (fn [line] (string/split line #",")))))

(defn generate-person
  [[id name age height weight ldl hdl]]
  (try {:id (Integer/parseInt id)
        :name name
        :age (Integer/parseInt age)
        :height (Double/parseDouble height)
        :weight (Double/parseDouble weight)
        :ldl (Double/parseDouble ldl)
        :hdl (Double/parseDouble hdl)}
       (catch Exception e
         (println "Excpetion: " (.getMessage e)))))

(defn generate-people
  [lines]
  (map generate-person lines))

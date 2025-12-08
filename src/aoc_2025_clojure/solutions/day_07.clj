(ns aoc-2025-clojure.solutions.day-07
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [includes?]]))

;; input parsing

(defn set-of-indices [string char]
  (set (keep-indexed #(when (= %2 char) %1) string)))

(defn parse-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (let [{:keys [beams splitters]}
          (reduce
            (fn [acc line]
              (cond
                (includes? line "S")
                  (assoc acc :beams (set-of-indices line \S))
                (includes? line "^")
                  (update acc :splitters conj (set-of-indices line \^))
                :else acc))
            {:beams #{} :splitters []}
            (line-seq rdr))]
      [beams splitters])))

;; problem 1

(defn next-beams-and-splits [beams splitters]
  (reduce
    (fn [[next-beams splits] beam]
      (if (get splitters beam)
        [(conj next-beams (inc beam) (dec beam)) (inc splits)]
        [(conj next-beams beam) splits]))
    [#{} 0]
    beams))

(defn solve1 [filename]
  (let [[beams splitters] (parse-input filename)]
    (second (reduce
      (fn [[current-beams sum-splits] current-splitters]
        (let [[next-beams next-splits] (next-beams-and-splits current-beams current-splitters)]
          [next-beams (+ sum-splits next-splits)]))
      [beams 0]
      splitters))))

;; problem 2

(defn add-to-map [[key value] map]
  (if (contains? map key)
    (update map key #(+ %1 value))
    (assoc map key value)))

(defn next-beams-with-multiplicity [beams splitters]
  (reduce
    (fn [next-beams [beam multiplicity]]
      (if (get splitters beam)
        (add-to-map [(dec beam) multiplicity] (add-to-map [(inc beam) multiplicity] next-beams))
        (add-to-map [beam multiplicity] next-beams)))
    {}
    beams))

(defn solve2 [filename]
  (let [[beams splitters] (parse-input filename)
        beams-with-multiplicity (zipmap beams '(1))
        beams-end (reduce (fn [current-beams current-splitters] (next-beams-with-multiplicity current-beams current-splitters)) beams-with-multiplicity splitters)]
    (reduce + (map (fn [[beam multiplicity]] multiplicity) beams-end))))

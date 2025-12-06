(ns aoc-2025-clojure.solutions.day-05
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split blank? includes?]]))

;; input parsing

(defn parse-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (let [{:keys [ranges numbers]}
          (reduce
            (fn [acc line]
              (cond
                (blank? line) acc
                (includes? line "-")
                (let [[a b] (split line #"-")]
                  (update acc :ranges conj [(Long/parseLong a) (Long/parseLong b)]))
                :else
                (update acc :numbers conj (Long/parseLong line))))
            {:ranges '() :numbers '()}
            (line-seq rdr))]
      [ranges numbers])))

;; problem 1

(defn range-contains? [number range]
  (let [[a b] range]
    (and (>= number a) (<= number b))))

(defn solve1 [filename]
  (let [[ranges numbers] (parse-input filename)]
    (reduce (fn [acc number]
      (if (some (partial range-contains? number) ranges)
        (inc acc)
        acc))
      0
      numbers)))

;; problem 2

(defn ranges-intersect? [range1 range2]
  (let [[a1 b1] range1
        [a2 b2] range2] ;; we assume a1 <= a2
    (>= b1 a2)))

(defn merge-ranges [range1 range2]
  (let [[a1 b1] range1
        [a2 b2] range2] ;; we assume a1 <= a2
    [a1 (max b1 b2)]))

(defn solve2 [filename]
  (let [[ranges] (parse-input filename)]
    (->>
      (sort-by first ranges)
      (reduce
        (fn [merged-ranges range]
          (if (empty? merged-ranges)
            [range]
            (let [last-range (last merged-ranges)]
              (if (ranges-intersect? last-range range)
                (conj (pop merged-ranges) (merge-ranges last-range range))
                (conj merged-ranges range)))))
        [])
      (map (fn [[a b]] (inc (- b a))))
      (reduce +))))

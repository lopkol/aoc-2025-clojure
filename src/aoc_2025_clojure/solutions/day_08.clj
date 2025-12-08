(ns aoc-2025-clojure.solutions.day-08
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split includes?]])
  (:require [clojure.set :refer [union]]))

;; input parsing

(defn parse-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (reduce
      (fn [acc line]
        (let [[a b c] (map parse-long (split line #","))]
          (conj acc [a b c])))
      []
     (line-seq rdr))))

;; problem 1

(defn dist-sq [point1 point2]
  (let [[x1 y1 z1] point1
        [x2 y2 z2] point2]
    (+ (* (- x1 x2) (- x1 x2)) (* (- y1 y2) (- y1 y2)) (* (- z1 z2) (- z1 z2)))))

(defn find-containing-component [idx connected-components]
  (let [component (some #(when (contains? % idx) %) connected-components)]
    (if component
      component
      #{idx})))

(defn connect [idx1 idx2 connected-components]
  (let [component1 (find-containing-component idx1 connected-components)
        component2 (find-containing-component idx2 connected-components)]
    (conj (disj connected-components component1 component2) (union component1 component2))))

(defn solve1 [filename]
  (let [n (if (includes? filename "test") 10 1000)
        input (parse-input filename)
        indices (range (count input))
        pairs (for [i indices
                    j indices
                    :when (< i j)]
                [i j])
        distances (map (fn [[i j]]
                         [[i j] (dist-sq (nth input i) (nth input j))])
                       pairs)
        sorted-distances (sort-by second distances)
        first-n-pairs (take n sorted-distances)
        connected-components (reduce (fn [connected-components [[i j] _]]
                                         (connect i j connected-components))
                                     #{}
                                     first-n-pairs)
        component-sizes (map count connected-components)
        three-largest (take 3 (sort > component-sizes))]
    (reduce * three-largest)))

;; problem 2

(defn solve2 [filename]
  (let [input (parse-input filename)
        indices (range (count input))
        total-count (count input)
        pairs (for [i indices
                    j indices
                    :when (< i j)]
                [i j])
        distances (map (fn [[i j]]
                         [[i j] (dist-sq (nth input i) (nth input j))])
                       pairs)
        sorted-distances (sort-by second distances)
        [k l] (loop [connected-components #{}
                     remaining-pairs sorted-distances]
                (if (empty? remaining-pairs)
                  nil
                  (let [[pair dist] (first remaining-pairs)
                        [i j] pair
                        new-components (connect i j connected-components)
                        is-complete (and (= (count new-components) 1)
                                         (= (count (first new-components)) total-count))]
                    (if is-complete
                      pair
                      (recur new-components (rest remaining-pairs))))))]
      (* (first (nth input k)) (first (nth input l)))))

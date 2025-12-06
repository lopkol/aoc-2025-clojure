(ns aoc-2025-clojure.solutions.day-06
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split blank?]]))

;; problem 1

(defn parse-input1 [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (let [{:keys [numbers operators]}
          (reduce
            (fn [acc line]
              (if (re-find #"[*+]" line)
                (let [operators (split line #"\s+")]
                  (assoc acc :operators operators))
                (let [new-numbers (->> (split line #"\s+")
                                       (filter (complement empty?))
                                       (map parse-long))]
                  (update acc :numbers conj new-numbers))))
            {:numbers [] :operators []}
            (line-seq rdr))]
      [numbers operators])))

(defn string-to-func [string]
  (deref (resolve (symbol string))))

(defn transpose-mtx [mtx]
  (apply map vector mtx))

(defn apply-operators-to-numbers [numbers operators]
  (map
    (fn [f args] (apply f args))
    (map string-to-func operators)
    numbers))

(defn solve1 [filename]
  (let [[numbers operators] (parse-input1 filename)]
    (reduce + (apply-operators-to-numbers (transpose-mtx numbers) operators))))

;; problem 2

(defn transpose-str [string]
  (->> (split string #"\n")
       (map seq)
       (transpose-mtx)
       (map #(apply str %))))

(defn remove-junk [string]
  (clojure.string/replace string #"[\s+*]" ""))

(defn parse-input2 [filename]
  (let [input (slurp (str "./inputs/" filename ".txt"))
        transposed-input (transpose-str input)
        {:keys [numbers operators]}
          (reduce
            (fn [acc line]
              (cond
                (blank? line)
                  (update acc :numbers conj [])
                (re-find #"[*+]" line)
                  (update
                    (update acc :operators conj (str (last line)))
                    :numbers
                    (fn [numbers] (update numbers (dec (count numbers)) #(conj % (parse-long (remove-junk line))))))
                :else
                  (update
                    acc
                    :numbers
                    (fn [numbers] (update numbers (dec (count numbers)) #(conj % (parse-long (remove-junk line))))))))
          {:numbers [[]] :operators []}
          transposed-input)]
    [numbers operators]))

(defn solve2 [filename]
  (let [[numbers operators] (parse-input2 filename)]
    (reduce + (apply-operators-to-numbers numbers operators))))

(ns clojetris.core
  (:gen-class))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))


;; Game brick bitmaps
;; The bricks L, S is declared bellow
(def l_brick [[0 0 0 0]
              [0 1 0 0]
              [0 1 0 0]
              [0 1 1 0]])

(def s_brick [[0 0 0 0]
              [0 0 1 1]
              [1 1 0 0]
              [0 0 0 0]])

;; Rotates the game bricks.
(defn brick_rotation [n_times, colls]
  "takes 2 arguments, number of rotations and a symetric 2d list"
  (if (= n_times 0)
    colls
    (recur (dec n_times) (reverse (partition-all 4 (apply interleave colls))))))

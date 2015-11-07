(ns clojetris.core
  (:gen-class))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))


;; Game brick bitmaps
;; The bricks L, S is declared below
(def l_brick [[0 0 0 0]
              [0 1 0 0]
              [0 1 0 0]
              [0 1 1 0]])

(def s_brick [[0 0 0 0]
              [0 0 1 1]
              [1 1 0 0]
              [0 0 0 0]])

;; Replace with a function
(defn create_gameboard [rows cols]
  (loop [gameboard [] r_rows rows]
    (if (zero? r_rows)
      gameboard
      (recur (conj gameboard (vec (take cols (repeat 0))))
             (dec r_rows)))))

;; Gameboard bitmap
;; Should add a padding of 1 extra
(def gameboard [[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]])

;; Kom ihåg hur vi kan applicera händelser från en lista
;; (reduce #(%2 %1) s_brick [rotate rotate rotate rotate])

;; (def rotate (partial brick_rotation 1))
;; (-> s_brick rotate rotate rotate)
;; Rotates the game bricks.
(defn brick_rotation [n_times, colls]
  "takes 2 arguments, number of rotations and a symetric 2d list"
  (if (= n_times 0)
    colls
    (recur (dec n_times) (reverse (partition-all 4 (apply interleave colls))))))

(defn trim_into [board_selection brick_selection pos]
  "Trim into returns a new brick data that will fit into board at pos"
  (if (< (count board_selection) (+ pos (count brick_selection)))
    (subvec brick_selection
            0
            (max 0 (- (count brick_selection)
                      (- (+ pos (count brick_selection))
                         (count board_selection)))))
    (subvec brick_selection
            (max 0 (min (count brick_selection)
                        (- (count brick_selection)
                           (+ (count brick_selection) pos))))
            (count brick_selection))))

(defn mask_in [board_row brick_row pos_col]
  "Merges a brick row into board row at a given column"
  (let [beginning (subvec board_row
                          0
                          (max 0 pos_col))
        selected (subvec board_row
                         (max 0 pos_col)
                         (+ (count (trim_into board_row brick_row pos_col)) (max 0 pos_col)))
        end (subvec board_row
                    (+ (max 0 pos_col) (count (trim_into board_row brick_row pos_col)))
                    (count board_row))]
    (vec (concat beginning (map bit-or selected (trim_into board_row brick_row pos_col)) end))))

(defn brick_position [gameboard brick pos_row pos_col]
  "Merges brick into gameboard at position row & col"
  (let [game_row (min (count gameboard) (max 0 pos_row))
        trimmed_brick (trim_into gameboard brick (min (count gameboard) pos_row))]
    (loop [processed_rows (subvec gameboard 0 game_row)
           selected_row (nth gameboard game_row)
           rest_rows (subvec gameboard (+ game_row 1) (count gameboard))
           row_idx 0]
      (if (= row_idx (count trimmed_brick))
        (if (nil? selected_row)
          (into processed_rows rest_rows)
          (into (conj processed_rows selected_row) rest_rows))
        (recur (conj processed_rows (mask_in selected_row (nth trimmed_brick row_idx) pos_col))
               (first rest_rows)
               (rest rest_rows)
               (inc row_idx))))))

;; Works as is. But will not function if brick and gameboard is color coded
(defn no_collision_board [gameboard brick pos_row pos_col]
  "Return new state of gameboard if there is no collisions, else nil"
  (let [new_gameboard (brick_position gameboard brick pos_row pos_col)
        check_sum (+ (reduce + (flatten gameboard)) (reduce + (flatten brick)))]
    (if (= check_sum (reduce + (flatten new_gameboard)))
      new_gameboard
      nil)))

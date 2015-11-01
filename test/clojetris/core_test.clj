(ns clojetris.core-test
  (:require [clojure.test :refer :all]
            [clojetris.core :refer :all]))

(def rotated_l [[0 0 0 0]
                [0 0 0 1]
                [0 1 1 1]
                [0 0 0 0]])
(def rotated_s [[0 1 0 0]
                [0 1 0 0]
                [0 0 1 0]
                [0 0 1 0]])

(deftest bricks
  (testing "Rotation"
    (testing "L-Brick"
      (is (= (brick_rotation 1 l_brick) rotated_l))
      (is (= (brick_rotation 3 rotated_l) l_brick))
      (is (= (brick_rotation 4 l_brick) l_brick)))
    (testing "S-Brick"
      (is (= (brick_rotation 1 s_brick) rotated_s))
      (is (= (brick_rotation 3 rotated_s) s_brick))
      (is (= (brick_rotation 4 s_brick) s_brick)))))

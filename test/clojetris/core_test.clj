(ns clojetris.core-test
  (:require [clojure.test :refer :all]
            [clojetris.core :refer :all]
            [midje.sweet :refer [fact]]))

(def rotated_l [[0 0 0 0]
                [0 0 0 1]
                [0 1 1 1]
                [0 0 0 0]])

(def rotated_s [[0 1 0 0]
                [0 1 0 0]
                [0 0 1 0]
                [0 0 1 0]])

(def lbrick1_board [[0 0 0 0 0 0]
                    [0 0 1 0 0 0]
                    [0 0 1 0 0 0]
                    [0 0 1 1 0 0]
                    [0 0 0 0 0 0]])

(def lbrick2_board [[0 0 0 0 0 0]
                    [1 0 0 0 0 0]
                    [1 0 0 0 0 0]
                    [1 1 0 0 0 0]
                    [0 0 0 0 0 0]])

(def lbrick3_board [[0 0 0 0 0 0]
                    [0 0 0 0 0 0]
                    [0 0 0 0 0 0]
                    [1 0 0 0 0 0]
                    [0 0 0 0 0 0]])

(def lbrick4_board [[0 1 0 0 0 0]
                    [0 1 0 0 0 0]
                    [0 1 1 0 0 0]
                    [0 0 0 0 0 0]
                    [0 0 0 0 0 0]])

(def lbrick5_board [[0 0 0 0 0 0]
                    [0 0 0 0 0 0]
                    [0 0 0 0 0 0]
                    [0 1 0 0 0 0]
                    [0 1 0 0 0 0]])

(def test_board [[0 0 0 0 0 0]
                 [0 0 0 0 0 0]
                 [0 0 0 0 0 0]
                 [0 0 0 0 0 0]
                 [0 0 0 0 0 0]])

(def test_row [0 0 0 0 0 0])
(def test_b_row [0 1 0 0])


(deftest bricks
  (testing "Gameboard creation"
    (fact (create_gameboard 5 6) => test_board))
  (testing "Rotation"
    (testing "L-Brick"
      (is (= (brick_rotation 1 l_brick) rotated_l))
      (is (= (brick_rotation 3 rotated_l) l_brick))
      (is (= (brick_rotation 4 l_brick) l_brick)))
    (testing "S-Brick"
      (is (= (brick_rotation 1 s_brick) rotated_s))
      (is (= (brick_rotation 3 rotated_s) s_brick))
      (fact s_brick => (brick_rotation 4 s_brick))))
  (testing "Brick movement"
    (testing "Position brick on gameboard"
      (fact (brick_position test_board l_brick 0 1) => lbrick1_board)
      (fact (brick_position test_board l_brick 0 -1) => lbrick2_board)
      (fact (brick_position test_board l_brick 0 -2) => lbrick3_board)
      (fact (brick_position test_board l_brick 0 -3) => test_board)
      (fact (brick_position test_board l_brick -3 -3) => test_board)
      (fact (brick_position test_board l_brick -1 0) => lbrick4_board)
      (fact (brick_position test_board l_brick 2 0) => lbrick5_board))
    (testing "Collision"
      (fact (no_collision_board test_board l_brick 0 1) => lbrick1_board)
      (fact (no_collision_board lbrick1_board l_brick 0 1) => nil)))
  (testing "Row trimming"
    (fact (trim_into test_row test_b_row -1) => [1 0 0])
    (fact (trim_into test_row test_b_row 4) => [0 1])
    (fact (trim_into test_row test_b_row 5) => [0])
    (fact (trim_into test_row test_b_row 6) => [])))

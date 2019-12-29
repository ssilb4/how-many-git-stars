(ns gitapi.core-test
  (:require [clojure.test :refer :all]
            [gitapi.core :refer :all]))

(deftest jsonParser-test
  (testing "FIXME, I fail."
    (is (= (jsonParser "asdfasdfasfsaf \"\"\"\"stargazers_count\":   fasdffsad") "   fasdffsad"))))

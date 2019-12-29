(ns gitapi.core
  (:require 
    [clj-http.client :as http]
    [clojure.string :as str])
  (:gen-class))

(def completeURL "https://api.github.com/users/")
(def repos "/repos")

(def progress (atom 0))
(def index    (atom -1))
(def result   (atom []))
(def _0Bar   "○○○○○   0%")
(def _20Bar  "●○○○○  20%")
(def _40Bar  "●●○○○  40%")
(def _60Bar  "●●●○○  60%")
(def _80Bar  "●●●●○  80%")
(def _100Bar "●●●●● 100%")

(defn printProgressBar
  [currentValue beforeValue]
  (case currentValue
    0   (do (println _0Bar)   (reset! progress 20))
    20  (do (println _20Bar)  (reset! progress 40))
    40  (do (println _40Bar)  (reset! progress 60))
    60  (do (println _60Bar)  (reset! progress 80))
    80  (do (println _80Bar)  (reset! progress 100))
    100 (do (println _100Bar))))

(add-watch progress :progress
  (fn [key atom old-state new-state]
    (printProgressBar new-state old-state)))


(defn jsonParser
  [json index]
    (swap! result assoc index (reduce + (map #(Integer/parseInt %) (take-nth 2 (rest (str/split (str json) #"stargazers_count\\\":|,\\\"watchers_count\\\":"))))))
    (println @result))

(defn getStars
  [name index]
    (do
      (println (str "url: " completeURL name repos))
      (http/get (str completeURL name repos) {:async? true}
        (fn [response]  (jsonParser (str response) index))
        (fn [exception] (println "exception message is: " (.getMessage exception))))))

(defn -main
  "Production"
  [& args]
    (reset! result (vec args))
    (println @result)
    (if (nil? args)
      (println "Usage: lein run [user-id]")
      (do (dorun (map (fn [value] 
                          (swap! index inc) 
                          (getStars value @index)) args))
          (reset! progress 20))))
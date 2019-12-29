(ns gitapi.core
  (:require 
    [clj-http.client :as http]
    [clojure.string :as str])
  (:gen-class))

(def completeURL "https://api.github.com/users/")
(def repos "/repos")

(def progress (atom -1))
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
    0   (do (println _0Bar)  (swap! progress inc))
    20  (do (println _20Bar) (swap! progress inc))
    40  (do (println _40Bar) (swap! progress inc))
    60  (do (println _60Bar) (swap! progress inc))
    80  (do (println _80Bar) (swap! progress inc))
    100 (println _100Bar)
    (swap! progress inc)))

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
                          (getStars value @index)) args))))
    (swap! progress inc))
(ns gitapi.core
  (:require 
    [clj-http.client :as http]
    [clojure.string :as str])
  (:gen-class))

(def completeURL "https://api.github.com/users/")
(def repos "/repos")

(defn getJson
  [name]
    (do
      (println (str "url: " completeURL name repos))
      (http/get (str completeURL name repos) {:query-params {} ;; you can submit query params here
                       ;:as :json
                       })))

(defn jsonParser
  [json]
    (reduce + (map #(Integer/parseInt %) (take-nth 2 (rest (str/split (str json) #"stargazers_count\\\":|,\\\"watchers_count\\\":"))))))

(defn -main
  "Production"
  [& args]
    (if (nil? args)
      (println "Usage: lein run [user-id]")
      (println (jsonParser (getJson (str (first args)))))))
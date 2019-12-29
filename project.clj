(defproject gitapi "0.1.0-SNAPSHOT"
  :description "An example project demonstrating how to build a REST API in Clojure"
  :dependencies [
    [org.clojure/clojure "1.10.0"]
    [clj-http "3.7.0"]]
  :main ^:skip-aot gitapi.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all} })
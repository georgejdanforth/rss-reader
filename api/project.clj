(defproject api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring-cors "0.1.8"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [cheshire "5.6.3"]
                 [clj-http "3.3.0"]
                 [clj-time "0.14.4"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [clj-postgresql "0.7.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler api.handler/api}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})

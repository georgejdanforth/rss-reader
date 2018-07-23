(ns api.db
  (:require [clj-postgresql.core :as pg]
            [clojure.java.jdbc :as jdbc]
            [clojure.set :refer [rename-keys]]))

(def db-config
  (pg/spec
    :host "localhost"
    :user "georgejdanforth"
    :dbname "rss_db"
    :port 5432))

(def feed-camel-case-keys
  {:feed_url :feedUrl
   :site_url :siteUrl
   :image_url :imageUrl})

(defn insert-feed [feed]
  (jdbc/insert! db-config :feeds feed))

(defn get-feeds []
  (map
    #(rename-keys % feed-camel-case-keys)
    (jdbc/query db-config ["SELECT id, feed_url, site_url, title FROM feeds"])))

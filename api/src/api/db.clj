(ns api.db
  (:require [clj-postgresql.core :as pg]
            [clj-postgresql.types]
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

(defn get-feed-urls []
  (jdbc/query db-config ["SELECT id, feed_url FROM feeds"]))

(defn get-cache [id]
  (:cache
    (jdbc/query db-config ["SELECT cache FROM feeds WHERE id = ?" id])))

(defn update-cache [[id cache-val]]
  (jdbc/update! db-config :feeds {:cache cache-val} ["id = ?" id]))

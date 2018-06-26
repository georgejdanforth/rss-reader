(ns api.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.set :refer [rename-keys]]))

(def db-config
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/database.db"})

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

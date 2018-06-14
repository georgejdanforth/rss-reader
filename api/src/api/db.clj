(ns api.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-config
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/database.db"})

(defn insert-feed [feed]
  (jdbc/insert! db-config :feeds feed))

(defn get-feed-urls []
  (map :feed_url (jdbc/query db-config ["SELECT feed_url FROM feeds"])))

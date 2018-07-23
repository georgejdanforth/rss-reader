(ns api.update
  (:require [api.db :as db]
            [api.feed-parser :refer :all]))

(defn get-updated-feeds []
  (let [feed-urls (db/get-feed-urls)]
    (zipmap
      (map :id feed-urls)
      (map parse-full-feed (map :feed_url feed-urls)))))

(defn -main
  [& args]
  (println (get-updated-feeds)))

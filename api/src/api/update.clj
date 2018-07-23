(ns api.update
  (:require [api.db :as db]
            [api.feed-parser :refer :all]))

(defn get-updated-feeds []
  (let [feed-urls (db/get-feed-urls)]
    (zipmap
      (map :id feed-urls)
      (map
        #((comp (partial map parse-item) items parse-from-url) %)
        (map :feed_url feed-urls)))))

(defn -main
  [& args]
  (println (get-updated-feeds)))

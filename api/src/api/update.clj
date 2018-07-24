(ns api.update
  (:require [api.db :as db]
            [api.feed-parser :refer :all]
            [clojure.data :refer :all]))

(defn get-updated-feeds []
  (let [feed-urls (db/get-feed-urls)]
    (zipmap
      (map :id feed-urls)
      (map parse-full-feed (map :feed_url feed-urls)))))

(defn get-updated-cache [[id feed]]
  {id
   (sort
     pubdate-comp
     (let [cache (db/get-cache id)]
       (take 100
             (if cache
               (into cache (filter #(not= % nil) (first (diff feed cache))))
               feed))))})

(defn update-caches []
  (map db/update-cache
       (apply merge (map get-updated-cache (get-updated-feeds)))))

(defn -main
  [& args]
  (println (get-updated-feeds)))

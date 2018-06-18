(ns api.feed-parser
  (:require [clojure.data.xml :as xml]
            [clojure.string :as string]
            [clj-http.client :as http]
            [clj-time.core :as clj-time]
            [clj-time.format :as clj-time-format]))

(defn text-content [element]
  (first (:content element)))

(defn image-url [image]
  (text-content (first (filter #(= (:tag %) :url) (:content image)))))

(def metadata-tag-info
  {:title {:column-name :title :parser text-content}
   :link {:column-name :site_url :parser text-content}
   :image {:column-name :image_url :parser image-url}})

(def item-tag-info
  {:title {:key :itemTitle :parser text-content}
   :link {:key :url :parser text-content}
   :description {:key :description :parser text-content}
   :pubDate {:key :pubDate :parser text-content}})

(defn parse-from-url [url]
  (first (:content (xml/parse-str (:body (http/get url))))))

(defn metadata [url root]
  (into
    {:feed_url url}
    (map
      (fn [element]
        (let [tag-info ((:tag element) metadata-tag-info)]
          [(:column-name tag-info) ((:parser tag-info) element)]))
      (filter
        (fn [element]
          (and
            (contains? metadata-tag-info (:tag element))
            (not-empty (:content element))))
        (:content root)))))

(defn items [root]
  (filter
    (fn [element] (= :item (:tag element)))
    (:content root)))

(defn parse-item [item]
  (into
    {}
    (map
      (fn [element]
        (let [tag-info ((:tag element) item-tag-info)]
          [(:key tag-info) ((:parser tag-info) element)]))
      (filter
        (fn [element]
          (and
            (contains? item-tag-info (:tag element))
            (not-empty (:content element))))
        (:content item)))))

(defn parse-pubdate [item]
  (clj-time-format/parse
    (clj-time-format/formatters :rfc822)
    (string/replace (:pubDate item) #"GMT" "+0000")))

(defn pubdate-comp [item-1 item-2]
  (apply clj-time/after? (map parse-pubdate [item-1 item-2])))

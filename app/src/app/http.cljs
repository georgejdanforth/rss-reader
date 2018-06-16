(ns app.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))


(def feeds-url "http://localhost:3000/feeds")

(defn get-feeds []
  (go
    (let [response (<! (http/get feeds-url))]
      (:body response))))

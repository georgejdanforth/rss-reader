(ns api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [cheshire.parse :as parse]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as ring-response]
            [api.db :refer :all]
            [api.feed-parser :refer :all]))

(defn response
  ([content] (response content 200))
  ([content status]
   (ring-response/status
     (ring-response/content-type
       (ring-response/response content)
       "application/json;charset=utf-8")
     status)))

(defn add-feed [body]
  (let [url (:url body)]
    (insert-feed
      (metadata
        url
        (parse-from-url url))))
  (response nil))

(defn get-feeds []
  (response
    (map
      (comp (partial map parse-item) items parse-from-url)
      (get-feed-urls))))

(defroutes api-routes
  (context "/feeds" [] (defroutes feeds-routes
    (GET "/" [] (get-feeds))
    (POST "/add" {body :body} (add-feed body))))
  (route/not-found "Not Found"))

(def api
  (-> api-routes
      (wrap-defaults api-defaults)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

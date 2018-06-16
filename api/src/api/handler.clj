(ns api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [cheshire.parse :as parse]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as ring-response]
            [api.db :as db]
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
    (db/insert-feed
      (metadata
        url
        (parse-from-url url))))
  (response nil))

(defn get-feeds []
  (response
    (map
      (fn [feed-metadata]
        (into
          feed-metadata
          {:items
           ((comp (partial map parse-item) items parse-from-url :feed_url)
            feed-metadata)}))
      (db/get-feeds))))

(defroutes api-routes
  (context "/feeds" [] (defroutes feeds-routes
    (GET "/" [] (get-feeds))
    (POST "/add" {body :body} (add-feed body))))
  (route/not-found "Not Found"))

(def api
  (routes
    (-> #'api-routes
        (wrap-cors :access-control-allow-origin [#".*"]
                   :access-control-allow-credentials "true"
                   :access-control-allow-methods [:get :put :post :delete])
        (wrap-json-body {:keywords? true})
        (wrap-json-response)
        (wrap-defaults api-defaults))))

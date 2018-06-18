(ns app.core
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

;; -------------------------
;; Setup

(def feeds-url "http://localhost:3000/feeds")

;; -------------------------
;; Views

(defn feed-item [item]
  [:div {:key (:url item)}
   [:h4 [:a {:href (:url item)} (:itemTitle item)]]
   [:p (:description item)]])

(defn home-page []
  (let [feed-data (r/atom [])]
    (r/create-class
      {:component-did-mount
       (fn []
         (ajax/GET feeds-url {:params {}
                              :handler
                              (fn [response] (reset! feed-data response))
                              :error-handler (fn [response] (prn response))
                              :response-format :json
                              :keywords? true}))
       :reagent-render
       (fn []
         [:div
           (if (empty? @feed-data)
             [:h2 "Loading..."]
             [:div
              (for [item @feed-data]
                (feed-item item))])])})))

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

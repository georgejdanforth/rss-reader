(ns app.core
  (:require [reagent.core :as r]
            [app.http :as http]))

;; -------------------------
;; Views

(defn home-page []
  [:div
   [:h2 "Welcome to Reagent"]
   [:button#do-request {:on-click http/get-feeds}
    "Do Request"]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

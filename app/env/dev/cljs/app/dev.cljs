(ns ^:figwheel-no-load app.dev
  (:require
    [app.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)

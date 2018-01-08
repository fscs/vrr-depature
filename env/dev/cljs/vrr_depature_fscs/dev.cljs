(ns ^:figwheel-no-load vrr-depature-fscs.dev
  (:require
    [vrr-depature-fscs.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)

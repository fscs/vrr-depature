(ns vrr-depature-fscs.core
    (:require-macros [cljs.core.async.macros :refer [go go-loop]])
    (:require
      [reagent.core :as r]
      [cljs-http.client :as http]
      [cljs.core.async :refer [<! timeout]]))

(def no-lines 7)

(defn depature-row [[line destination depature]]
  [:li.list-group-item.d-flex.justify-content-between.align-items-center
   [:span.subway-line line]
   [:span.subway-destination destination]
   [:span.subway-depature depature]])

(defn depature-monitor [title url]
    (let [query-url (str url "?frontend=json&no_lines=6" no-lines)
          state (r/atom {:data [["a" "1" "z"] ["b" "2" "y"] ["c" "3" "x"]]})
          refresh (fn [response]
                    (reset! state {:data (take no-lines (get-in response [:body :preformatted]))}))]

      (go-loop []
          (refresh (<! (http/get query-url {:with-credentials? false})))
          (<! (timeout 5000))
          (recur))

      (fn []
          [:div.card.card-body
           [:div.card.card-title title]
           [:ul.list-group
            (for [line (:data @state)]
               ^{:key line} [depature-row line])]])))



;; -------------------------
;; Views

(defn home-page []
    [:div.fullscreen
     [:div.row.h-50
      [:div.col-md [depature-monitor "Uni Ost" "https://vrrf.finalrewind.org/Düsseldorf/Universitaet Ost.json"]]
      [:div.col-md [depature-monitor "Uni Mitte" "https://vrrf.finalrewind.org/Düsseldorf/Uni Mitte.json"]]]
     [:div.row.h-50
      [:div.col-md [depature-monitor "Christophstraße" "https://vrrf.finalrewind.org/Düsseldorf/Uni Nord/Christophstraße.json"]]
      [:div.col-md [depature-monitor "Bilk S" "https://vrrf.finalrewind.org/Düsseldorf/Bilk S.json"]]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

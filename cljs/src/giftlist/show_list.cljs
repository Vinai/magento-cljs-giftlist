(ns giftlist.show-list
  (:require [re-frame.core :refer [dispatch subscribe]]))

(def <sub (comp deref subscribe))

(defn gift [recipient [sku product-name]]
  [:div product-name])

(defn gift-list-recipient [recipient]
  (if (<sub [:has-gifts? recipient])
    [:div.gift-recipient
     [:div.name
      [:strong recipient]]
     (into [:div.gifts]
           (mapv (partial gift recipient)
                 (<sub [:gifts-for recipient])))]))

(defn gift-list []
  (into [:div] (map gift-list-recipient (<sub [:recipients]))))

(defn render []
  (if (<sub [:gift-list?])
    [:div.block.widget
     [:div.block-title
      [:strong "Gift Plan"]]
     [:div.block-content
      [gift-list]]]))

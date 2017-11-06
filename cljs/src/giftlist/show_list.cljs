(ns giftlist.show-list
  (:require [re-frame.core :refer [dispatch subscribe]]))

(def <sub (comp deref subscribe))

(defn delete [event]
  [:a {:on-click #(dispatch event)
       :style    {:cursor "pointer"}}
   " (X)"])

(defn gift [recipient [sku product-name]]
  [:div product-name
   [delete [:remove-gift recipient sku]]])

(defn gift-list-recipient [recipient]
  (if (<sub [:has-gifts? recipient])
    [:div.gift-recipient
     [:div.name
      [:strong recipient]
      [delete [:remove-recipient recipient]]]
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

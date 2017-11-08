(ns giftlist.show-list
  (:require [re-frame.core :refer [dispatch subscribe]]))

(def <sub (comp deref subscribe))

(defn rm-button [event]
  [:a {:on-click #(dispatch event)
       :style    {:cursor "pointer"}}
   [:span [:span.icon-remove] " (X)"]])

(defn gift [recipient [sku product-name]]
  [:div product-name " " [rm-button [:remove-gift recipient sku]]])

(defn gift-list-recipient [recipient]
  (if (<sub [:has-gifts? recipient])
    [:div.gift-recipient
     [:div.name
      [:strong recipient] " "
      [rm-button [:remove-recipient recipient]]]
     (into [:div.gifts]
           (mapv (partial gift recipient)
                 (<sub [:gifts-for recipient])))]))

(defn gift-list []
  (into [:div] (map gift-list-recipient (<sub [:recipients]))))

(defn render []
  (if (<sub [:gift-list?])
    [:div.block.widget
     [:div.block-title
      [:strong
       (if-let [owner (<sub [:owner])]
         (str owner "'s Plan")
         "Gift Plan")]]
     [:div.block-content
      [gift-list]]]))

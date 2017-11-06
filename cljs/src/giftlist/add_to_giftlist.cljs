(ns giftlist.add-to-giftlist
  (:require [re-frame.core :refer [dispatch subscribe]]
            [giftlist.storage]))

(def <sub (comp deref subscribe))

(defn escape? [event]
  (or (= "Escape" (.-key event)) (= 27 (.-keyCode event))))

(defn enter? [event]
  (or (= "Enter" (.-key event)) (= 13 (.-keyCode event))))

(defn recipient-input [sku product-name]
  [:input.input-text
   {:placeholder  "For whom?"
    :style        {:width "100%"}
    :auto-focus   true
    :on-key-press (fn [e]
                    (when (enter? e)
                      (let [recipient (.. e -target -value)]
                        (dispatch [:hide-input])
                        (dispatch [:add-to-list sku product-name recipient]))))
    :on-key-down  #(when (escape? %) (dispatch [:hide-input]))}])

(defn button [sku]
  [:button.action {:on-click #(dispatch [:show-input sku])} "Gift to"])

(defn render [sku product-name]
  [:div
   (if (<sub [:show-input? sku])
     [recipient-input sku product-name]
     [button sku])])

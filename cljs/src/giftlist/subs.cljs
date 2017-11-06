(ns giftlist.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub :show-input?
         (fn [db [_ sku]]
           (and sku (= sku (:input-active db)))))

(reg-sub :gift-list
         (fn [db _]
           (:gift-list db)))

(reg-sub :gift-list?
         :<- [:gift-list]
         (fn [gift-list]
           (seq gift-list)))

(reg-sub :recipients
         :<- [:gift-list]
         (fn [gift-list]
           (keys gift-list)))

(reg-sub :gifts-for
         :<- [:gift-list]
         (fn [gift-list [_ recipient]]
           (seq (get gift-list recipient))))

(reg-sub :has-gifts?
         (fn [[_ recipient]]
           (subscribe [:gifts-for recipient]))
         (fn [gifts-for-recipient _]
           (< 0 (count gifts-for-recipient))))

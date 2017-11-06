(ns giftlist.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx dispatch]]))

(reg-event-fx :init-db [(inject-cofx :storage)]
              (fn [cofx]
                (when (empty? (:db cofx))
                  {:db (:storage cofx)})))

(reg-event-db :show-input
              (fn [db [_ sku]]
                (assoc db :input-active sku)))

(reg-event-db :hide-input
              (fn [db _]
                (dissoc db :input-active)))

(defn remove-recipients-without-gifts [recipients]
  (let [no-gifts? (fn [[recipient gifts]]
                    (empty? gifts))]
    (into {} (remove no-gifts? recipients))))

(defn clean-db-for-storage [db]
  (dissoc db :input-active))

(reg-event-fx :persist-giftlist
              (fn [cofx]
                {:storage (clean-db-for-storage (:db cofx))}))

(reg-event-fx :add-to-list
              (fn [cofx [_ sku product-name recipient]]
                (let [db           (:db cofx)
                      path         [:gift-list recipient sku]]
                  {:db       (assoc-in db path product-name)
                   :dispatch [:persist-giftlist]})))

(reg-event-fx :remove-recipient
              (fn [cofx [_ recipient]]
                (let [db     (:db cofx)]
                  {:db       (update db :gift-list dissoc recipient)
                   :dispatch [:persist-giftlist]})))

(reg-event-fx :remove-gift
              (fn [cofx [_ recipient sku]]
                (let [db     (:db cofx)
                      gifts  (dissoc (get-in db [:gift-list recipient]) sku)]
                  (if (empty? gifts)
                    {:dispatch [:remove-recipient recipient]}
                    {:db       (assoc-in db [:gift-list recipient] gifts)
                     :dispatch [:persist-giftlist]}))))

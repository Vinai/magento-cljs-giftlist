(ns giftlist.core
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch-sync dispatch]]
            [clojure.string :as string]
            [giftlist.events]
            [giftlist.subs]
            [giftlist.add-to-giftlist :as add-to-giftlist]
            [giftlist.show-list :as show-list]))

(defn document-loaded? []
  (= "complete" (.-readyState js/document)))


(comment "JS Version:
function subscribe_to_magento_customer_data_fn (updateFn) {
  require(['Magento_Customer/js/customer-data'],
    function (customerData) {
        var customer = customerData.get('customer');
        customer.subscribe(updateFn);
        updateFn(customer());
  });
}
")

(defn subscribe-to-customer-data-fn [update-fn]
  (js/window.require (array "Magento_Customer/js/customer-data")
                     (fn [customer-data]
                       (let [customer (.get customer-data "customer")]
                         (.subscribe customer update-fn)
                         (update-fn (customer))))))

(defn subscribe-to-customer-name []
  (if (document-loaded?)
    (subscribe-to-customer-data-fn #(dispatch [:set-owner-name (.-firstname %)]))
    (js/window.addEventListener "load" subscribe-to-customer-name)))

(defn get-elements-by-class-name [class]
  (let [nodes (js/document.getElementsByClassName class)]
    (seq (js/Array.from nodes))))

(defn select-add-gift-dom-nodes []
  (get-elements-by-class-name "giftlist-add-gift"))

(defn select-show-list-dom-nodes []
  (get-elements-by-class-name "giftlist-show-list"))

(defn nth-parent [node n]
  (reduce #(.-parentElement %) node (range n)))

(defn find-product-details-parent [react-root]
  (nth-parent react-root 4))

(defn find-add-to-cart-form [product-node]
  (.querySelector product-node "form[data-product-sku]"))

(defn find-sku [product-node]
  (if-let [form (find-add-to-cart-form product-node)]
    (.getAttribute form "data-product-sku")))

(defn find-product-name [product-node]
  (let [selectors [".product.name a" ".page-title-wrapper.product .page-title"]]
    (when-let [name-node (some #(.querySelector product-node %) selectors)]
      (string/trim (.-textContent name-node)))))

(defn render-add-gift [insert-node]
  (let [product-details (find-product-details-parent insert-node)
        product-sku     (find-sku product-details)
        product-name    (find-product-name product-details)]
    (reagent/render [add-to-giftlist/render product-sku product-name] insert-node)))

(defn render-show-list [inserl-node]
  (reagent/render [show-list/render] inserl-node))

(defn render-when-ready! [selector-fn render-fn]
  (if-let [insert-nodes (selector-fn)]
    (mapv render-fn insert-nodes)
    (when-not (document-loaded?)
      (.addEventListener js/document "DOMContentLoaded"
                         #(render-when-ready! selector-fn render-fn)))))

(defn render! []
  (render-when-ready! select-add-gift-dom-nodes render-add-gift)
  (render-when-ready! select-show-list-dom-nodes render-show-list))

(dispatch-sync [:init-db])

(subscribe-to-customer-name)

(render!)

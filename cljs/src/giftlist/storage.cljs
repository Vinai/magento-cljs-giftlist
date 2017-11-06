(ns giftlist.storage
  (:require [re-frame.core :refer [reg-fx reg-cofx]]
            [cljs.reader]))

(defn- report-exception [e]
  (js/console.log (str "An error occurred while recovering "
                       "the local state, starting from scratch."))
  (js/console.log (.-stack e))
  nil)

(defn- to-string [val]
  (pr-str val))

(defn- from-string [val]
    (try (cljs.reader/read-string val)
      (catch js/Error e (report-exception e))))

(defn set-item!
  "Set 'key' in browsers sessionStorage to 'val'."
  [key val]
  (.. js/window -sessionStorage (setItem key (to-string val))))

(defn get-item
  "Returns value of 'key' from browsers sessionStorage."
  ([key default] (if-let [val (get-item key)] val default))
  ([key]
   (when-let [val (.. js/window -sessionStorage (getItem key))]
     (from-string val))))

(defn remove-item!
  "Remove the browsers sessionStorage value for the given 'key'"
  [key]
  (.. js/window -sessionStorage (removeItem key)))

(reg-fx :storage
        (fn [db]
          (set-item! :app-db db)))

(reg-cofx :storage
          (fn storage-coeffects-handler
            [cofx]
            (let [stored-db (get-item :app-db)]
              (assoc cofx :storage stored-db))))

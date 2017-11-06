(ns user
    (:require [figwheel-sidecar.repl-api :as ra]))

;; 1. bash in phpstorm terminal: rlwrap lein repl
;; 2. connect to repl (Ctrl-Shift-P): localhost:[.nrepl-port]
;; 3. (start-watcher)
;; 4. (cljs)

;; put (enable-console-print!) somewhere

;; to exit cljs repl: :cljs/quit

(defn start-watcher []
  (ra/start-figwheel!))

(defn stop-watcher []
  (ra/stop-figwheel!))

(defn cljs []
  (ra/cljs-repl "dev"))

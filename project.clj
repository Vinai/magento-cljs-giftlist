(defproject example-giftlist "0.1.0-SNAPSHOT"
            :dependencies
                          [[org.clojure/clojure "1.8.0"]
                           [org.clojure/clojurescript "1.9.946"]
                           [re-frame "0.10.2"]]
            :plugins      [[lein-figwheel "0.5.14"] [lein-cljsbuild "1.1.7"]]
            :clean-targets
                          [:target-path
                           [:cljsbuild :builds :dev :compiler :output-dir]
                           [:cljsbuild :builds :dev :compiler :output-to]
                           [:cljsbuild :builds :min :compiler :output-dir]]
            :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
            :cljsbuild
                          {:builds
                           {:dev {:source-paths ["cljs/src"]
                                  :figwheel     true
                                  :compiler     {:main                 "giftlist.core"
                                                 :output-to            "view/frontend/web/js/giftlist.js"
                                                 :output-dir           "view/frontend/web/js/dev"
                                                 :asset-path           "/static/frontend/Magento/blank/cljs/Example_GiftList/js/dev"
                                                 :source-map-timestamp true
                                                 :preloads             [devtools.preload]}}
                            :min {:source-paths ["cljs/src"]
                                  :compiler     {:main          "giftlist.core"
                                                 :output-to     "view/frontend/web/js/giftlist.js"
                                                 :output-dir    "view/frontend/web/js/min"
                                                 :optimizations :advanced
                                                 :infer-externs true
                                                 :asset-path    "/static/frontend/Magento/blank/cljs/Example_GiftList/js/dev"}}}}
                           :profiles
                           {:dev {:dependencies [[figwheel-sidecar "0.5.14"]
                                                 [com.cemerick/piggieback "0.2.1"]
                                                 [binaryage/devtools "0.9.7"]]
                                  :source-paths ["cljs/src" "cljs/dev"]}})


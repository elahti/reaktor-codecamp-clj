(defproject reaktor-codecamp-clj "0.1.0-SNAPSHOT"
  :description "Reaktor Codecamp Clojure base"
  :url "https://github.com/elahti/reaktor-codecamp-clj"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [aleph "0.4.1"]                            ; TCP / UDP
                 [cheshire "5.6.3"]                         ; JSON
                 [org.clojure/core.async "0.2.391"]
                 [com.stuartsierra/component "0.3.1"]       ; lifecycled components, dependency injection
                 [environ "1.1.0"]                          ; environment vars / config
                 [com.taoensso/timbre "4.7.4"]]             ; logging

  :profiles {:dev     {:dependencies [[reloaded.repl "0.2.3"]] ; "reloaded" development workflow
                       :env          {:server-port "10000"}
                       :repl-options {:init-ns user}
                       :plugins      [[lein-environ "1.1.0"]]
                       :source-paths ["src" "dev-src"]}
             :uberjar {:aot :all}}

  :main ^:skip-aot reaktor-codecamp-clj.core
  :target-path "target/%s")

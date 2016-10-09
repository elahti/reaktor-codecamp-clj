(ns reaktor-codecamp-clj.core
  (:require [com.stuartsierra.component :as component]
            [reaktor-codecamp-clj.handler :as handler]
            [reaktor-codecamp-clj.server :as server])
  (:gen-class))

(defn new-system
  []
  (component/system-map
    :server-event-handler handler/on-server-event

    :server (component/using
              (server/new-server)
              [:server-event-handler])))

  (defn -main
    [& _]
    (let [_ (component/start-system (new-system))]
      @(promise)))

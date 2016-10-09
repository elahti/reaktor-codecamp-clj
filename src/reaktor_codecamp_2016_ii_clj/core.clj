(ns reaktor-codecamp-2016-ii-clj.core
  (:require [com.stuartsierra.component :as component]
            [reaktor-codecamp-2016-ii-clj.handler :as handler]
            [reaktor-codecamp-2016-ii-clj.server :as server])
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

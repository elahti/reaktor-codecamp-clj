(ns reaktor-codecamp-clj.server
  (:require [aleph.udp :as udp]
            [byte-streams :as bs]
            [cheshire.core :as json]
            [clojure.core.async :as async]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [manifold.stream :as s]
            [taoensso.timbre :refer [debug error info]]))

(defmacro try!
  [body & {:keys [message]}]
  `(try
     (do ~body)
     (catch Exception e#
       (error e# ~message))))

(defn- byte-array->data
  [byte-array]
  (when-not (nil? byte-array)
    (let [json-str (bs/to-string byte-array)]
      (try! (json/parse-string json-str true)
            :message (str "Failed to parse incoming JSON: " json-str)))))

(defn- data->json
  [data]
  {:pre [(some? data)]}
  (-> data
      json/generate-string))

(defn- event-loop
  [socket stop? server-event-handler]
  (async/go-loop [input (s/take! socket)]
    (if-not (some? input)
      (throw (Exception. "Failed to read value from server stream"))
      (when-not @stop?
        (let [{:keys [port host message]} (update @input :message byte-array->data)]
          (when (some? message)
            (debug (str "Received from " host ":" port ": " message))
            (let [resp (.on-message server-event-handler message)]
              (s/put! socket {:host    host
                              :port    port
                              :message (data->json resp)})
              (debug (str "Sent to " host ":" port ": " resp)))))
        (recur (s/take! socket))))))

(defn- stopped?
  [this]
  (every? (fn [kwd]
            (nil? (get this kwd)))
          [:socket :event-loop :stop?]))

(defn- login [server-event-handler socket]
  (let [remote-host (env :remote-host)
        remote-port (Integer/valueOf ^String (env :remote-port))
        message     (.login server-event-handler)]
    @(s/put! socket {:host    remote-host
                     :port    remote-port
                     :message (json/generate-string message)})
    (info (str "Sent login message to " remote-host ":" remote-port))))

(defn- logout [server-event-handler socket]
  (let [remote-host (env :remote-host)
        remote-port (Integer/valueOf ^String (env :remote-port))
        message     (.logout server-event-handler)]
    @(s/put! socket {:host    remote-host
                     :port    remote-port
                     :message (json/generate-string message)})
    (info (str "Sent logout message to " remote-host ":" remote-port))))

(defrecord Server []
  component/Lifecycle

  (start [{:keys [server-event-handler] :as this}]
    (let [port       (Integer/valueOf ^String (env :server-port))
          socket     @(udp/socket {:port port})
          stop?      (agent false)
          event-loop (event-loop socket stop? server-event-handler)]
      (info (str "Started UDP server on port " port))
      (login server-event-handler socket)
      (merge this
        {:socket     socket
         :event-loop event-loop
         :stop?      stop?})))

  (stop [{:keys [socket event-loop stop? server-event-handler] :as this}]
    (logout server-event-handler socket)
    (when-not (stopped? this)
      (send stop? not)
      (try! (s/close! socket))
      (try! (async/close! event-loop)))
    (info (str "Stopped UDP server"))
    (merge this
      {:socket     nil
       :event-loop nil
       :stop?      nil})))

(defn new-server
  []
  (->Server))

(ns reaktor-codecamp-clj.handler)

(defprotocol EventHandler
  (on-message [handler message]
    "Handler function that receives the JSON parsed as Clojure data
     structure as it's argument. Must return a Clojure data structure
     that will be sent back to the server as response to the message.")

  (login [handler]
    "Return a Clojure data structure that will be sent as a JSON encoded
     login message payload to the remote server.")

  (logout [handler]
    "Return a Clojure data structure that will be sent as a JSON encoded
     logout message payload to the remote server."))

(defrecord UdpEventHandler []
  EventHandler

  (on-message [this message]
    {:foo "bar"})

  (login [this]
    {:log "in"})

  (logout [this]
    {:log "out"}))

(defn new-event-handler []
  (->UdpEventHandler))

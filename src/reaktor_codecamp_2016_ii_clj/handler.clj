(ns reaktor-codecamp-2016-ii-clj.handler)

(defn on-server-event
  "Handler function that receives the JSON parsed as Clojure data
   structure as it's argument. Must return a Clojure data structure
   that will be sent back to the server as response to the message."
  [message]
  {:foo "bar"})

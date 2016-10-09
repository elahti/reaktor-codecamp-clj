(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [reaktor-codecamp-clj.core :as core]))

(reloaded.repl/set-init! core/new-system)

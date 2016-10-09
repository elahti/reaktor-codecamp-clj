(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [reaktor-codecamp-2016-ii-clj.core :as core]))

(reloaded.repl/set-init! core/new-system)

(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))


(defn greet [req]
  (cond
    (= "/" (req :uri))
    {:status 200
     :body "Hello , World!"
     :headers {}}
    (= "/goodbye" (req :uri))
    {:status 200
     :body "Goodbye"
     :headers {}}
    :else
    {:status 404
     :body "Not found"
     :headers {}}))


(defn goodbye [req]
  {:status 200
   :body "Goodbye, cruel world!"
   :headers {}})


(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (not-found "Page not found."))


(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))


(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))

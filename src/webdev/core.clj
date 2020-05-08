(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(defn greet [req]
  {:status 200
   :body "Hello , World!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, cruel world!"
   :headers {}})

(defn about [req]
  {:status 200
   :body "My name is Bardia and this is an app I'm building to learn how to code web apps in Clojure"
   :headers {}})

(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status 200
     :body (str "Yo " name)
     :headers {}}))

(def ops
  {"+" +
   "-" -
   "*" *
   ":" /})

(defn calc [req]
  (let [params (req :route-params)
        a (Integer. (:a params))
        b (Integer. (:b params))
        op (ops (:op params))]
    (if op
      {:status 200
       :body (str (op a b))
       :headers {}}
      {:status 404
       :body "Not found"
       :headers {}})))

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))

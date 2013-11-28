(ns logically.abs.meta
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic :exclude [is] :as l]
        [clojure.core.logic.nominal :exclude [fresh hash] :as nom]))

(defn ack-clause [u== head body]
  (conde
   [(fresh [a b]
           (== head [:== a b])
           (u== a b)
           (== body ()))]
   [(fresh [a b c]
           (== head [:ack a b c])
           (conde
            [(fresh [n]
                    (== body [[:== a 0]
                              [:== b n]
                              [:== c [:s n]]]))]
            [(fresh [m n d e f]
                    (== body [[:== a [:s m]]
                              [:== b 0]
                              [:== c n]
                              [:== d m]
                              [:== e [:s 0]]
                              [:== f n]
                              [:ack d e f]]))]
            [(fresh [d e f g h i m n o p]
                    (== body [[:== a [:s m]]
                              [:== b [:s n]]
                              [:== c p]
                              [:== d [:s m]]
                              [:== e n]
                              [:== f o]
                              [:== g m]
                              [:== h o]
                              [:== i p]
                              [:ack d e f] [:ack g h i]]))]))]))

(defn solve* [u== clause goals]
  (conde
   [(== goals ())]
   [(fresh [head others body]
           (conso head others goals)
           (clause u== head body)
           (solve* u== clause body)
           (solve* u== clause others))]))

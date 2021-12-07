(ns tp-lenguajes-formales.core-test
  (:require [clojure.test :refer :all]
            [tp-lenguajes-formales.core :refer :all]))

(deftest es-el-doble?-test
 (testing "Prueba de la funcion: es-el-doble?"
 (is (= true (es-el-doble? 4 8)))
 (is (= false (es-el-doble? 4 7)))
 )
)

(deftest leer-entrada-test
  (testing "prueba de la funcion: leer-entrada"
    (println "Escriba (hola -enter- mundo)")
    (is (= (leer-entrada) (with-in-string '"(hola" '"mundo)")))
    (println "Escriba 123")
    (is (= (leer-entrada) '"123"))
  )
)

(deftest verificar-parentesis-test
  (testing "prueba de la funcion: verificar-parentesis"
    (is (= 1 (verificar-parentesis "(hola 'mundo")))
    (is (= -1 (verificar-parentesis "(hola '(mundo)))")))
    (is (= -1 (verificar-parentesis "(hola '(mundo) () 6) 7)")))
    (is (= -1 (verificar-parentesis "(hola '(mundo) () 6) 7) 9)")))
    (is (= 0 (verificar-parentesis "(hola '(mundo) )")))
  )
)

(deftest actualizar-amb-test
  (testing "prueba de la funcion: actualizar-amb"
    (is (= '(a 1 b 2 c 3 d 4) (actualizar-amb '(a 1 b 2 c 3) 'd 4)))
    (is (= '(a 1 b 4 c 3) (actualizar-amb '(a 1 b 2 c 3) 'b 4)))
    (is (= '(a 1 b 2 c 3) (actualizar-amb '(a 1 b 2 c 3) 'b (list (symbol ";ERROR:") 'mal 'hecho)))
    (is (= '(b 7) (actualizar-amb () 'b 7)))
  )
)

(deftest buscar-test
  (testing "prueba de la funcion: buscar"
    (is (= 3 (buscar 'c '(a 1 b 2 c 3 d 4 e 5))))
    (is (= (generar-mensaje-error :unbound-variable 'f) (buscar 'f '(a 1 b 2 c 3 d 4 e 5))))
  )
)

(deftest error?-test
  (testing "prueba de la funcion: error?"
    (is (= true (error? (list (symbol ";ERROR:") 'mal 'hecho))))
    (is (= false (error? (list 'mal 'hecho))))
    (is (= true (error? (list (symbol ";WARNING:") 'mal 'hecho))))
  )
)

(deftest proteger-bool-en-str-test
  (testing "prueba de la funcion: proteger-bool-en-str"
    (is (= '"(or %F %f %t %T)" (proteger-bool-en-str "(or #F #f #t #T)")))
    (is (= '"(and (or #F #f #t #T) #T)" (proteger-bool-en-str "(and (or #F #f #t #T) #T)")))
    (is (= '"" (proteger-bool-en-str "")))
  )
)

(deftest restaurar-bool-test
  (testing "prueba de la funcion: restaurar-bool"
    (is (= '"(and (or #F #f #t #T) #T)" (restaurar-bool (read-string (proteger-bool-en-str "(and (or #F #f #t #T) #T)"))))
    (is (= '"(and (or #F #f #t #T) #T)" (restaurar-bool (read-string "(and (or %F %f %t %T) %T)") ))
  )
)

(deftest igual?-test
  (testing "prueba de la funcion: igual?"
    (is (= true (igual? 'if 'IF)))
    (is (= true (igual? 'if 'if)))
    (is (= true (igual? 'IF 'IF)))
    (is (= false (igual? 'IF "IF")))
    (is (= false (igual? 6 "6")))
  )
)

(deftest fnc-append-test
  (testing "prueba de la funcion: fnc-append"
    (is (= '(1 2 3 4 5 6 7) (fnc-append '( (1 2) (3) (4 5) (6 7)))))
    (is (= (generar-mensaje-error :wrong-type-arg 3) (fnc-append '( (1 2) 3 (4 5) (6 7)))))
    (is (= (generar-mensaje-error :wrong-type-arg 'A) (fnc-append '( (1 2) A (4 5) (6 7)))))
  )
)

(deftest fnc-equal?-test
  (testing "prueba de la funcion: fnc-equal?"
    (is (= '"#t" (fnc-equal? ())))
    (is (= '"#t" (fnc-equal? '(A))))
    (is (= '"#t" (fnc-equal? '(A a))))
    (is (= '"#t" (fnc-equal? '(A a A))))
    (is (= '"#t" (fnc-equal? '(A a A a))))
    (is (= '"#f" (fnc-equal? '(A a A B))))
    (is (= '"#t" (fnc-equal? '(1 1 1 1))))
    (is (= '"#f" (fnc-equal? '(1 1 2 1))))    
  )
)

(deftest fnc-read-test
  (testing "prueba de la funcion: fnc-read"
    (println "Escriba (hola -enter- mundo)")
    (is (= '(hola mundo) (fnc-read ())))
    (is (= (generar-mensaje-error :io-ports-not-implemented 'read) (fnc-read '(1))))
    (is (= (generar-mensaje-error :wrong-number-args '#<primitive-procedure read>) (fnc-read '(1 2))))
    (is (= (generar-mensaje-error :wrong-number-args '#<primitive-procedure read>) (fnc-read '(1 2 3))))  
  )
)

(deftest fnc-sumar-test
  (testing "prueba de la funcion: fnc-sumar"
    (is (= 0 (fnc-sumar ())))
    (is (= 3 (fnc-sumar '(3))))
    (is (= 7 (fnc-sumar '(3 4))))
    (is (= 12 (fnc-sumar '(3 4 5))))
    (is (= 18 (fnc-sumar '(3 4 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '"+" 'A) (fnc-sumar '(A 4 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg2 '"+" 'A) (fnc-sumar '(3 A 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg2 '"+" 'A) (fnc-sumar '(3 4 A 6))))    
  )
)

(deftest fnc-restar-test
  (testing "prueba de la funcion: fnc-restar"
    (is (= (generar-mensaje-error :wrong-number-args '"-") (fnc-restar ())))
    (is (= -3 (fnc-restar '(3))))
    (is (= -1 (fnc-restar '(3 4))))
    (is (= -6 (fnc-restar '(3 4 5))))
    (is (= -12 (fnc-restar '(3 4 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '"-" 'A) (fnc-restar '(A 4 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg2 '"-" 'A) (fnc-restar '(3 A 5 6))))
    (is (= (generar-mensaje-error :wrong-type-arg2 '"-" 'A) (fnc-restar '(3 4 A 6))))    
  )
)

(deftest fnc-menor-test
  (testing "prueba de la funcion: fnc-menor"
    (is (= '"#t" (fnc-menor ())))
    (is (= '"#t" (fnc-menor '(1))))
    (is (= '"#t" (fnc-menor '(1 2))))
    (is (= '"#t" (fnc-menor '(1 2 3))))
    (is (= '"#t" (fnc-menor '(1 2 3 4))))
    (is (= '"#f" (fnc-menor '(1 2 2 4))))
    (is (= '"#f" (fnc-menor '(1 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '"<" 'A) (fnc-menor '(A 1 2 4))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '"<" 'A) (fnc-menor '(1 A 2 4))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '"<" 'A) (fnc-menor '(1 2 A 4))))    
  )
)

(deftest fnc-mayor-test
  (testing "prueba de la funcion: fnc-mayor"
    (is (= '"#t" (fnc-mayor ())))
    (is (= '"#t" (fnc-mayor '(1))))
    (is (= '"#t" (fnc-mayor '(2 1))))
    (is (= '"#t" (fnc-mayor '(3 2 1))))
    (is (= '"#t" (fnc-mayor '(4 3 2 1))))
    (is (= '"#f" (fnc-mayor '(4 2 2 1))))
    (is (= '"#f" (fnc-mayor '(4 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '">" 'A) (fnc-mayor '(A 3 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">" 'A) (fnc-mayor '(3 A 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">" 'A) (fnc-mayor '(3 2 A 1))))    
  )
)

(deftest fnc-mayor-o-igual-test
  (testing "prueba de la funcion: fnc-mayor-o-igual"
    (is (= '"#t" (fnc-mayor ())))
    (is (= '"#t" (fnc-mayor '(1))))
    (is (= '"#t" (fnc-mayor '(2 1))))
    (is (= '"#t" (fnc-mayor '(3 2 1))))
    (is (= '"#t" (fnc-mayor '(4 3 2 1))))
    (is (= '"#t" (fnc-mayor '(4 2 2 1))))
    (is (= '"#f" (fnc-mayor '(4 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '">=" 'A) (fnc-mayor '(A 3 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">=" 'A) (fnc-mayor '(3 A 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">=" 'A) (fnc-mayor '(3 2 A 1))))    
  )
)

(deftest evaluar-escalar-test
  (testing "prueba de la funcion: evaluar-escalar"
    (is (= '(32 (x 6 y 11 z "hola")) (evaluar-escalar 32 '(x 6 y 11 z "hola"))))
    (is (= '("chau" (x 6 y 11 z "hola")) (evaluar-escalar "chau" '(x 6 y 11 z "hola"))))
    (is (= '(11 (x 6 y 11 z "hola")) (evaluar-escalar 'y '(x 6 y 11 z "hola"))))
    (is (= '("hola" (x 6 y 11 z "hola")) (evaluar-escalar 'z '(x 6 y 11 z "hola"))))
    (is (= '(11 (x 6 y 11 z "hola")) (evaluar-escalar 'n '(x 6 y 11 z "hola"))))
    (is (= '((generar-mensaje-error :unbound-variable escalar) (x 6 y 11 z "hola")) (evaluar-escalar 'y '(x 6 y 11 z "hola"))))
  )
)
(ns tp-lenguajes-formales.core-test
  (:require [clojure.test :refer :all]
            [tp-lenguajes-formales.core :refer :all]))

(deftest leer-entrada-test
  (testing "prueba de la funcion: leer-entrada"
    (println "Escriba (hola -enter- mundo)")
    (is (= (leer-entrada) '"(hola mundo)"))
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
    (is (= '(a 1 b 2 c 3) (actualizar-amb '(a 1 b 2 c 3) 'b (list (symbol ";ERROR:") 'mal 'hecho))))
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
    (is (= '"(and (or %F %f %t %T) %T)" (proteger-bool-en-str "(and (or #F #f #t #T) #T)")))
    (is (= '"" (proteger-bool-en-str "")))
  )
)

(deftest restaurar-bool-test
  (testing "prueba de la funcion: restaurar-bool"
    (is (= (list 'and (list 'or (symbol "#F") (symbol "#f") (symbol "#t") (symbol "#T")) (symbol "#T")) (restaurar-bool (read-string (proteger-bool-en-str "(and (or #F #f #t #T) #T)")))))
    (is (= (list 'and (list 'or (symbol "#F") (symbol "#f") (symbol "#t") (symbol "#T")) (symbol "#T")) (restaurar-bool (read-string "(and (or %F %f %t %T) %T)"))))
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
    (is (= (symbol "#t") (fnc-equal? ())))
    (is (= (symbol "#t") (fnc-equal? '(A))))
    (is (= (symbol "#t") (fnc-equal? '(A a))))
    (is (= (symbol "#t") (fnc-equal? '(A a A))))
    (is (= (symbol "#t") (fnc-equal? '(A a A a))))
    (is (= (symbol "#f") (fnc-equal? '(A a A B))))
    (is (= (symbol "#t") (fnc-equal? '(1 1 1 1))))
    (is (= (symbol "#f") (fnc-equal? '(1 1 2 1))))    
  )
)

(deftest fnc-read-test
  (testing "prueba de la funcion: fnc-read"
    (println "Escriba (hola -enter- mundo)")
    (is (= '(hola mundo) (fnc-read ())))
    (is (= (generar-mensaje-error :io-ports-not-implemented 'read) (fnc-read '(1))))
    (is (= (generar-mensaje-error :wrong-number-args-prim-proc 'read) (fnc-read '(1 2))))
    (is (= (generar-mensaje-error :wrong-number-args-prim-proc 'read) (fnc-read '(1 2 3))))  
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
    (is (= (symbol "#t") (fnc-menor ())))
    (is (= (symbol "#t") (fnc-menor '(1))))
    (is (= (symbol "#t") (fnc-menor '(1 2))))
    (is (= (symbol "#t") (fnc-menor '(1 2 3))))
    (is (= (symbol "#t") (fnc-menor '(1 2 3 4))))
    (is (= (symbol "#f") (fnc-menor '(1 2 2 4))))
    (is (= (symbol "#f") (fnc-menor '(1 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '"<" 'A) (fnc-menor '(A 1 2 4))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '"<" 'A) (fnc-menor '(1 A 2 4))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '"<" 'A) (fnc-menor '(1 2 A 4))))    
  )
)

(deftest fnc-mayor-test
  (testing "prueba de la funcion: fnc-mayor"
    (is (= (symbol "#t") (fnc-mayor ())))
    (is (= (symbol "#t") (fnc-mayor '(1))))
    (is (= (symbol "#t") (fnc-mayor '(2 1))))
    (is (= (symbol "#t") (fnc-mayor '(3 2 1))))
    (is (= (symbol "#t") (fnc-mayor '(4 3 2 1))))
    (is (= (symbol "#f") (fnc-mayor '(4 2 2 1))))
    (is (= (symbol "#f") (fnc-mayor '(4 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '">" 'A) (fnc-mayor '(A 3 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">" 'A) (fnc-mayor '(3 A 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">" 'A) (fnc-mayor '(3 2 A 1))))    
  )
)

(deftest fnc-mayor-o-igual-test
  (testing "prueba de la funcion: fnc-mayor-o-igual"
    (is (= (symbol "#t") (fnc-mayor-o-igual ())))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(2 1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(3 2 1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(4 3 2 1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(4 2 2 1))))
    (is (= (symbol "#f") (fnc-mayor-o-igual '(4 2 1 4))))
    (is (= (generar-mensaje-error :wrong-type-arg1 '">=" 'A) (fnc-mayor-o-igual '(A 3 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">=" 'A) (fnc-mayor-o-igual '(3 A 2 1))))    
    (is (= (generar-mensaje-error :wrong-type-arg2 '">=" 'A) (fnc-mayor-o-igual '(3 2 A 1))))    
  )
)

(deftest evaluar-escalar-test
  (testing "prueba de la funcion: evaluar-escalar"
    (is (= '(32 (x 6 y 11 z "hola")) (evaluar-escalar 32 '(x 6 y 11 z "hola"))))
    (is (= '("chau" (x 6 y 11 z "hola")) (evaluar-escalar "chau" '(x 6 y 11 z "hola"))))
    (is (= '(11 (x 6 y 11 z "hola")) (evaluar-escalar 'y '(x 6 y 11 z "hola"))))
    (is (= '("hola" (x 6 y 11 z "hola")) (evaluar-escalar 'z '(x 6 y 11 z "hola"))))
    (is (= (list (generar-mensaje-error :unbound-variable 'n) '(x 6 y 11 z "hola")) (evaluar-escalar 'n '(x 6 y 11 z "hola"))))
  )
)

(deftest evaluar-define-test
  (testing "prueba de la funcion: evaluar-define"
    (is (= (list (symbol "#<unspecified>") '(x 2)) (evaluar-define '('define x 2) '(x 1))))
    (is (= (list (symbol "#<unspecified>") (list 'x 1 'f (list 'lambda (list 'x) (list '+ 'x 1)))) (evaluar-define (list 'define '(f x) '(+ x 1)) '(x 1))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'define '('define)) '(x 1)) (evaluar-define '('define) '(x 1))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'define '('define x)) '(x 1)) (evaluar-define '('define x) '(x 1))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'define '('define x 2 3)) '(x 1)) (evaluar-define '('define x 2 3) '(x 1))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'define '('define ())) '(x 1)) (evaluar-define '('define ()) '(x 1))))
    (is (= (list (generar-mensaje-error :bad-variable 'define '('define () 2)) '(x 1)) (evaluar-define '('define () 2) '(x 1))))
    (is (= (list (generar-mensaje-error :bad-variable 'define '('define 2 x)) '(x 1)) (evaluar-define '('define 2 x) '(x 1))))
  )
)

(deftest evaluar-if-test
  (testing "prueba de la funcion: evaluar-if"
    (is (= (list 2 (list 'n 7)) (evaluar-if '('if 1 2) '(n 7))))
    (is (= (list 7 (list 'n 7)) (evaluar-if '('if 1 n) '(n 7))))
    (is (= (list 7 (list 'n 7)) (evaluar-if '('if 1 n 8) '(n 7))))
    (is (= (list (symbol  "#<unspecified>") (list 'n 7 (symbol "#f") (symbol "#f"))) (evaluar-if (list 'if (symbol "#f") 'n) (list 'n 7 (symbol "#f") (symbol "#f")))))
    (is (= (list 8 (list 'n 7 (symbol "#f") (symbol "#f")) (evaluar-if (list 'if (symbol "#f") 'n 8) (list 'n 7 (symbol "#f") (symbol "#f"))))))
    (is (= (list (symbol "#<unspecified>") (list 'n 9 (symbol "#f") (symbol "#f"))) (evaluar-if (list 'if (symbol "#f") 'n '(set! n 9)) (list 'n 7 (symbol "#f") (symbol "#f")))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'if '('if)) '(n 7)) (evaluar-if '('if) '(n 7))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'if '('if 1)) '(n 7)) (evaluar-if '('if 1) '(n 7))))
  )
)

(deftest evaluar-or-test
  (testing "prueba de la funcion: evaluar-or"
    (is (= (list (symbol "#f") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) (evaluar-or (list 'or) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")))))
    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) (evaluar-or (list 'or (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")))))
    (is (= (list 7 (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) (evaluar-or (list 'or 7) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")))))
    (is (= (list 5 (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) (evaluar-or (list 'or (symbol "#f") 5) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")))))
    (is (= (list (symbol "#f") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) (evaluar-or (list 'or (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")))))
  )
)

(deftest evaluar-set!-test
  (testing "prueba de la funcion: evaluar-set!"
    (is (= (list (symbol "#<unspecified>") '(x 1)) (evaluar-set! '(set! x 1) '(x 0))))
    (is (= (list (generar-mensaje-error :unbound-variable 'x) '()) (evaluar-set! '(set! x 1) '())))
    (is (= (list (generar-mensaje-error :missing-or-extra 'set! '(set! x)) '(x 0)) (evaluar-set! '(set! x) '(x 0))))
    (is (= (list (generar-mensaje-error :missing-or-extra 'set! '(set! x 1 2)) '(x 0)) (evaluar-set! '(set! x 1 2) '(x 0))))
    (is (= (list (generar-mensaje-error :bad-variable 'set! 1) '(x 0)) (evaluar-set! '(set! 1 2) '(x 0))))
  )
)
package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;

/* Работа с этим классом, как и с FuzzyFunction, тоже будет вестись путем создания абстрактных классов
* Пример:
  rule1 = new Rule(){
        public Array<FuzzyFunction> apply(Array<Double> distances){
            ruleValue = Math.min( lowDistanceRule(distances[0]), midDistanceRule(distances[1]),
                        midDistanceRule(distances[2]) );

            leftFun = Rule.topBound(ruleValue, moveFuns[1]);
            rightFun = leftFun;

            return(leftFun, rightFun);
         }
  }

  */
public abstract class Rule {

    /*возвращает массив из 2 функций, первая для левой гуменицы, вторая для правой
    */
    public abstract Array<FuzzyFunction> apply(Array<Double> distances);

    /* Ограничивает функцию сверху, возвращает новую функцию */
    public static FuzzyFunction topBound(double bound, FuzzyFunction fuzzyFun){
        if (bound == 0)
            return zeroFunction;
        return new FuzzyFunction(){
            public double fun(double x){
                double value = fuzzyFun.fun(x);
                if (value > bound){
                    return bound;
                }
                else
                    return value;
            }
        };
    }

    static private FuzzyFunction zeroFunction = new FuzzyFunction(){
                                                    public double fun(double x){
                                                        return 0;
                                                    }
                                                };
}

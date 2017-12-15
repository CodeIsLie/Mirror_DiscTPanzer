package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;

//в этом классе будет происходить инициализация всех наших функций
public class FunctionSet {

    public static Array<FuzzyFunction> getSensorFuns(){
        Array<FuzzyFunction> funs = new Array<>();

        //контрольные значения дистанций
        final double d1 = 20;
        final double d2 = 40;
        final double d3 = 60;
        final double d4 = 80;
        //double d5 = 50;

        //близко
        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < d1){
                    return x/d1;
                }
                else if (x < d2)
                    return 1;
                else if (x < d3)
                    return 1 - (x - d2)/(d2 - d2);
                else
                    return 0;
            }
        });
        //средне
        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < d1)
                    return 0;
                else if (x < d2)
                    return (x - d1)/(d2 - d1);
                else if (x < d3)
                    return 1;
                else if (x < d4)
                    return 1 - (x - d3)/(d4-d3);
                else
                    return 0;
            }
        });
        //далеко
        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < d3)
                    return 0;
                else if (x < d4)
                    return (x - d3)/(d4 - d3);
                else return 1;
            }
        });

        return funs;
    }

    public static Array<FuzzyFunction> getTrackFuns(){
        Array<FuzzyFunction> funs = new Array<>();

        final double speed1 = 15;
        final double speed2 = 50;
        final double speed3 = 100;

        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                double k = x/speed1;
                if ( k > 1 )
                    return 0;
                else
                    return 1 - k;
            }
        });

        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < speed1)
                    return x/speed1;
                else if (x < speed2)
                    return 1 - (x - speed1)/(speed2 - speed1);
                else
                    return 0;
            }
        });

        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < speed1)
                    return 0;
                else if (x < speed2)
                    return (x - speed1)/(speed2 - speed1);
                else if (x < speed3)
                    return 1 - (x - speed2)/(speed3 - speed2);
                else
                    return 0;
            }
        });

        return funs;
    }
}

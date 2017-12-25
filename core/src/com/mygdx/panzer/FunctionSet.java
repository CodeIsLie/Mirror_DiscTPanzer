package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;

import java.util.Map;
import java.util.TreeMap;

//в этом классе будет происходить инициализация всех наших функций
public class FunctionSet {

    public static Map<String, FuzzyFunction> getSensorFuns(float maxRange){
        Map<String, FuzzyFunction> funs = new TreeMap<>();

        //контрольные значения дистанций
        final float d1 = 55;
        final float d2 = 95;

        //final double d3 = 60;
        //final double d4 = 80;
        //double d5 = 50;

        //близко
        funs.put("low", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < d1){
                    return 1;
                }
                else if (x < d2)
                    return 1 - (x - d1)/(d2 - d1);
                else
                    return 0;
            }
        });
        //средне
        funs.put("mid", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < d1)
                    return 0;
                else if (x < d2)
                    return (x - d1)/(d2 - d1);
                else
                    return 1;
            }
        });
        /*//далеко
        funs.add(new FuzzyFunction() {
            @Override
            public double fun(double x) {
                if (x < d3)
                    return 0;
                else if (x < d4)
                    return (x - d3)/(d4 - d3);
                else return 1;
            }
        });*/

        return funs;
    }

    public static int getMaxSpeed(){ return 300; }

    public static int getMinSpeed(){ return 0; }

    public static Map<String, FuzzyFunction> getTrackFuns(float maxSpeed){
        Map<String, FuzzyFunction> funs = new TreeMap<>();


        final float speed1 = 30;
        final float speed2 = 60;
        final float speed3 = 100;
        final float speed4 = 150;
        final float speed5 = 200;
        final float speed6 = 240;
        final float speed7 = getMaxSpeed();

        /*funs.put("reverseS", new FuzzyFunction() {
            @Override
            public float fun(float x) {

            }
        })*/

        funs.put("veryS", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                float k = x/speed1;
                if ( x < speed1 )
                    return 1 - k;
                else
                    return 0;
            }
        });

        // 1
        funs.put("slow", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < speed1)
                    return x/speed1;
                else if (x < speed2)
                    return 1;
                else if (x < speed3)
                    return 1 - (x - speed2)/(speed3 - speed2);
                else
                    return 0;
            }
        });

        // 2
        funs.put("mid", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < speed2)
                    return 0;
                else if (x < speed3)
                    return (x - speed2)/(speed3 - speed2);
                else if (x < speed4)
                    return 1;
                else
                    return 0;
            }
        });

        // 3
        funs.put("fast", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < speed3)
                    return 0;
                else if (x < speed4)
                    return (x - speed3)/(speed4 - speed3);
                else if (x < speed5)
                    return 1;
                else
                    return 0;
            }
        });

        // 4
        funs.put("veryF", new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < speed4)
                    return 0;
                else if (x < speed5)
                    return (x - speed4)/(speed5 - speed4);
                else if (x < speed6)
                    return 1;
                else
                    return 0;
            }
        });

        /*
        funs.add(new FuzzyFunction() {
            @Override
            public float fun(float x) {
                if (x < speed5)
                    return 0;
                else if (x < speed6)
                    return (x - speed5)/(speed6 - speed5);
                else if (x < speed7)
                    return 1;
                else
                    return 0;
            }
        });
        */

        return funs;
    }
}

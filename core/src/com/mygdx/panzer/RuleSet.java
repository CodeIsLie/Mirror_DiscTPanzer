package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class RuleSet {
    private Array<Rule> rules;

    public RuleSet(Array<Rule> rules){
        this.rules = rules;
    }

    // Массив значений - 2 значения, нагрузка на левую и правую функцию
    Array<Double> apply(Array<Sensor> sensors){
        Array<Double> distances = new Array<>();
        distances.add(sensors.get(0).getRange());
        distances.add(sensors.get(1).getRange());
        distances.add(sensors.get(2).getRange());

        Array<FuzzyFunction> leftTrackFuns = new Array<>();
        Array<FuzzyFunction> rightTrackFuns = new Array<>();
        for (Rule r: rules){
            Array<FuzzyFunction> ruleRes = r.apply(distances);
            leftTrackFuns.add(ruleRes.get(0));
            rightTrackFuns.add(ruleRes.get(1));
        }

        FuzzyFunction leftTrackSummaryFun = combine(leftTrackFuns);
        FuzzyFunction rightTrackSummaryFun = combine(rightTrackFuns);

        double leftPower = massCentre(leftTrackSummaryFun,0, 50, 100);
        double rightPower = massCentre(rightTrackSummaryFun, 0, 50, 100);

        Array<Double> powers = new Array<Double>();
        powers.add(leftPower);
        powers.add(rightPower);

        return powers;
    }

    private static FuzzyFunction combine(final Array<FuzzyFunction> funs){
        return new FuzzyFunction(){
            @Override
            public double fun(double x){
                double max = -1;
                for (FuzzyFunction f: funs){
                    if (f.fun(x) > max)
                        max = f.fun(x);
                }

                return max;
            }
        };
    }

    //превращает функции f(x) в x*f(x), нужно для вычисления интеграла
    private static FuzzyFunction modify(final FuzzyFunction f){
        return new FuzzyFunction() {
            @Override
            public double fun(double x) {
                return x * f.fun(x);
            }
        };
    }

    private static double massCentre(FuzzyFunction f, double start, double end, int n){
        double divider = calcIntegral(f, start, end, n);
        double denominator = calcIntegral(modify(f), start, end, n);

        return divider/denominator;
    }

    public static double calcIntegral(FuzzyFunction f, double start, double end, int n){
        double step = (end - start)/ n;
        double area = 0;
        double x = start + step/2;

        for (int i = 0; i < n; ++i){
            area += f.fun(x) * step;
            x += step;
        }

        //System.out.println("Integral Value is " + area);
        return area;
    }
}

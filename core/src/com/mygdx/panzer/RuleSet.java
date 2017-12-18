package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.lwjgl.Sys;

import static jdk.nashorn.internal.objects.NativeMath.min;

public class RuleSet {
    private Array<Rule> rules;
    Panzer panzer;
    //0 - лево, 1 - право
    int direction;

    public RuleSet(Array<Rule> rules, Panzer panzer){
        this.rules = rules;
        this.panzer = panzer;
        direction = 0;
    }

    // Массив значений - 2 значения, нагрузка на левую и правую функцию
    Array<Float> apply(Array<Sensor> sensors){
        Array<Float> distances = new Array<>();

        float dist1 = sensors.get(0).getSeeing() == Sensor.Seeing.NOTHING ? Float.POSITIVE_INFINITY : sensors.get(0).getRange();
        float dist2 = sensors.get(1).getSeeing() == Sensor.Seeing.NOTHING ? Float.POSITIVE_INFINITY : sensors.get(1).getRange();
        float dist3 = sensors.get(2).getSeeing() == Sensor.Seeing.NOTHING ? Float.POSITIVE_INFINITY : sensors.get(2).getRange();

        distances.add(dist1);
        distances.add(dist2);
        distances.add(dist3);

        updateDirection();

        Array<FuzzyFunction> leftTrackFuns = new Array<>();
        Array<FuzzyFunction> rightTrackFuns = new Array<>();
        for (Rule r: rules){
            Array<FuzzyFunction> ruleRes = r.apply(distances, direction);
            leftTrackFuns.add(ruleRes.get(0));
            rightTrackFuns.add(ruleRes.get(1));
        }

        FuzzyFunction leftTrackSummaryFun = combine(leftTrackFuns);
        FuzzyFunction rightTrackSummaryFun = combine(rightTrackFuns);

        float leftPower = massCentre(leftTrackSummaryFun,0, 50, 100);
        float rightPower = massCentre(rightTrackSummaryFun, 0, 50, 100);

        Array<Float> powers = new Array<Float>();
        powers.add(leftPower);
        powers.add(rightPower);

        return powers;
    }

    private void updateDirection(){
        //в градусах
        float goalX = Settings.getFinishPos().x;
        float goalY = Settings.getFinishPos().y;

        float normY = goalY/ (goalX - panzer.getPosition().x);
        double cos = 1/(Math.sqrt(1 + normY*normY));
        float angle = Math.round(Math.toDegrees(Math.acos(cos))) ;

        System.out.println("current angle is " + (angle - panzer.getAngle()) );
        System.out.println("tank angle is " + ( panzer.getAngle()) );
        System.out.println("angle is " + (angle) );

        if (panzer.getAngle() - angle < 180){
            direction = 1;
        }
        else
            direction = 0;
    }

    private static FuzzyFunction combine(final Array<FuzzyFunction> funs){
        return new FuzzyFunction(){
            @Override
            public float fun(float x){
                float max = -1;
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
            public float fun(float x) {
                return x * f.fun(x);
            }
        };
    }

    private static float massCentre(FuzzyFunction f, float start, float end, int n){
        float divider = calcIntegral(modify(f), start, end, n);
        float denominator = calcIntegral(f, start, end, n);
        if (denominator == 0)
            return 0;
        return divider/denominator;
    }

    private static float calcIntegral(FuzzyFunction f, float start, float end, int n){
        float step = (end - start)/ n;
        float area = 0;
        float x = start + step/2;

        for (int i = 0; i < n; ++i){
            area += f.fun(x) * step;
            x += step;
        }

        //System.out.println("Integral Value is " + area);
        return area;
    }

    //здесь будет находиться инициализация всех правил
    public static Array<Rule> getRules(){
        Array<Rule> rules = new Array<>();
        final Array<FuzzyFunction> trackFuns = FunctionSet.getTrackFuns();
        final Array<FuzzyFunction> sensorFuns = FunctionSet.getSensorFuns();

        //1) 1,2,3 = mid -> td = 1, -td = 2
        rules.add( new Rule() {
            @Override
            public Array<FuzzyFunction> apply(Array<Float> distances, int direction) {
                float v1 = sensorFuns.get(1).fun(distances.get(0));
                float v2 = sensorFuns.get(1).fun(distances.get(1));
                float v3 = sensorFuns.get(1).fun(distances.get(2));

                float ruleValue = Math.min(v1, Math.min(v2, v3));
                FuzzyFunction first = topBound(ruleValue, trackFuns.get(1));
                FuzzyFunction second = topBound(ruleValue, trackFuns.get(2));
                Array<FuzzyFunction> powers = new Array<>();

                if (direction == 0)
                {
                    powers.add(first);
                    powers.add(second);
                }
                else{
                    powers.add(second);
                    powers.add(first);
                }

                return powers;
            }
        } );
        //2) 1-low, 2,3 - mid, td==L  -> L=1, R=1
        //                               else L = 2, R = 1
        rules.add( new Rule(){
            @Override
            public Array<FuzzyFunction> apply(Array<Float> distances, int direction) {
                float v1 = sensorFuns.get(0).fun(distances.get(0));
                float v2 = sensorFuns.get(1).fun(distances.get(1));
                float v3 = sensorFuns.get(1).fun(distances.get(2));

                float ruleValue = Math.min(v1, Math.min(v2, v3));

                Array<FuzzyFunction> powers = new Array<>();

                if (direction == 0)
                {
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                }
                else{
                    powers.add(topBound(ruleValue, trackFuns.get(2)));
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                }

                return powers;
            }
        });
        //3) 1- mid, 2- low, 3 - mid -> TD = 0, -TD = 1
        rules.add( new Rule() {
            @Override
            public Array<FuzzyFunction> apply(Array<Float> distances, int direction) {
                float v1 = sensorFuns.get(1).fun(distances.get(0));
                float v2 = sensorFuns.get(0).fun(distances.get(1));
                float v3 = sensorFuns.get(1).fun(distances.get(2));

                float ruleValue = Math.min(v1, Math.min(v2, v3));

                Array<FuzzyFunction> powers = new Array<>();

                if (direction == 0)
                {
                    powers.add(topBound(ruleValue, trackFuns.get(0)));
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                }
                else{
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                    powers.add(topBound(ruleValue, trackFuns.get(0)));
                }

                return powers;
            }
        } );

        //4) 1 - low, 2 - low, 3 - mid -> L= 1, R= 0
        rules.add( new Rule() {
            @Override
            public Array<FuzzyFunction> apply(Array<Float> distances, int direction) {
                float v1 = sensorFuns.get(1).fun(distances.get(0));
                float v2 = sensorFuns.get(0).fun(distances.get(1));
                float v3 = sensorFuns.get(1).fun(distances.get(2));

                float ruleValue = Math.min(v1, Math.min(v2, v3));

                Array<FuzzyFunction> powers = new Array<>();

                powers.add(topBound(ruleValue, trackFuns.get(1)));
                powers.add(topBound(ruleValue, trackFuns.get(0)));

                return powers;
            }
        } );

        //5) 1,2,3 - low, TD = 0, -TD = 1
        rules.add( new Rule() {
            @Override
            public Array<FuzzyFunction> apply(Array<Float> distances, int direction) {
                float v1 = sensorFuns.get(0).fun(distances.get(0));
                float v2 = sensorFuns.get(0).fun(distances.get(1));
                float v3 = sensorFuns.get(0).fun(distances.get(2));

                float ruleValue = Math.min(v1, Math.min(v2, v3));

                Array<FuzzyFunction> powers = new Array<>();
                if (direction == 0){
                    powers.add(topBound(ruleValue, trackFuns.get(0)));
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                }
                else{
                    powers.add(topBound(ruleValue, trackFuns.get(1)));
                    powers.add(topBound(ruleValue, trackFuns.get(0)));
                }

                return powers;
            }
        } );

        return rules;
    }
}

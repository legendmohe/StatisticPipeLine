package pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数动作
 *
 * Created by hexinyu on 2019/1/22.
 */
public class CounterAction extends BaseStatisticAction {

    private AtomicInteger mInitValue;

    private List<Integer> mOpList = new ArrayList<Integer>();

    public static CounterAction fromValue(int initValue) {
        return new CounterAction(initValue);
    }

    public static CounterAction fromZero() {
        return fromValue(0);
    }

    public static IStatisticAction increase(String counter) {
        return new Increaser(counter);
    }

    public static IStatisticAction decrease(String counter) {
        return new Decreaser(counter);
    }

    //////////////////////////////////////////////////////////////////////

    private CounterAction(int initValue) {
        mInitValue = new AtomicInteger(initValue);
    }

    public synchronized CounterAction increase() {
        mOpList.add(1);
        return this;
    }

    public synchronized CounterAction decrease() {
        mOpList.add(-1);
        return this;
    }

    public int getValue() {
        return mInitValue.intValue();
    }

    @Override
    public void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
        int intValue = mInitValue.intValue();
        for (IStatisticAction action : pipeLine.getActions()) {
            if (action instanceof CounterOp
                    && action instanceof CounterAction
                    && ((CounterOp) action).getCountName().equals(getName())) {
                intValue += ((CounterAction) action).getValue();
            }
        }
        for (Integer op : mOpList) {
            intValue += op;
        }
        result.put(getName(), intValue);
    }

    ///////////////////////////////////private///////////////////////////////////

    private interface CounterOp {
        String getCountName();
    }

    private static class Increaser extends CounterAction implements CounterOp{

        private String mCounter;

        private Increaser(String counter) {
            super(1);
            mCounter = counter;
        }

        @Override
        public String getCountName() {
            return mCounter;
        }
    }

    private static class Decreaser extends CounterAction implements CounterOp{

        private String mCounter;

        private Decreaser(String counter) {
            super(-1);
            mCounter = counter;
        }

        @Override
        public String getCountName() {
            return mCounter;
        }
    }
}

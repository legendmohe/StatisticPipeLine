package pipeline;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class CounterAction extends BaseStatisticAction {

    private AtomicInteger mValue;

    public static CounterAction fromValue(int value) {
        return new CounterAction(value);
    }

    public static CounterAction zero() {
        return fromValue(0);
    }

    public static IStatisticAction increase(String counter) {
        return new Increaser(counter);
    }

    public static IStatisticAction decrease(String counter) {
        return new Decreaser(counter);
    }

    private CounterAction(int value) {
        mValue = new AtomicInteger(value);
    }

    public CounterAction increase() {
        mValue.incrementAndGet();
        return this;
    }

    public CounterAction decrease() {
        mValue.decrementAndGet();
        return this;
    }

    public int getValue() {
        return mValue.intValue();
    }

    @Override
    public void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
        for (IStatisticAction action : pipeLine.getActions()) {
            if (action instanceof CounterOp
                    && action instanceof CounterAction
                    && ((CounterOp) action).getCountName().equals(getName())) {
                mValue.set(mValue.intValue() + ((CounterAction) action).getValue());
            }
        }
        result.put(getName(), mValue.intValue());
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

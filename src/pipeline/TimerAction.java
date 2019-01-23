package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public interface TimerAction {

    ///////////////////////////////////start///////////////////////////////////

    class Start extends BaseStatisticAction {

        private long mStartTs;

        public static Start fromCurrentTimestamp() {
            return fromTimestamp(System.currentTimeMillis());
        }

        public static Start fromTimestamp(long ts) {
            return new Start(ts);
        }


        private Start(long startTs) {
            mStartTs = startTs;
        }

        public long getStartTs() {
            return mStartTs;
        }

        @Override
        public void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
        }
    }

    ///////////////////////////////////end///////////////////////////////////

    class End extends BaseStatisticAction {

        private String mStartActionName;
        private final long mCurTs;

        public static End fromStart(String startActionName) {
            return new End(startActionName);
        }

        private End(String startActionName) {
            mStartActionName = startActionName;
            mCurTs = System.currentTimeMillis();
        }

        @Override
        public void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
            IStatisticAction action = pipeLine.get(mStartActionName);
            if (action instanceof Start) {
                long startTs = ((Start) action).getStartTs();
                result.put(getName(), mCurTs - startTs);
            }
        }
    }
}

package pipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        public void onCollect(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
        }
    }

    ///////////////////////////////////end///////////////////////////////////

    class End extends BaseStatisticAction {

        private String mStartActionName;
        private long mPeriod;

        public static End fromStart(String startActionName) {
            return new End(startActionName);
        }

        private End(String startActionName) {
            mStartActionName = startActionName;
        }

        @Override
        public boolean onPut(StatisticPipeLine pipeLine) {
            IStatisticAction action = pipeLine.get(mStartActionName);
            if (action instanceof Start) {
                long startTs = ((Start) action).getStartTs();
                mPeriod = System.currentTimeMillis() - startTs;
            }
            return super.onPut(pipeLine);
        }

        @Override
        public void onCollect(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
            result.put(getName(), mPeriod);
        }
    }

    ///////////////////////////////////avg///////////////////////////////////

    class Avg extends BaseStatisticAction {

        private String mStartActionName;

        private final int mSessionId;

        private long mPeriod;

        private String mGroupName;

        private static final AtomicInteger sId = new AtomicInteger();

        public static IStatisticAction collect(String startActionName) {
            return new Avg(startActionName);
        }

        private Avg(String startActionName) {
            mStartActionName = startActionName;
            mSessionId = sId.incrementAndGet();
        }

        public long getPeriod() {
            return mPeriod;
        }

        public String getGroupName() {
            return mGroupName;
        }

        @Override
        public void setName(String name) {
            mGroupName = name;
            super.setName(name + "_" + mSessionId);
        }

        @Override
        public boolean onPut(StatisticPipeLine pipeLine) {
            IStatisticAction startAction = pipeLine.get(mStartActionName);
            if (!(startAction instanceof Start)) {
                throw new NullPointerException("start action must be instance of TimerAction.Start:" + mStartActionName);
            }

            mPeriod = getCurrentTs() - ((Start) startAction).getStartTs();
            return true;
        }

        @Override
        public void onCollect(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
            if (result.containsKey(mGroupName)) {
                // 不要每一个都算一遍
                return;
            }
            long total = 0;
            long count = 0;
            List<IStatisticAction> actions = pipeLine.getActions();
            for (IStatisticAction action : actions) {
                if (action instanceof Avg && action.getName().startsWith(mGroupName)) {
                    total += ((Avg) action).getPeriod();
                    count++;
                }
            }
            result.put(mGroupName, total / count);
        }

        private long getCurrentTs() {
            return System.currentTimeMillis();
        }
    }
}

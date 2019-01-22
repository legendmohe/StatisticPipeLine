package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class StartTsAction extends BaseStatisticAction {

    private long mStartTs;

    public static StartTsAction fromCurrentTimestamp() {
        return fromTimestamp(System.currentTimeMillis());
    }
    public static StartTsAction fromTimestamp(long ts) {
        return new StartTsAction(ts);
    }


    private StartTsAction(long startTs) {
        mStartTs = startTs;
    }

    public long getStartTs() {
        return mStartTs;
    }

    @Override
    public void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> result) {
        // 默认不放到结果里面
//        result.put(getName(), mStartTs);
    }
}

package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class EndTsAction extends BaseStatisticAction {

    private String mStartActionName;
    private final long mCurTs;

    public static EndTsAction fromStartAction(String startActionName) {
        return new EndTsAction(startActionName);
    }

    private EndTsAction(String startActionName) {
        mStartActionName = startActionName;
        mCurTs = System.currentTimeMillis();
    }

    @Override
    public void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> result) {
        IStatisticAction action = pipeLine.get(mStartActionName);
        if (action instanceof StartTsAction) {
            long startTs = ((StartTsAction) action).getStartTs();
            result.put(getName(), mCurTs - startTs);
        }
    }
}

package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public abstract class BaseStatisticAction implements IStatisticAction {

    private String mName = "";

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public boolean onPut(StatisticPipeLine pipeLine) {
        return true;
    }

    @Override
    public void onReset(StatisticPipeLine pipeLine) {

    }

    @Override
    public boolean onReplace(StatisticPipeLine pipeLine, IStatisticAction newAction) {
        return true;
    }

    @Override
    public boolean onPostCalculate(StatisticPipeLine pipeLine, Map<String, Object> context) {
        return true;
    }

    @Override
    public IStatisticAction copy() {
        return null;
    }
}

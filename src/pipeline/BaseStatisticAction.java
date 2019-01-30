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
    public boolean matchName(String name) {
        return getName().equals(name);
    }

    @Override
    public boolean equalTo(IStatisticAction action) {
        return action == this;
    }

    @Override
    public boolean onPut(IStatisticPipeLine pipeLine) {
        return true;
    }

    @Override
    public void onReset(IStatisticPipeLine pipeLine) {

    }

    @Override
    public boolean onReplace(IStatisticPipeLine pipeLine, IStatisticAction newAction) {
        return true;
    }

    @Override
    public boolean onPostCollect(IStatisticPipeLine pipeLine, Map<String, Object> context) {
        return true;
    }

    @Override
    public IStatisticAction copy() {
        return null;
    }
}

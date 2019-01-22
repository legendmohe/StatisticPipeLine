package pipeline;

/**
 * Created by hexinyu on 2019/1/22.
 */
public abstract class BaseStatisticAction implements IStatisticAction {

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public IStatisticAction copy() {
        return null;
    }
}

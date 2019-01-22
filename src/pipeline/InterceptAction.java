package pipeline;

/**
 * Created by hexinyu on 2019/1/22.
 */
public abstract class InterceptAction extends BaseStatisticAction {

    public InterceptAction() {
        setName("intercept_" + System.currentTimeMillis());
    }
}

package pipeline;

import java.util.Map;

/**
 * 拦截器，用于最后对结果进行处理。可以即用即弃。
 *
 * Created by hexinyu on 2019/1/22.
 */
public abstract class InterceptAction extends BaseStatisticAction {

    private boolean mRemoveAfterCollect;

    public InterceptAction(boolean removeAfterCollect) {
        mRemoveAfterCollect = removeAfterCollect;
        setName("intercept_" + System.currentTimeMillis());
    }

    public InterceptAction() {
        this(false);
    }

    @Override
    public boolean onPostCollect(IStatisticPipeLine pipeLine, Map<String, Object> context) {
        return !mRemoveAfterCollect;
    }
}

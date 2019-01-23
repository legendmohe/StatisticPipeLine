package pipeline;

import java.util.Map;

/**
 * 枚举动作
 *
 * Created by hexinyu on 2019/1/22.
 */
public class EnumAction<T> extends BaseStatisticAction {

    private T mValue;
    private boolean mReplaceLast;

    public static <T> EnumAction<T> fromValue(T value) {
        return new EnumAction<T>(value, false);
    }

    public static <T> EnumAction<T> fromValue(T value, boolean replaceLast) {
        return new EnumAction<T>(value, replaceLast);
    }

    private EnumAction(T value, boolean replaceLast) {
        mValue = value;
        mReplaceLast = replaceLast;
    }

    public T getValue() {
        return mValue;
    }

    public boolean isReplaceLast() {
        return mReplaceLast;
    }

    @Override
    public boolean onReplace(StatisticPipeLine pipeLine, IStatisticAction newAction) {
        return !(newAction instanceof EnumAction) || ((EnumAction) newAction).isReplaceLast();
    }

    @Override
    public void onCollect(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
        result.put(getName(), mValue);
    }
}

package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class EnumAction<T> extends BaseStatisticAction {

    private T mValue;

    public static <T> EnumAction<T> fromValue(T value) {
        return new EnumAction<T>(value);
    }

    private EnumAction(T value) {
        mValue = value;
    }

    @Override
    public void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> result) {
        result.put(getName(), mValue);
    }
}

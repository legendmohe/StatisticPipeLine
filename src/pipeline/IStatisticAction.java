package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public interface IStatisticAction {

    String getName();

    void setName(String name);

    IStatisticAction copy();

    /**
     * 计算结果。注意，所有的状态只在这里进行运算。action要满足多次assemble结果是一致的
     *
     * @param pipeLine
     * @param context
     * @param result
     */
    void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result);
}

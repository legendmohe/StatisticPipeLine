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
     * 注入相同name的action时，决定是否进行替换
     *
     * @param pipeLine
     * @param oldAction
     * @return true为替换，false为不替换
     */
    boolean onReplace(StatisticPipeLine pipeLine, IStatisticAction newAction);

    /**
     * 计算结果。注意，所有的状态只在这里进行运算。action要满足多次assemble结果是一致的
     *
     * @param pipeLine
     * @param context
     * @param result
     */
    void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result);
}

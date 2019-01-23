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
     * 被放进pipeline时调用
     *
     * @param pipeLine
     * @return true为可以放进去，false为不放进去
     */
    boolean onPut(StatisticPipeLine pipeLine);

    /**
     * pipeLine调用reset，用于action清理状态
     *
     * @param pipeLine
     */
    void onReset(StatisticPipeLine pipeLine);

    /**
     * 注入相同name的action时，决定是否进行替换
     *
     * @param pipeLine
     * @param newAction
     * @return true为替换，false为不替换
     */
    boolean onReplace(StatisticPipeLine pipeLine, IStatisticAction newAction);

    /**
     * 计算结果。注意，action要满足多次assemble结果是一致的
     *
     * @param pipeLine
     * @param context
     * @param result
     */
    void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result);

    /**
     * 在进行计算结果后回调
     *
     * @param pipeLine
     * @param context
     * @return false为从action队列中移除，true为不移除
     */
    boolean onPostCalculate(StatisticPipeLine pipeLine, Map<String, Object> context);
}

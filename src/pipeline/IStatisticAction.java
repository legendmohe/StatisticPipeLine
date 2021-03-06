package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public interface IStatisticAction {

    String getName();

    void setName(String name);

    /**
     * 是否符合参数name
     * @param name
     * @return
     */
    boolean matchName(String name);

    /**
     * 是否是同一个action
     *
     * @param name
     * @return
     */
    boolean equalTo(IStatisticAction action);

    /**
     * 返回一个copy对象（deep copy）
     * @return
     */
    IStatisticAction copy();

    /**
     * 被放进pipeline时调用
     *
     * @param pipeLine
     * @return true为可以放进去，false为不放进去
     */
    boolean onPut(IStatisticPipeLine pipeLine);

    /**
     * pipeLine调用reset，用于action清理状态
     *
     * @param pipeLine
     */
    void onReset(IStatisticPipeLine pipeLine);

    /**
     * 注入相同name的action时，决定是否进行替换
     *
     * @param pipeLine
     * @param newAction
     * @return true为替换，false为不替换
     */
    boolean onReplace(IStatisticPipeLine pipeLine, IStatisticAction newAction);

    /**
     * 计算结果。注意，action要满足多次assemble结果是一致的
     *
     * @param pipeLine
     * @param context
     * @param result
     */
    void onCollect(IStatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result);

    /**
     * 在进行计算结果后回调
     *
     * @param pipeLine
     * @param context
     * @return false为从action队列中移除，true为不移除
     */
    boolean onPostCollect(IStatisticPipeLine pipeLine, Map<String, Object> context);
}

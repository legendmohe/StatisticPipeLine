package pipeline;

import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public interface IStatisticAction {

    String getName();

    void setName(String name);

    IStatisticAction copy();

    void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result);
}

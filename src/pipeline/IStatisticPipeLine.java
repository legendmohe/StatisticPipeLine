package pipeline;

import java.util.List;
import java.util.Map;

/**
 * Created by hexinyu on 2019/1/24.
 */
public interface IStatisticPipeLine {
    String getTag();

    IStatisticPipeLine put(IStatisticAction action, String name);

    IStatisticPipeLine put(IStatisticAction action);

    IStatisticAction get(String name);

    void remove(String name);

    void remove(String name, Comparable<IStatisticAction> comparator);

    IStatisticAction getClone(String name);

    List<IStatisticAction> getActions();

    void reset();

    void clear(String... excludes);

    Map<String, Object> collectAll();

    Map<String, Object> collect(String... names);
}

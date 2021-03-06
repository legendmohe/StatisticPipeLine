package pipeline;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 清理制定name的action
 * <p>
 * Created by hexinyu on 2019/1/23.
 */
public class RemoveAction extends BaseStatisticAction {

    private Set<String> mNames = new HashSet<>();

    public RemoveAction(String[] names) {
        if (names != null) {
            for (String name : names) {
                mNames.add(name);
            }
        }
    }

    public static RemoveAction forName(String... names) {
        return new RemoveAction(names);
    }

    @Override
    public boolean onPut(IStatisticPipeLine pipeLine) {
        if (mNames.size() > 0) {
            for (final String name : mNames) {
                pipeLine.remove(name, new Comparable<IStatisticAction>() {
                    @Override
                    public int compareTo(IStatisticAction o) {
                        return o.matchName(name) ? 0 : -1;
                    }
                });
            }
        }
        return false;
    }

    @Override
    public void onCollect(IStatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {

    }
}

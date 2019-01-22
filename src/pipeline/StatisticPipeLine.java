package pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class StatisticPipeLine {

    ///////////////////////////////////static///////////////////////////////////

    public static StatisticPipeLine create(String tag) {
        return new StatisticPipeLine(tag);
    }

    ///////////////////////////////////public///////////////////////////////////

    private List<IStatisticAction> mActions = new ArrayList<IStatisticAction>();

    private String mTag;

    public StatisticPipeLine(String tag) {
        mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public synchronized StatisticPipeLine put(IStatisticAction action, String actionTag) {
        action.setName(actionTag);
        int i = find(actionTag);
        if (i >= 0) {
            mActions.remove(i);
            mActions.add(i, action);
        } else {
            mActions.add(action);
        }
        return this;
    }

    public synchronized StatisticPipeLine put(IStatisticAction action) {
        mActions.add(action);
        return this;
    }

    public synchronized IStatisticAction get(String actionName) {
        if (actionName == null || actionName.length() == 0) {
            return null;
        }
        for (IStatisticAction action : mActions) {
            if (action.getName().equals(actionName)) {
                return action;
            }
        }
        return null;
    }

    public synchronized IStatisticAction getClone(String actionName) {
        IStatisticAction iStatisticAction = get(actionName);
        return iStatisticAction != null ? iStatisticAction.copy() : null;
    }

    public synchronized List<IStatisticAction> getActions() {
        return new ArrayList<IStatisticAction>(mActions);
    }

    public synchronized void clear() {
        mActions.clear();
    }

    public synchronized Map<String, Object> assemble() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        for (IStatisticAction action : mActions) {
            if (action.getName() != null && action.getName().length() > 0) {
                action.onAssemble(this, result);
            }
        }
        return result;
    }

    ///////////////////////////////////private///////////////////////////////////

    private int find(String actionName) {
        for (int i = 0; i < mActions.size(); i++) {
            IStatisticAction action = mActions.get(i);
            if (action.getName() != null && action.getName().equals(actionName))
                return i;
        }
        return -1;
    }
}

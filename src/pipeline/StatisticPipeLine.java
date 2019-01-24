package pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by hexinyu on 2019/1/22.
 */
public class StatisticPipeLine implements IStatisticPipeLine {

    ///////////////////////////////////static///////////////////////////////////

    public static IStatisticPipeLine create(String tag) {
        return new StatisticPipeLine(tag);
    }

    ///////////////////////////////////public///////////////////////////////////

    /*
    注意，选择容器时要保证能够按照插入时的顺序进行遍历
     */
    private List<IStatisticAction> mActions = new ArrayList<IStatisticAction>();

    private String mTag;

    public StatisticPipeLine(String tag) {
        mTag = tag;
    }

    @Override
    public String getTag() {
        return mTag;
    }

    @Override
    public synchronized IStatisticPipeLine put(IStatisticAction action, String name) {
        if (name == null) {
            put(action);
        } else {
            action.setName(name);
            int i = find(name);
            if (i >= 0) {
                IStatisticAction oldAction = mActions.get(i);
                if (oldAction.onReplace(this, action)) {
                    mActions.remove(i);
                    putActionInternal(i, action);
                }
            } else {
                putActionInternal(action);
            }
        }
        return this;
    }

    @Override
    public synchronized IStatisticPipeLine put(IStatisticAction action) {
        putActionInternal(action);
        return this;
    }

    @Override
    public synchronized IStatisticAction get(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        for (IStatisticAction action : mActions) {
            if (action.getName().equals(name)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public synchronized void remove(String name) {
        ListIterator<IStatisticAction> iter = mActions.listIterator();
        while (iter.hasNext()) {
            if (iter.next().getName().equals(name)) {
                iter.remove();
            }
        }
    }

    @Override
    public synchronized void remove(String name, Comparable<IStatisticAction> comparator) {
        ListIterator<IStatisticAction> iter = mActions.listIterator();
        while (iter.hasNext()) {
            if (comparator.compareTo(iter.next()) == 0) {
                iter.remove();
            }
        }
    }

    @Override
    public synchronized IStatisticAction getClone(String name) {
        IStatisticAction iStatisticAction = get(name);
        return iStatisticAction != null ? iStatisticAction.copy() : null;
    }

    @Override
    public synchronized List<IStatisticAction> getActions() {
        return new ArrayList<>(mActions);
    }

    @Override
    public synchronized void reset() {
        for (IStatisticAction action : mActions) {
            action.onReset(this);
        }
        mActions.clear();
    }

    @Override
    public synchronized void clear(String... excludes) {
        Set<String> excludeSet = new HashSet<>(Arrays.asList(excludes));
        List<IStatisticAction> startUp = new ArrayList<>();
        for (IStatisticAction action : mActions) {
            action.onReset(this);
            if (excludeSet.contains(action.getName())) {
                startUp.add(action);
            }
        }
        mActions.clear();
        mActions.addAll(startUp);
    }

    @Override
    public synchronized Map<String, Object> collect() {
        // 存放结果
        HashMap<String, Object> result = new HashMap<String, Object>();
        // 用于各个action之间共享数据
        HashMap<String, Object> context = new HashMap<String, Object>();
        for (IStatisticAction action : mActions) {
            if (action.getName() != null && action.getName().length() > 0) {
                action.onCollect(this, context, result);
            }
        }
        ListIterator<IStatisticAction> iter = mActions.listIterator();
        while (iter.hasNext()) {
            if (!iter.next().onPostCollect(this, context)) {
                iter.remove();
            }
        }
        return result;
    }

    ///////////////////////////////////private///////////////////////////////////

    private int find(String actionName) {
        for (int i = 0; i < mActions.size(); i++) {
            IStatisticAction action = mActions.get(i);
            if (action.getName() != null && action.getName() != null && action.getName().equals(actionName))
                return i;
        }
        return -1;
    }

    private void putActionInternal(IStatisticAction action) {
        if (action.onPut(this)) {
            mActions.add(action);
        }
    }

    private void putActionInternal(int i, IStatisticAction action) {
        if (action.onPut(this)) {
            mActions.add(i, action);
        }
    }
}

package pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
            if (action.matchName(name)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public synchronized void remove(String name) {
        ListIterator<IStatisticAction> iter = mActions.listIterator();
        while (iter.hasNext()) {
            if (iter.next().matchName(name)) {
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
        List<IStatisticAction> startUp = new ArrayList<>();
        for (IStatisticAction action : mActions) {
            action.onReset(this);
            for (String exclude : excludes) {
                if (action.matchName(exclude)) {
                    startUp.add(action);
                }
            }
        }
        mActions.clear();
        mActions.addAll(startUp);
    }

    @Override
    public final synchronized Map<String, Object> collectAll() {
        return collect();
    }

    @Override
    public final Map<String, Object> collect(String... names) {
        // 存放结果
        HashMap<String, Object> result = new HashMap<String, Object>();
        // 用于各个action之间共享数据
        HashMap<String, Object> context = new HashMap<String, Object>();
        for (IStatisticAction action : mActions) {
            if (action.getName() != null && action.getName().length() > 0) {
                if (names != null && names.length > 0) { // 只处理names里面的
                    for (String name : names) {
                        if (action.matchName(name)) {
                            action.onCollect(this, context, result);
                            // fixme - 这里需要break吗？
                        }
                    }
                } else {
                    action.onCollect(this, context, result);
                }
            }
        }
        ListIterator<IStatisticAction> iter = mActions.listIterator();
        while (iter.hasNext()) {
            IStatisticAction next = iter.next();
            if (!next.onPostCollect(this, context)) {
                iter.remove();
            }
        }
        onCollected(result);
        return result;
    }

    /*
    供子类实现
     */
    protected void onCollected(Map<String, Object> result) {

    }

    ///////////////////////////////////private///////////////////////////////////

    private int find(String actionName) {
        for (int i = 0; i < mActions.size(); i++) {
            IStatisticAction action = mActions.get(i);
            // 这里不用matchName，因为要精确检查
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

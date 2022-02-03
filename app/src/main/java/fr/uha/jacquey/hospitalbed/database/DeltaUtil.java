package fr.uha.jacquey.hospitalbed.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class DeltaUtil<X, Y> {

    private List<Y> toAdd;
    private List<Y> toRemove;
    private List<Y> toUpdate;

    private Map<Long, X> convert (List<X> list) {
        Map<Long, X> ids = new TreeMap<>();
        for (X x : list) {
            ids.put(getId(x), x);
        }
        return ids;
    }

    public void calculate (List<X> left, List<X> right) {
        Map<Long, X> initial = convert(left);
        Map<Long, X> next = convert(right);
        toAdd = new ArrayList<>();
        for (Long id : next.keySet()) {
            if (!initial.containsKey(id)) toAdd.add(createFor(next.get(id)));
        }
        toRemove = new ArrayList<>();
        for (Long id : initial.keySet()) {
            if (!next.containsKey(id)) toRemove.add(createFor(initial.get(id)));
        }
        toUpdate = new ArrayList<>();
        for (Long id : next.keySet()) {
            if (initial.containsKey(id)) {
                if (! same(initial.get(id), next.get(id))) {
                    toUpdate.add(createFor(next.get(id)));
                }
            }
        }
    }

    protected abstract long getId(X x);

    protected abstract boolean same(X initial, X now);

    protected abstract Y createFor(X object);

    public List<Y> getToAdd() {
        return toAdd;
    }

    public List<Y> getToRemove() {
        return toRemove;
    }

    public List<Y> getToUpdate() {
        return toUpdate;
    }

}

package site.kason.fhc;

import java.util.LinkedList;
import java.util.List;

public class NamedValueList {

    private List<NamedValue> list = new LinkedList<>();

    public void add(NamedValue namedValue) {
        list.add(namedValue);
    }

    public void remove(String name) {
        LinkedList<NamedValue> ls = new LinkedList();
        for (NamedValue v : list) {
            if (!name.equals(v.getName())) {
                ls.add(v);
            }
        }
        list = ls;
    }

    public NamedValue[] getAllNamedValues() {
        return list.toArray(new NamedValue[0]);
    }

}

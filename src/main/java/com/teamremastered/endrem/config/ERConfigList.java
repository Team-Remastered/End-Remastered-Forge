package com.teamremastered.endrem.config;

import com.google.common.collect.Lists;
import com.teamremastered.endrem.EndRemastered;

import java.util.ArrayList;

public class ERConfigList extends ERConfigGenericEntry<String> {
    public ERConfigList(String id, String comment, ArrayList<String> default_value) {
        super(id, comment, getStringFromList(default_value));
    }

    public ERConfigList(String id, String comment, String default_value) {
        super(id, comment, default_value);
    }

    public ArrayList<String> getList() {
        return getListFromString(this.get());
    }

    public void set(ArrayList<String> value) {
        this.set(getStringFromList(value));
    }

    private static String getStringFromList(ArrayList<String> lst) {
        return "[" + String.join(", ", lst) + "]";
    }

    private static ArrayList<String> getListFromString(String str) {
        if (str.length() < 2 || str.charAt(0) != '[' || str.charAt(str.length() - 1) != ']') {
            EndRemastered.LOGGER.error(String.format("Invalid value for list: %s", str));
        }
        return Lists.newArrayList(str.substring(1, str.length() - 1).split(",\\s*"));
    }
}


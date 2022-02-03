package fr.uha.jacquey.hospitalbed.helper;

import java.util.Date;

public class CompareUtil {

    static public boolean compare (String lhs, String rhs) {
        if (lhs == null && rhs == null) return true;
        if (lhs == null && rhs != null) return false;
        if (lhs != null && rhs == null) return false;
        return lhs.equals(rhs);
    }

    static public boolean compare (Enum lhs, Enum rhs) {
        if (lhs == null && rhs == null) return true;
        if (lhs == null && rhs != null) return false;
        if (lhs != null && rhs == null) return false;
        return lhs.ordinal() == rhs.ordinal();
    }

    static public boolean compare (Date lhs, Date rhs) {
        if (lhs == null && rhs == null) return true;
        if (lhs == null && rhs != null) return false;
        if (lhs != null && rhs == null) return false;
        return lhs.getTime() == rhs.getTime();
    }

    static public boolean compare (int lhs, int rhs) {
        return lhs == rhs;
    }
    static public boolean compare (float lhs, float rhs) {
        return lhs == rhs;
    }

    static public boolean compare (Object lhs, Object rhs) {
        if (lhs == null && rhs == null) return true;
        if (lhs == null && rhs != null) return false;
        if (lhs != null && rhs == null) return false;
        return lhs.equals(rhs);
    }

}

package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.jsonlogic.JsonLogic;

@Named.Symbol("==")
@Operation.Limited(min = 2, max = 2)
public class JLEqual implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... arguments) {
        Object left = arguments[0];
        Object right = arguments[1];

        if (left == null || right == null) {
            return left == right;
        }

        // Check numeric loose equality
        if (left instanceof Number && right instanceof Number) {
            return Double.valueOf(((Number) left).doubleValue()).equals(((Number) right).doubleValue());
        }

        if (left instanceof Number && right instanceof String) {
            return compareNumberToString((Number) left, (String) right);
        }

        if (left instanceof Number && right instanceof Boolean) {
            return compareNumberToBoolean((Number) left, (Boolean) right);
        }

        // Check string loose equality
        if (left instanceof String && right instanceof String) {
            return left.equals(right);
        }

        if (left instanceof String && right instanceof Number) {
            return compareNumberToString((Number) right, (String) left);
        }

        if (left instanceof String && right instanceof Boolean) {
            return compareStringToBoolean((String) left, (Boolean) right);
        }

        // Check boolean loose equality
        if (left instanceof Boolean && right instanceof Boolean) {
            return ((Boolean) left).booleanValue() == ((Boolean) right).booleanValue();
        }

        if (left instanceof Boolean && right instanceof Number) {
            return compareNumberToBoolean((Number) right, (Boolean) left);
        }

        if (left instanceof Boolean && right instanceof String) {
            return compareStringToBoolean((String) right, (Boolean) left);
        }

        // Check non-truthy values
        return !JsonLogic.truthy(left) && !JsonLogic.truthy(right);

    }

    private boolean compareNumberToString(Number left, String right) {
        try {
            if (right.trim().isEmpty()) {
                right = "0";
            }

            return Double.parseDouble(right) == left.doubleValue();
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean compareNumberToBoolean(Number left, Boolean right) {
        if (right) {
            return left.doubleValue() == 1.0;
        }

        return left.doubleValue() == 0.0;
    }

    private boolean compareStringToBoolean(String left, Boolean right) {
        return JsonLogic.truthy(left) == right;
    }

}

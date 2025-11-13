package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.jsonlogic.JsonLogic;

@Named.Symbol("==")
@Operation.Arity(min = 2, max = 2)
public class JLEqual implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... arguments) {
        Object left = arguments[0];
        Object right = arguments[1];

        if (left == null || right == null) {
            return left == right;
        }

        // Check numeric loose equality
        return switch (left) {
            case Number number when right instanceof Number ->
                    Double.valueOf(number.doubleValue()).equals(((Number) right).doubleValue());
            case Number number when right instanceof String -> compareNumberToString(number, (String) right);
            case Number number when right instanceof Boolean -> compareNumberToBoolean(number, (Boolean) right);

            // Check string loose equality
            case String s when right instanceof String -> left.equals(right);
            case String s when right instanceof Number -> compareNumberToString((Number) right, s);
            case String s when right instanceof Boolean -> compareStringToBoolean(s, (Boolean) right);

            // Check boolean loose equality
            case Boolean b when right instanceof Boolean -> b.booleanValue() == ((Boolean) right).booleanValue();
            case Boolean b when right instanceof Number -> compareNumberToBoolean((Number) right, b);
            case Boolean b when right instanceof String -> compareStringToBoolean((String) right, b);
            default ->
                // Check non-truthy values
                    !JsonLogic.truthy(left) && !JsonLogic.truthy(right);
        };

    }

    private boolean compareNumberToString(Number left, String right) {
        try {
            if (right.trim().isEmpty()) {
                right = "0";
            }

            return Double.parseDouble(right) == left.doubleValue();
        } catch (NumberFormatException e) {
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

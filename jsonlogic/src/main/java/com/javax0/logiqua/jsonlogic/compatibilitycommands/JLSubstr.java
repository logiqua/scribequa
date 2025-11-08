package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

@Named.Symbol("substr")
@Operation.Limited(min = 2, max = 3)
public class JLSubstr implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... arguments) {
        final var cast = new Castor(executor);
        if (!(arguments[1] instanceof Number)) {
            throw new IllegalArgumentException("second argument to substr must be a number");
        }

        String value = ""+ arguments[0];
        int startIndex;
        int endIndex;

        if (arguments.length == 2) {
            startIndex = cast.toLong(arguments[1]).orElseThrow(
                    () -> new IllegalArgumentException("The second argument of the substr command must be a number.")
            ).intValue();
            endIndex = value.length();

            if (startIndex < 0) {
                startIndex = endIndex + startIndex;
            }

            if (startIndex < 0) {
                return "";
            }
        } else {
            if (!(arguments[2] instanceof Number)) {
                throw new IllegalArgumentException("third argument to substr must be an integer");
            }

            startIndex = cast.toLong(arguments[1]).orElseThrow(
                    () -> new IllegalArgumentException("The second argument of the substr command must be a number.")
            ).intValue();

            if (startIndex < 0) {
                startIndex = value.length() + startIndex;
            }

            endIndex = cast.toLong(arguments[2]).orElseThrow(
                    () -> new IllegalArgumentException("The third argument of the substr command must be a number.")
            ).intValue();

            if (endIndex < 0) {
                endIndex = value.length() + endIndex;
            } else {
                endIndex += startIndex;
            }

            if (startIndex > endIndex || endIndex > value.length()) {
                return "";
            }
        }

        return value.substring(startIndex, endIndex);
    }
}

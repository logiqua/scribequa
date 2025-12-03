import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.*;

module logiqua.commands {
    requires logiqua.api;
    exports com.javax0.logiqua.commands;
    exports com.javax0.logiqua.commands.utils;
    provides Operation.Function with Log, Var, Max, Min, Missing, MissingSome, Equals, NotEquals, Not,
            LessThan, LessThanOrEqual, GreaterThan, GreaterThanOrEqual, Plus, Minus, Multiply, Divide, Remainder,
            Merge, In, Cat, It, Substr;
    provides Operation.Macro with If, Ternary, Or, And, Map, Filter, All, Some, None, Reduce;

}
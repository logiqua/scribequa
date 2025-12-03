import com.javax0.logiqua.Operation;

module logiqua.engine {
    requires logiqua.api;
    exports com.javax0.logiqua.engine;
    exports com.javax0.logiqua.scripts;
    uses Operation.Function;
    uses Operation.Macro;
}
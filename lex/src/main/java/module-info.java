import com.javax0.lex.analyzers.*;

module logiqua.lex {
    exports com.javax0.lex;
    exports com.javax0.lex.tokens;
    exports com.javax0.lex.analyzers;
    uses com.javax0.lex.Analyzer;
    provides com.javax0.lex.Analyzer with SpaceAnalyzer, SymbolAnalyzer, IntegerNumberAnalyzer, StringAnalyzer,
            IdentifierAnalyzer, NewLineAnalyzer, FloatNumberAnalyzer;
}
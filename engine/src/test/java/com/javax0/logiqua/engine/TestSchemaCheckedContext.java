package com.javax0.logiqua.engine;

import com.javax0.logiqua.Schema;
import com.javax0.logiqua.SchemaViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestSchemaCheckedContext {

    private static final Set<String> KNOWN = Set.of("a", "b", "b.c", "list", "list.0", "list[0]");

    private static final Schema SCHEMA = path -> KNOWN.contains(path)
            ? Schema.Judgement.described()
            : Schema.Judgement.impossible("'" + path + "' is not described");

    private static Map<String, Object> data() {
        return Map.of("a", "A", "b", Map.of(), "list", List.of("first"));
    }

    @Test
    void testDescribedKeyIsDelegated() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        Assertions.assertEquals("A", context.get("a").get());
    }

    @Test
    void testDescribedButAbsentKeyIsNull() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        Assertions.assertNull(context.get("b.c"));
    }

    @Test
    void testUndescribableKeyThrows() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        final var exception = Assertions.assertThrows(SchemaViolationException.class, () -> context.get("b.d"));
        Assertions.assertEquals("b.d", exception.path());
        Assertions.assertTrue(exception.getMessage().contains("is not described"), exception.getMessage());
    }

    @Test
    void testTheEmptyKeyIsNeverRejected() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        Assertions.assertEquals(data(), context.get("").get());
    }

    @Test
    void testAnUnconstrainedKeyPassesWhenLenient() {
        final var context = new SchemaCheckedContext(new MapContext(data()), Schema.OPEN);
        Assertions.assertEquals("A", context.get("a").get());
        Assertions.assertNull(context.get("nowhere"));
    }

    @Test
    void testAnUnconstrainedKeyIsRejectedWhenStrict() {
        final var context = new SchemaCheckedContext(
                new MapContext(data()), Schema.OPEN, SchemaCheckedContext.Strictness.STRICT);
        Assertions.assertThrows(SchemaViolationException.class, () -> context.get("a"));
    }

    @Test
    void testASproutedLocalScopeIsNotChecked() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        final var local = context.sprout(Map.of("current", "element"));
        Assertions.assertEquals("element", local.get("current").get());
        // the name would be a schema violation in the outer context, in a loop body it is only unknown
        Assertions.assertDoesNotThrow(() -> local.get("undescribed"));
    }

    @Test
    void testTheContextReportsItsSchema() {
        final var context = SchemaCheckedContext.of(data(), SCHEMA);
        Assertions.assertSame(SCHEMA, context.schema());
    }

    @Test
    void testAPlainContextReportsTheOpenSchema() {
        Assertions.assertSame(Schema.OPEN, new MapContext(data()).schema());
    }

    @Test
    void testAnEngineBuiltWithASchemaChecksItsVariables() {
        final var engine = Engine.withData(data(), SCHEMA);
        Assertions.assertEquals("A", engine.getOp("var").args("a").evaluate());
        final var script = engine.getOp("var").args("b.d", "fallback");
        Assertions.assertThrows(SchemaViolationException.class, script::evaluate);
    }

    @Test
    void testSegmentsDefineTheSharedPathSyntax() {
        Assertions.assertArrayEquals(new String[]{"a"}, com.javax0.logiqua.Context.segments("a"));
        Assertions.assertArrayEquals(new String[]{"a", "b"}, com.javax0.logiqua.Context.segments("a.b"));
        Assertions.assertArrayEquals(new String[]{"a", "0"}, com.javax0.logiqua.Context.segments("a[0]"));
        Assertions.assertArrayEquals(new String[]{"a", "0", "b"}, com.javax0.logiqua.Context.segments("a[0].b"));
        Assertions.assertArrayEquals(new String[]{""}, com.javax0.logiqua.Context.segments(""));
    }
}

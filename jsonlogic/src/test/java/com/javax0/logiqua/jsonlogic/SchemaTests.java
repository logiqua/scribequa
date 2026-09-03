package com.javax0.logiqua.jsonlogic;

import com.javax0.logiqua.Schema;
import com.javax0.logiqua.SchemaViolationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JsonLogic has no undefined variable, every unknown name evaluates to {@code null}, so a typo in a
 * rule is invisible. A schema of the data separates the name that may be absent from the name that
 * cannot exist.
 * <p>
 * The schema here is written by hand rather than taken from a JSON Schema document, to show that
 * {@link Schema} is a plain interface and the JSON Schema implementation in the {@code schema} module
 * is only one way to fill it.
 */
public class SchemaTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    private static final Set<String> KNOWN = Set.of("pi", "user", "user.name", "user.age");

    private static final Schema SCHEMA = path ->
            KNOWN.contains(path)
                    ? Schema.Judgement.described()
                    : Schema.Judgement.impossible("'" + path + "' is not one of " + KNOWN);

    private static final Map<String, Object> DATA = Map.of(
            "pi", 3.14,
            "user", Map.of("name", "Alice"));

    @Test
    public void testDescribedVariableIsRead() {
        assertEquals(3.14, jsonLogic.apply("""
                {"var": "pi"}
                """, DATA, SCHEMA));
    }

    @Test
    public void testDescribedButAbsentVariableIsStillNull() {
        assertNull(jsonLogic.apply("""
                {"var": "user.age"}
                """, DATA, SCHEMA));
    }

    @Test
    public void testDescribedButAbsentVariableStillTakesTheDefault() {
        assertEquals(41L, jsonLogic.apply("""
                {"var": ["user.age", 41]}
                """, DATA, SCHEMA));
    }

    @Test
    public void testUndescribableVariableThrowsInsteadOfReturningNull() {
        final var exception = assertThrows(SchemaViolationException.class, () -> jsonLogic.apply("""
                {"var": "user.nmae"}
                """, DATA, SCHEMA));
        assertEquals("user.nmae", exception.path());
    }

    @Test
    public void testUndescribableVariableThrowsEvenWithADefault() {
        assertThrows(SchemaViolationException.class, () -> jsonLogic.apply("""
                {"var": ["user.nmae", "fallback"]}
                """, DATA, SCHEMA));
    }

    @Test
    public void testWithoutASchemaTheBehaviourIsUnchanged() {
        assertNull(jsonLogic.apply("""
                {"var": "user.nmae"}
                """, DATA));
    }

    @Test
    public void testTheSchemaJudgesPathsRelativeToTheData() {
        assertEquals("Alice", jsonLogic.apply("""
                {"var": "user.name"}
                """, DATA, SCHEMA));
    }

    @Test
    public void testMissingReportsAnAbsentVariableButThrowsForAnUndescribableOne() {
        assertEquals(List.of("user.age"), jsonLogic.apply("""
                {"missing": ["pi", "user.age"]}
                """, DATA, SCHEMA));
        assertThrows(SchemaViolationException.class, () -> jsonLogic.apply("""
                {"missing": ["pi", "user.aeg"]}
                """, DATA, SCHEMA));
    }

    @Test
    public void testTheDataItselfIsAlwaysReadable() {
        assertEquals(3.14, jsonLogic.apply("""
                {"var": ""}
                """, 3.14, SCHEMA));
    }
}

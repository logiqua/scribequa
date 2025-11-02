package com.javax0.logiqua.json;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonReader {

    private static Object parseToObject(String sample) {
        final var analyzer = new LexicalAnalyzer();
        analyzer.skip(Space.class);
        final var tokenArray = analyzer.analyse(StringInput.of(sample));
        final var tokens = TokenIterator.over(tokenArray);
        return JsonReader.of(tokens).read();
    }

    @Test
    void readSampleJson() {
        final var sample = """
                {"a":1,"b":"Hello, World!"}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertEquals("{a=1, b=Hello, World!}", jsonObject.toString());
    }


    @Test
    void readEmptyObject() {
        final var sample = "{}";
        final var jsonObject = parseToObject(sample);
        Assertions.assertEquals("{}", jsonObject.toString());
    }

    @Test
    void readSingleProperty() {
        final var sample = """
                {"name":"John"}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertEquals("{name=John}", jsonObject.toString());
    }

    @Test
    void readMultipleStringProperties() {
        final var sample = """
                {"first":"Alice","middle":"Marie","last":"Smith"}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("first=Alice"));
        Assertions.assertTrue(jsonObject.toString().contains("middle=Marie"));
        Assertions.assertTrue(jsonObject.toString().contains("last=Smith"));
    }

    @Test
    void readMultipleNumericProperties() {
        final var sample = """
                {"x":10,"y":20,"z":30}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("x=10"));
        Assertions.assertTrue(jsonObject.toString().contains("y=20"));
        Assertions.assertTrue(jsonObject.toString().contains("z=30"));
    }

    @Test
    void readMixedTypes() {
        final var sample = """
                {"id":42,"active":true,"name":"Test"}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("id=42"));
        Assertions.assertTrue(jsonObject.toString().contains("active=true"));
        Assertions.assertTrue(jsonObject.toString().contains("name=Test"));
    }

    @Test
    void readBooleanTrue() {
        final var sample = """
                {"enabled":true}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("enabled=true"));
    }

    @Test
    void readBooleanFalse() {
        final var sample = """
                {"enabled":false}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("enabled=false"));
    }

    @Test
    void readNullValue() {
        final var sample = """
                {"value":null}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("value=null"));
    }

    @Test
    void readZeroValue() {
        final var sample = """
                {"count":0}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("count=0"));
    }

    @Test
    void readNegativeNumber() {
        final var sample = """
                {"temperature":-15}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("temperature=-15"));
    }

    @Test
    void readFloatingPointNumber() {
        final var sample = """
                {"price":19.99}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("price=19.99"));
    }

    @Test
    void readStringWithSpecialCharacters() {
        final var sample = """
                {"message":"Hello! @#$%"}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("message=Hello! @#$%"));
    }

    @Test
    void readStringWithEscapedQuotes() {
        final var sample = """
                {"quote":"She said \\"Hello\\""}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("quote="));
    }

    @Test
    void readLargeJsonObject() {
        final var sample = """
                {"field1":1,"field2":2,"field3":3,"field4":4,"field5":5}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("field1=1"));
        Assertions.assertTrue(jsonObject.toString().contains("field5=5"));
    }

    @Test
    void readJsonWithWhitespace() {
        final var sample = """
                { "a" : 1 , "b" : "test" }
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("a=1"));
        Assertions.assertTrue(jsonObject.toString().contains("b=test"));
    }

    @Test
    void readEmptyArray(){
        final var sample = """
                {"items":[]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("items=[]"));
    }

    @Test
    void readArrayOfNumbers(){
        final var sample = """
                {"numbers":[1,2,3,4,5]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("numbers="));
        Assertions.assertTrue(jsonObject.toString().contains("1"));
        Assertions.assertTrue(jsonObject.toString().contains("5"));
    }

    @Test
    void readArrayOfStrings(){
        final var sample = """
                {"colors":["red","green","blue"]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("colors="));
        Assertions.assertTrue(jsonObject.toString().contains("red"));
        Assertions.assertTrue(jsonObject.toString().contains("blue"));
    }

    @Test
    void readArrayOfBooleans(){
        final var sample = """
                {"flags":[true,false,true]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("flags="));
        Assertions.assertTrue(jsonObject.toString().contains("true"));
    }

    @Test
    void readArrayOfMixedTypes(){
        final var sample = """
                {"mixed":[1,"text",true,null]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("mixed="));
        Assertions.assertTrue(jsonObject.toString().contains("1"));
        Assertions.assertTrue(jsonObject.toString().contains("text"));
    }

    @Test
    void readArrayOfObjects(){
        final var sample = """
                {"users":[{"name":"Alice","age":30},{"name":"Bob","age":25}]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("users="));
        Assertions.assertTrue(jsonObject.toString().contains("Alice"));
        Assertions.assertTrue(jsonObject.toString().contains("Bob"));
    }

    @Test
    void readNestedObject(){
        final var sample = """
                {"person":{"name":"John","address":{"city":"NYC","zip":"10001"}}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("person="));
        Assertions.assertTrue(jsonObject.toString().contains("John"));
        Assertions.assertTrue(jsonObject.toString().contains("NYC"));
    }

    @Test
    void readTwoDimensionalArray(){
        final var sample = """
                {"matrix":[[1,2,3],[4,5,6],[7,8,9]]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("matrix="));
        Assertions.assertTrue(jsonObject.toString().contains("1"));
        Assertions.assertTrue(jsonObject.toString().contains("9"));
    }

    @Test
    void readThreeDimensionalArray(){
        final var sample = """
                {"cube":[[[1,2],[3,4]],[[5,6],[7,8]]]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("cube="));
        Assertions.assertTrue(jsonObject.toString().contains("1"));
        Assertions.assertTrue(jsonObject.toString().contains("8"));
    }

    @Test
    void readArrayOfArraysOfObjects(){
        final var sample = """
                {"data":[[{"id":1},{"id":2}],[{"id":3},{"id":4}]]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("data="));
        Assertions.assertTrue(jsonObject.toString().contains("id"));
    }

    @Test
    void readObjectWithNestedArraysAndObjects(){
        final var sample = """
                {"company":{"name":"TechCorp","departments":[{"dept":"Engineering","members":["Alice","Bob"]},{"dept":"Sales","members":["Carol"]}]}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("company="));
        Assertions.assertTrue(jsonObject.toString().contains("TechCorp"));
        Assertions.assertTrue(jsonObject.toString().contains("Engineering"));
        Assertions.assertTrue(jsonObject.toString().contains("Alice"));
    }

    @Test
    void readMapWithNumericAndStringKeys(){
        final var sample = """
                {"data":{"field1":"value1","field2":"value2","field3":"value3"}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("field1=value1"));
        Assertions.assertTrue(jsonObject.toString().contains("field3=value3"));
    }

    @Test
    void readNestedMapsMultipleLevels(){
        final var sample = """
                {"level1":{"level2":{"level3":{"level4":"deep value"}}}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("level1="));
        Assertions.assertTrue(jsonObject.toString().contains("deep value"));
    }

    @Test
    void readMapWithArrayValues(){
        final var sample = """
                {"config":{"servers":["server1","server2","server3"],"ports":[8080,8081,8082]}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("config="));
        Assertions.assertTrue(jsonObject.toString().contains("servers"));
        Assertions.assertTrue(jsonObject.toString().contains("ports"));
    }

    @Test
    void readComplexNestedStructure(){
        final var sample = """
                {"organization":{"name":"AcmeCorp","offices":[{"location":"HQ","teams":[{"name":"Platform","members":["Dev1","Dev2"],"budget":50000},{"name":"Support","members":["Support1"],"budget":30000}]},{"location":"Branch","teams":[{"name":"Sales","members":["Sales1","Sales2"],"budget":40000}]}]}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("AcmeCorp"));
        Assertions.assertTrue(jsonObject.toString().contains("Platform"));
        Assertions.assertTrue(jsonObject.toString().contains("Dev1"));
        Assertions.assertTrue(jsonObject.toString().contains("50000"));
    }

    @Test
    void readArrayContainingMapsAndArrays(){
        final var sample = """
                {"records":[{"user":"Alice","scores":[95,87,92]},{"user":"Bob","scores":[88,90]}]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("records="));
        Assertions.assertTrue(jsonObject.toString().contains("Alice"));
        Assertions.assertTrue(jsonObject.toString().contains("95"));
        Assertions.assertTrue(jsonObject.toString().contains("scores="));
    }

    @Test
    void readMapWithEmptyNestedStructures(){
        final var sample = """
                {"parent":{"empty_array":[],"empty_object":{},"value":"test"}}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("parent="));
        Assertions.assertTrue(jsonObject.toString().contains("empty_array=[]"));
    }

    @Test
    void readRaggedArray(){
        final var sample = """
                {"data":[[1,2,3],[4,5],[6,7,8,9]]}
                """;
        final var jsonObject = parseToObject(sample);
        Assertions.assertTrue(jsonObject.toString().contains("data="));
        Assertions.assertTrue(jsonObject.toString().contains("1"));
        Assertions.assertTrue(jsonObject.toString().contains("9"));
    }
}

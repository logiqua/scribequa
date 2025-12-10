package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class TestComplexExpression {

    @Test
    void testItAll() {
        final var script = """
                credit > debt * ratio 
                """;
        final var data = Map.<String, Object>of("currency", "EUR", "debt", 44.0, "credit", 56, "ratio", 1.6);
        final var scriptObject = new ExpLogiqua().with(data).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
        Assertions.assertEquals("""
                {">":[{"var":["credit"]},{"*":[{"var":["debt"]},{"var":["ratio"]}]}]}\
                """,scriptObject.jsonify());
    }

    @Test
    void testDeeplyNestedComplexExpression() {
        // Create a complex nested data structure
        final var companyData = Map.<String, Object>of(
                // Primitive values
                "companyId", 12345L,
                "companyName", "Acme Corp",
                "isActive", true,
                
                // Nested map with primitive wrappers
                "financials", Map.of(
                        "revenue", 1000000.50,
                        "expenses", 750000,
                        "profit", 250000L,
                        "currency", "USD"
                ),
                
                // Nested map with deeper structure
                "departments", Map.of(
                        "engineering", Map.of(
                                "headCount", 50,
                                "budget", BigDecimal.valueOf(500000.75),
                                "projects", List.of(
                                        Map.of("name", "Project Alpha", "status", "active", "budget", 100000),
                                        Map.of("name", "Project Beta", "status", "planning", "budget", 50000),
                                        Map.of("name", "Project Gamma", "status", "completed", "budget", 200000)
                                ),
                                "manager", Map.of(
                                        "name", "John Doe",
                                        "employeeId", 1001,
                                        "salary", 120000.0,
                                        "bonus", BigInteger.valueOf(15000)
                                )
                        ),
                        "sales", Map.of(
                                "headCount", 30,
                                "budget", BigDecimal.valueOf(300000.25),
                                "regions", List.of("North", "South", "East", "West"),
                                "targets", List.of(1000000, 800000, 600000, 400000)
                        )
                ),
                
                // List of maps
                "employees", List.of(
                        Map.of("id", 1, "name", "Alice", "salary", 80000, "department", "engineering"),
                        Map.of("id", 2, "name", "Bob", "salary", 75000, "department", "sales"),
                        Map.of("id", 3, "name", "Charlie", "salary", 90000, "department", "engineering")
                ),
                
                // Nested lists
                "metrics", List.of(
                        List.of(100, 200, 300),
                        List.of(400, 500, 600),
                        List.of(700, 800, 900)
                )
        );
        
        // Complex multi-level expression with parentheses
        final var script = """
                (var("financials.revenue") - var("financials.expenses") > 200000) and 
                (var("departments.engineering.headCount") + var("departments.sales.headCount") > 70) and
                (var("departments.engineering.manager.salary") * 1.1 < 150000) and
                (reduce(var("departments.engineering.projects"), accumulator + var("current.budget"), 0) > 300000) and
                (all(employees, var("current.salary") > 70000)) and
                (some(var("departments.sales.regions"), current == "North")) and
                (var("metrics[0][0]") + var("metrics[1][1]") + var("metrics[2][2]") == 1500)
                """;
        
        final var scriptObject = new ExpLogiqua().with(companyData).compile(script);
        final var result = scriptObject.evaluate();
        
        // Verify evaluation result
        Assertions.assertEquals(true, result, "Complex nested expression should evaluate to true");
        
        // Verify JSON output
        final var jsonOutput = scriptObject.jsonify();
        Assertions.assertNotNull(jsonOutput, "jsonify() should return a non-null string");
        Assertions.assertFalse(jsonOutput.isEmpty(), "jsonify() should return a non-empty string");
        Assertions.assertEquals("""
                {
                  "and": [
                    {
                      "and": [
                        {
                          "and": [
                            {
                              "and": [
                                {
                                  "and": [
                                    {
                                      "and": [
                                        {
                                          ">": [
                                            {
                                              "-": [
                                                {
                                                  "var": [
                                                    "financials.revenue"
                                                  ]
                                                },
                                                {
                                                  "var": [
                                                    "financials.expenses"
                                                  ]
                                                }
                                              ]
                                            },
                                            200000
                                          ]
                                        },
                                        {
                                          ">": [
                                            {
                                              "+": [
                                                {
                                                  "var": [
                                                    "departments.engineering.headCount"
                                                  ]
                                                },
                                                {
                                                  "var": [
                                                    "departments.sales.headCount"
                                                  ]
                                                }
                                              ]
                                            },
                                            70
                                          ]
                                        }
                                      ]
                                    },
                                    {
                                      "<": [
                                        {
                                          "*": [
                                            {
                                              "var": [
                                                "departments.engineering.manager.salary"
                                              ]
                                            },
                                            1.1
                                          ]
                                        },
                                        150000
                                      ]
                                    }
                                  ]
                                },
                                {
                                  ">": [
                                    {
                                      "reduce": [
                                        {
                                          "var": [
                                            "departments.engineering.projects"
                                          ]
                                        },
                                        {
                                          "+": [
                                            {
                                              "var": [
                                                "accumulator"
                                              ]
                                            },
                                            {
                                              "var": [
                                                "current.budget"
                                              ]
                                            }
                                          ]
                                        },
                                        0
                                      ]
                                    },
                                    300000
                                  ]
                                }
                              ]
                            },
                            {
                              "all": [
                                {
                                  "var": [
                                    "employees"
                                  ]
                                },
                                {
                                  ">": [
                                    {
                                      "var": [
                                        "current.salary"
                                      ]
                                    },
                                    70000
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "some": [
                            {
                              "var": [
                                "departments.sales.regions"
                              ]
                            },
                            {
                              "==": [
                                {
                                  "var": [
                                    "current"
                                  ]
                                },
                                "North"
                              ]
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "==": [
                        {
                          "+": [
                            {
                              "+": [
                                {
                                  "var": [
                                    "metrics[0][0]"
                                  ]
                                },
                                {
                                  "var": [
                                    "metrics[1][1]"
                                  ]
                                }
                              ]
                            },
                            {
                              "var": [
                                "metrics[2][2]"
                              ]
                            }
                          ]
                        },
                        1500
                      ]
                    }
                  ]
                }""".replaceAll("[\n\r\t ]",""),jsonOutput);
    }
}

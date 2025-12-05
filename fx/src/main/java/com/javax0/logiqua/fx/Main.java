package com.javax0.logiqua.fx;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.exp.ExpLogiqua;
import com.javax0.logiqua.json.JsonLogiqua;
import com.javax0.logiqua.json.JsonReader;
import com.javax0.logiqua.jsonlogic.JsonLogic;
import com.javax0.logiqua.lsp.LspLogiqua;
import com.javax0.logiqua.xml.XmlLogiqua;
import com.javax0.logiqua.yaml.YamlLogiqua;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;


public class Main extends Application {

    @Override
    public void start(Stage stage) {

        final var root = new VBox(10);
        root.setPadding(new Insets(20, 10, 10, 10));
        final var formatSelectorBox = new HBox(20);
        final var group = new ToggleGroup();
        final var rbJson = new RadioButton("JSON");
        rbJson.setToggleGroup(group);
        rbJson.setSelected(true);
        rbJson.setUserData("J");
        final var rbYaml = new RadioButton("YAML");
        rbYaml.setToggleGroup(group);
        rbYaml.setUserData("Y");
        final var rbXml = new RadioButton("XML");
        rbXml.setToggleGroup(group);
        rbXml.setUserData("X");
        final var rbLsp = new RadioButton("Lisp");
        rbLsp.setToggleGroup(group);
        rbLsp.setUserData("L");
        final var rbExp = new RadioButton("Expression");
        rbExp.setToggleGroup(group);
        rbExp.setUserData("E");
        final var rbJsonLogica = new RadioButton("JSON CompatibilityMode");
        rbJsonLogica.setToggleGroup(group);
        rbJsonLogica.setUserData("C");

        formatSelectorBox.getChildren().addAll(rbJson, rbYaml, rbXml, rbLsp, rbExp, rbJsonLogica);


        final var logicLabel = new Label("Logic");
        final var logic = new TextArea();
        final var dataLabel = new Label("Data in JSON format");
        final var data = new TextArea();
        final var runButton = new Button("Run");
        final var resultLabel = new Label("Result");
        final var result = new TextArea();

        runButton.setOnAction(e -> {
            try {
                final var analyzer = new LexicalAnalyzer();
                analyzer.skip(Space.class);
                analyzer.skip(NewLine.class);
                final var tokenArray = analyzer.analyse(StringInput.of(data.getText()));
                final Object dataMap;
                if (tokenArray.length == 0) {
                    dataMap = null;
                } else {
                    final var tokens = TokenIterator.over(tokenArray);
                    dataMap = JsonReader.of(tokens).read();
                }
                if (dataMap == null || dataMap instanceof Map<?, ?>) {
                    final var map = (Map<String, Object>) dataMap;
                    final String logicCode = logic.getText();
                    if (logicCode.isBlank()) {
                        result.setText("Logic is empty");
                        result.setStyle("-fx-text-fill: red;");
                    } else {
                        final var resultString =
                                switch (group.getSelectedToggle().getUserData().toString()) {
                                    case "J" -> new JsonLogiqua().with(map).compile(logicCode).evaluate();
                                    case "Y" -> new YamlLogiqua().with(map).compile(logicCode).evaluate();
                                    case "X" -> new XmlLogiqua().with(map).compile(logicCode).evaluate();
                                    case "L" -> new LspLogiqua().with(map).compile(logicCode).evaluate();
                                    case "E" -> new ExpLogiqua().with(map).compile(logicCode).evaluate();
                                    case "C" -> new JsonLogic().apply(logicCode, map);
                                    default -> "Unknown format";
                                };
                        result.setText(resultString.toString());
                        result.setStyle("-fx-text-fill: black;");
                    }
                } else {
                    result.setText("Data is not a JSON object");
                    result.setStyle("-fx-text-fill: red;");
                }

            } catch (Exception ex) {
                result.setText(ExceptionDumper.throwableToString(ex));
                result.setStyle("-fx-text-fill: red;");

            }
        });

        root.getChildren().addAll(formatSelectorBox, logicLabel, logic, dataLabel, data, runButton, resultLabel, result);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Logiqua");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

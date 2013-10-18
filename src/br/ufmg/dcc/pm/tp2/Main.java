package br.ufmg.dcc.pm.tp2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception{

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Escolher arquivo Bibtex...");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        try {
                            showApplication(primaryStage, file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        final GridPane inputGridPane = new GridPane();
        GridPane.setConstraints(openButton, 0, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        primaryStage.setScene(new Scene(rootGroup));
        primaryStage.show();


    }

    private void showApplication(Stage primaryStage, File file) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        final Parent root = (Parent) loader.load();
        final MainWindow controller = loader.<MainWindow>getController();
        controller.setFile(file.getAbsolutePath());
        controller.setStage(primaryStage);

        // initialize the stage.
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("PMCC-TP2");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

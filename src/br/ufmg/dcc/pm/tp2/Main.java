package br.ufmg.dcc.pm.tp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        final Parent root = (Parent) loader.load();
        final MainWindow controller = loader.<MainWindow>getController();
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

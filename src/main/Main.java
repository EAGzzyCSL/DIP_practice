package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import my.Constant;
import my.MyTabs;

import java.util.ResourceBundle;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"),
                null);
        Parent root = loader.load();
        primaryStage.setTitle("08111302班1120132110赵仲印数字图像处理作业");
        Scene myScene = new Scene(root, 1024, 576);

        primaryStage.setScene(myScene);
        Platform.runLater(() ->
                myAddTab(loader, myScene)
        );
        primaryStage.show();

    }

    private void myAddTab(FXMLLoader loader, Scene myScene) {
        TabPane tabPane_main = (TabPane) myScene.lookup("#tabPane_main");
        MyTabs myTabs = new MyTabs(tabPane_main);
        ((MainController) loader.getController()).setMyTabs(myTabs);
        myTabs.addNewTab(Constant.function_tab, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

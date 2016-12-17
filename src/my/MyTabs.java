package my;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyTabs {
    private TabPane tabPane;
    public ArrayList<MyController> myControllers = new ArrayList<>();

    public MyTabs(TabPane tabPane) {
        this.tabPane = tabPane;

    }
    public boolean isEmpty(){
        return myControllers.isEmpty();
    }

    public void addNewTab(TabInfo tabInfo, Object addOn) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(tabInfo.getFxmlFileName()));
        try {
            Node node_newTab = fxmlLoader.load();
            MyController myController = fxmlLoader.<MyController>getController();
            myController.dealAddOn(addOn);
            myControllers.add(
                    tabPane.getSelectionModel().getSelectedIndex()+1,
                    myController);
            myController.setMyTabs(this);
            Tab tab = new Tab(tabInfo.getTabTitle(), node_newTab);
            tab.setOnClosed((Event) -> {
                myControllers.remove(myController);
                System.out.println("close");
            });
            tabPane.getTabs().add(
                    tabPane.getSelectionModel().getSelectedIndex()+1,
                    tab);
            tabPane.getSelectionModel().selectNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void whenOpen(File file) {
        myControllers.get(
                tabPane.getSelectionModel().getSelectedIndex()
        ).whenOpen(file);
    }

    public void whenSave(File file) {
        myControllers.get(
                tabPane.getSelectionModel().getSelectedIndex()
        ).whenSave(file);
    }
}

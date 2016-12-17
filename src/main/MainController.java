package main;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import my.Constant;
import my.MyTabs;

import java.io.File;

public class MainController {
    private MyTabs myTabs;

    public void setMyTabs(MyTabs myTabs) {
        this.myTabs = myTabs;
    }


    private FileChooser myFileChooser = new FileChooser();
    @FXML
    private TabPane tabPane_main;

    @FXML
     void openFile() {
        File f = myFileChooser.showOpenDialog(tabPane_main.getScene().getWindow());
        if(myTabs.isEmpty()){
            myTabs.addNewTab(Constant.function_tab,null);
        }
        if (f != null) {
            myTabs.whenOpen(f);
        }
    }
    @FXML void showAbout(){
        myTabs.addNewTab(Constant.about_tab,null);
    }

    @FXML
     void saveFile() {
        File f = myFileChooser.showSaveDialog(tabPane_main.getScene().getWindow());
        if (f != null) {
           myTabs.whenSave(f);
        }
    }
    @FXML void newFile(){
        myTabs.addNewTab(Constant.function_tab,null);
    }
}

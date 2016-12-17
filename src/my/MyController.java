package my;

import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public abstract class MyController implements Initializable {
    private MyTabs myTabs;

    public abstract void whenOpen(File file);

    public void dealAddOn(Object addOn) {
        if (addOn == null) {
            return;
        }
    }

    public abstract void whenSave(File file);

    public void setMyTabs(MyTabs myTabs) {
        this.myTabs = myTabs;
    }

    protected void addNewTab(TabInfo tabInfo, Object addOn)


    {
        myTabs.addNewTab(tabInfo, addOn);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

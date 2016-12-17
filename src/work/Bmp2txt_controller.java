package work;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import my.MyController;
import my.MyImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Bmp2txt_controller extends MyController {
    @Override
    public void whenOpen(File file) {

    }

    @Override
    public void whenSave(File file) {
        try {
            FileWriter fileWriter=new FileWriter(file);
            fileWriter.write(textArea_bmpTxt.getText());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @FXML
    public TextArea textArea_bmpTxt;
    @Override
    public void dealAddOn(Object addOn) {
        super.dealAddOn(addOn);
        if(addOn instanceof MyImage){
            textArea_bmpTxt.setText(
                    ((MyImage)addOn).bmp2txt()
            );
        }
    }


}

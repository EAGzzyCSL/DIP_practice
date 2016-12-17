package work;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import my.MyController;
import my.MyImage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class BitmapPane_controller extends MyController {

    @Override
    public void whenOpen(File file) {

    }

    @Override
    public void whenSave(File file) {

    }
    @FXML
    private ImageView bitmapPane_0;
    @FXML
    private ImageView bitmapPane_1;
    @FXML
    private ImageView bitmapPane_2;
    @FXML
    private ImageView bitmapPane_3;
    @FXML
    private ImageView bitmapPane_4;
    @FXML
    private ImageView bitmapPane_5;
    @FXML
    private ImageView bitmapPane_6;
    @FXML
    private ImageView bitmapPane_7;
    private ImageView[] imageView_bitmapPanes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView_bitmapPanes = new ImageView[]{
                bitmapPane_0,
                bitmapPane_1,
                bitmapPane_2,
                bitmapPane_3,
                bitmapPane_4,
                bitmapPane_5,
                bitmapPane_6,
                bitmapPane_7,
        };


    }

    @Override
    public void dealAddOn(Object addOn) {
        super.dealAddOn(addOn);
        if( addOn instanceof MyImage){
            MyImage[] bitmapPanes = ((MyImage)addOn).getBitmapPane();
            for (int i = 0; i < imageView_bitmapPanes.length; i++) {
                imageView_bitmapPanes[i].setImage(
                        bitmapPanes[i].toFXImage()
                );
            }
        }
    }
}

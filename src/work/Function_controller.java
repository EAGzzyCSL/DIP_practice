package work;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import my.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Function_controller extends MyController {
    @Override
    public void dealAddOn(Object addOn) {
        super.dealAddOn(addOn);
        if (addOn instanceof MyImage) {
            originImage = (MyImage) addOn;
            doneImage = originImage;
            imageView_show.setImage(
                    originImage.toFXImage()
            );
        }
    }

    private MyImage originImage;
    private MyImage doneImage;
    @FXML
    private ImageView imageView_show;
    @FXML
    private Slider slider_sample;
    @FXML
    private Slider slider_quantify;
    @FXML
    private Slider slider_binary;

    @Override
    public void whenOpen(File file) {
        originImage = new MyImage(file);
        doneImage = originImage;
        imageView_show.setImage(
                originImage.toFXImage()
        );

    }

    @Override
    public void whenSave(File file) {
        doneImage.saveMe(file);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider_sample.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number oldValue, Number newValue) -> {
            if (newValue.intValue() != oldValue.intValue()) {

                doneImage = originImage.doSample(newValue.intValue());
                imageView_show.setImage(doneImage.toFXImage()
                );
            }

        });
        slider_quantify.valueProperty().addListener((
                ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) -> {
            if (newValue.intValue() != oldValue.intValue()) {
                doneImage = originImage.doQuantify(newValue.intValue());
                imageView_show.setImage(doneImage.toFXImage());
            }

        });
        slider_binary.valueProperty().addListener((
                ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) -> {
            if (newValue.intValue() != oldValue.intValue()) {
                doneImage = originImage.getBinary(newValue.intValue());
                imageView_show.setImage(doneImage.toFXImage());
            }

        });
    }

    @FXML
    public void showBitmapPane() {
        addNewTab(Constant.bitmapPane_tab, doneImage);
    }

    @FXML
    public void showHistogram() {
        addNewTab(Constant.histogram_tab, doneImage);
    }

    @FXML
    private TextField textField_g1_old;
    @FXML
    private TextField textField_g1_new;
    @FXML
    private TextField textField_g2_old;
    @FXML
    private TextField textField_g2_new;

    @FXML
    public void pointOperation() {
        int g1_old = Integer.valueOf(textField_g1_old.getText());
        int g1_new = Integer.valueOf(textField_g1_new.getText());
        int g2_old = Integer.valueOf(textField_g2_old.getText());
        int g2_new = Integer.valueOf(textField_g2_new.getText());
        doneImage = originImage.pointOperation(g1_old, g1_new, g2_old, g2_new);
        imageView_show.setImage(doneImage.toFXImage());

    }

    @FXML
    public void histogramEqualization() {
        doneImage = originImage.histogramEqualization();
        imageView_show.setImage(doneImage.toFXImage());
    }

    @FXML
    public void bmp2txt() {
        addNewTab(Constant.bmp2txt, doneImage);
    }

    @FXML
    public void smooth_e() {
        doneImage = originImage.smooth_e();
        imageView_show.setImage(doneImage.toFXImage());
    }

    @FXML
    public void smooth_m() {
        doneImage = originImage.smooth_m();
        imageView_show.setImage(doneImage.toFXImage());
    }

    @FXML
    public void smooth_k() {
        doneImage = originImage.smooth_k();
        imageView_show.setImage(doneImage.toFXImage());
    }

    @FXML
    public void reset() {
        doneImage = originImage.myClone();
        imageView_show.setImage(doneImage.toFXImage());
    }

    @FXML
    public void openInNewTab() {
        addNewTab(Constant.function_tab, doneImage.myClone());
    }

    @FXML
    public void sharpen_Sobel(){
        doneImage=originImage.sharpen(1);
        imageView_show.setImage(doneImage.toFXImage());

    }

    @FXML
    public void sharpen_Prewitt(){
        doneImage=originImage.sharpen(2);
        imageView_show.setImage(doneImage.toFXImage());

    }
    @FXML
    public void sharpen_Isotropic(){
        doneImage=originImage.sharpen(3);
        imageView_show.setImage(doneImage.toFXImage());
    }
    @FXML private TextField textField_transform_translationX;
    @FXML private TextField textField_transform_translationY;
    @FXML private TextField textField_transform_rotate;
    @FXML private TextField textField_transform_zoomX;
    @FXML private TextField textField_transform_zoomY;
    @FXML
    public void transform_translation(){
        MyMatrix myMatrix=new MyMatrix(
                new double[]{
                        1,0,0,
                        0,1,0,
                        0,0,1,
                }
        );
        double dx=Double.valueOf(textField_transform_translationX.getText());
        double dy=Double.valueOf(textField_transform_translationY.getText());
        double degree=Double.valueOf(textField_transform_rotate.getText());
        double sx=Double.valueOf(textField_transform_zoomX.getText());
        double sy=Double.valueOf(textField_transform_zoomY.getText());
        myMatrix.postTranslate(dx,dy).
                postScale(sx,sy).postRotate(MyUtils.degreeToRadian(degree));
        doneImage=originImage.matrixTransform(myMatrix);
        imageView_show.setImage(doneImage.toFXImage());
    }
    @FXML
    public void erosion(){
        doneImage=originImage.getBinary(128).erosion(new int[][]{
                {1,1,1},
                {1,1,1},
                {1,1,1},
        });
        imageView_show.setImage(doneImage.toFXImage());
    }
    @FXML
    public void Expand(){
        doneImage=originImage.getBinary(128).expand(new int[][]{
                {1,1,1},
                {1,1,1},
                {1,1,1},
        });
        imageView_show.setImage(doneImage.toFXImage());
    }

}

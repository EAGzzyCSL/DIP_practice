package work;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import my.MyController;
import my.MyImage;

import java.io.File;


public class Histogram_controller extends MyController {
    @FXML
    private AreaChart<Integer, Integer> areaChart_grayHistogram;
    @FXML
    private Label label_avgGray;
    @FXML
    private Label label_pxAmount;
    @FXML
    private Label label_medianGray;
    @FXML
    private Label label_standardDeviation;
    @Override
    public void whenOpen(File file) {

    }

    @Override
    public void whenSave(File file) {

    }

    @Override
    public void dealAddOn(Object addOn) {
        super.dealAddOn(addOn);
        if (addOn instanceof MyImage) {


            XYChart.Series<Integer, Integer> seriesGray = new XYChart.Series<>();
            seriesGray.setName("gray");
            int[] grayH = ((MyImage) addOn).getGrayHistogram();
            ObservableList<XYChart.Data<Integer, Integer>> grayData =
                    seriesGray.getData();
            for (int i = 0; i < grayH.length; i++) {
                grayData.add(new XYChart.Data<>(i, grayH[i]));
            }
            ObservableList<XYChart.Series<Integer, Integer>> chartSeries =
                    areaChart_grayHistogram.getData();
            chartSeries.clear();
            chartSeries.add(seriesGray);


            MyImage.GrayInfo grayInfo = ((MyImage) addOn).getGrayInfo();
            label_avgGray.setText("平均灰度：" + grayInfo.getAvgGray());
            label_medianGray.setText("中值灰度：" + grayInfo.getMedianGray());
            label_pxAmount.setText("像素总数：" + grayInfo.getPxAmount());
            label_standardDeviation.setText("标准差：" + grayInfo.getStandardDeviation());
        }
    }
}

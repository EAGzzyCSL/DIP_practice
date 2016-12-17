package my;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MyImage {
    /*ini by constructor*/
    private BufferedImage image;
    private String format;
    /*ini in constructor*/
    private int width;
    private int height;
    private int[] myPx = null;

    private static ColorModel cM = ColorModel.getRGBdefault();
    private static int MY_BLACK = Color.black.getRGB();
    private static int MY_WHITE = Color.white.getRGB();


    /*从文件中读入并转化为灰度图*/
    public MyImage(File file) {

        try {
            String probeType = Files.probeContentType(Paths.get(file.getPath()));
            if (probeType != null) {
                int slash = probeType.indexOf('/');
                //String type = probeType.substring(0, slash);
                this.format = probeType.substring(slash + 1);
                this.image = ImageIO.read(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        getAddition();
        this.myPx = image.getRGB(0, 0, width, height, null, 0, width);
        BufferedImage imgDone = new BufferedImage(width, height, image.getType());
        for (int i = 0, r, g, b, tmp, gray; i < myPx.length; i++) {
            tmp = myPx[i];
            r = cM.getRed(tmp);
            g = cM.getGreen(tmp);
            b = cM.getBlue(tmp);
            if (r == g && g == b) {
                continue;
            }
            gray = (int) (0.299 * r + 0.587 * g
                    + 0.114 * b);
            myPx[i] = MyUtils.toRGBInt(gray, gray, gray);
        }
        imgDone.setRGB(0, 0, width, height, myPx, 0, width);
        this.image = imgDone;
    }

    public MyImage(int[] pxs, int width, int height, int type, String format) {
        this.myPx = pxs;
        this.image = new BufferedImage(width, height, type);
        this.image.setRGB(0, 0, width, height, pxs, 0, width);
        this.format = format;
        getAddition();
    }
    /*保存文件*/
    public void saveMe(File f) {
        try {
            ImageIO.write(this.image, format, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*克隆对象*/
    public MyImage myClone() {
        int[] newPx = myPx;
        return new MyImage(newPx, width, height, image.getType(), format);
    }

    private void getAddition() {
        /*addition*/
        this.width = image.getWidth();
        this.height = image.getHeight();

    }

    /*采样*/
    public MyImage doSample(int div) {
        if (div < 1 || div > 256) {
            return this;
        }
        int xE, yE;
        int[] newPx = new int[myPx.length];
        for (int i = 0; i < width; i += div) {
            for (int j = 0; j < height; j += div) {
                xE = (i + div > width) ? width - i : div;
                yE = (j + div > height) ? height - j : div;
                int r = 0, g = 0, b = 0, tmp;
                tmp = myPx[(j + yE / 2) * width + xE / 2 + i];
                r += cM.getRed(tmp);
                g += cM.getGreen(tmp);
                b += cM.getBlue(tmp);
                int avgC = MyUtils.toRGBInt(r, g, b);
                for (int x = 0; x < xE; x++) {
                    for (int y = 0; y < yE; y++) {
                        newPx[(j + y) * width + x + i] = avgC;
                    }
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }
    /*量化*/
    public MyImage doQuantify(int level) {
        if (level < 1 || level > 256) {
            return this;
        }
        int div = 256 / level;
        int[] newPx = new int[myPx.length];
        for (int i = 0, r, g, b, tmp; i < myPx.length; i++) {
            tmp = myPx[i];
            r = cM.getRed(tmp) / div * div;
            g = cM.getGreen(tmp) / div * div;
            b = cM.getBlue(tmp) / div * div;

            newPx[i] = MyUtils.toRGBInt(r, g, b);
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }

    public WritableImage toFXImage() {
        return SwingFXUtils.toFXImage(this.image, null);
    }

    /*获取位平面*/
    public MyImage[] getBitmapPane() {
        int[][] bitmapPanePxs = new int[8][myPx.length];
        MyImage[] myImage_bitmapPanes = new MyImage[8];
        byte base = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < myPx.length; j++) {
                byte b = (byte) cM.getRed(myPx[j]);
                bitmapPanePxs[i][j] = (b & base) == 0 ? MY_WHITE : MY_BLACK;
            }
            myImage_bitmapPanes[i] = new MyImage(bitmapPanePxs[i],
                    this.width, this.height, this.image.getType(), this.format);
            base <<= 1;
        }
        return myImage_bitmapPanes;
    }
    /*获取灰度直方图*/
    public int[] getGrayHistogram() {
        int[] grayH = new int[256];
        for (int tmp : myPx) {
            grayH[cM.getRed(tmp)]++;
        }
        return grayH;
    }
    /*点运算*/
    public MyImage pointOperation(int g1_old, int g1_new, int g2_old, int g2_new) {
        if (g1_old > g2_old || !inRange(
                new int[]{g1_old, g1_new, g2_old, g2_new,}, 0, 255)) {
            return null;
        }

        int[] newPx = new int[myPx.length];
        double k1 = g1_old == 0 ? 0 : ((double) g1_new) / g1_old;
        double k2 = g2_old == g1_old ? 0 : ((double) (g2_new - g1_new)) / (g2_old - g1_old);
        double k3 = g2_old == 255 ? 0 : ((double) (255 - g2_new) / (255 - g2_old));
        int gray;
        for (int i = 0; i < myPx.length; i++) {
            gray = cM.getRed(myPx[i]);
            if (gray <= g1_old) {
                if (g1_old == 0) {
                    //边界
                    newPx[i] = MyUtils.toRGBInt(g1_new);
                } else {
                    newPx[i] = MyUtils.toRGBInt((int) (k1 * gray));
                }
            } else if (gray < g2_old) {

                newPx[i] = MyUtils.toRGBInt((int) (g1_new + k2 * (gray - g1_old)));
            } else {
                if (g2_old == 255) {
                    newPx[i] = MyUtils.toRGBInt(g2_new);
                } else {

                    newPx[i] = MyUtils.toRGBInt((int) (g2_new + k3 * (gray - g2_old)));
                }
            }
        }

        return new MyImage(newPx, width, height, image.getType(), format);
    }
    /*直方图均衡化*/
    public MyImage histogramEqualization() {
        int[] grayH = getGrayHistogram();
        double[] pro_old = new double[grayH.length];
        pro_old[0] = grayH[0] / myPx.length;
        for (int i = 1; i < pro_old.length; i++) {
            pro_old[i] = ((double) grayH[i]) / myPx.length;
            pro_old[i] += pro_old[i - 1];
        }
        int[] newIndex = new int[pro_old.length];
        for (int i = 0; i < newIndex.length; i++) {
            newIndex[i] = (int) (pro_old[i] * 255);
        }
        int[] newPx = new int[myPx.length];
        for (int i = 0; i < newPx.length; i++) {
            newPx[i] = MyUtils.toRGBInt(newIndex[cM.getRed(myPx[i])]);
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }

    private boolean inRange(int x, int a, int b) {
        return x >= a && x <= b;
    }

    private boolean inRange(int[] xS, int a, int b) {
        for (int x : xS) {
            if (!inRange(x, a, b)) {
                return false;
            }
        }
        return true;
    }
    /*获取灰度信息*/
    public GrayInfo getGrayInfo() {
        return new GrayInfo(myPx);
    }

    public static class GrayInfo {
        private double avgGray;
        private int medianGray;
        private int pxAmount;
        private double standardDeviation;

        public GrayInfo(int[] px) {
            int[] grayPx = new int[px.length];
            for (int i = 0; i < px.length; i++) {
                grayPx[i] = cM.getRed(px[i]);
            }
            this.pxAmount = px.length;
            this.avgGray = MyUtils.getAverage(grayPx);
            this.medianGray = MyUtils.getMedian(grayPx);
            this.standardDeviation = MyUtils.getStandardDeviation(grayPx);
        }

        public double getAvgGray() {
            return avgGray;
        }

        public double getMedianGray() {
            return medianGray;
        }

        public int getPxAmount() {
            return pxAmount;

        }

        public double getStandardDeviation() {
            return standardDeviation;
        }

    }
    /*二值化*/
    public MyImage getBinary(int threshold) {
        int[] binaryPx = new int[myPx.length];
        for (int i = 0; i < myPx.length; i++) {
            binaryPx[i] = cM.getRed(myPx[i]) > threshold ? MY_WHITE : MY_BLACK;
        }
        return new MyImage(binaryPx, this.width, this.height,
                image.getType(), this.format);
    }
    /*bmp2txt*/
    public String bmp2txt() {
        String grayString = "#+:.";
        char[] grayChar = grayString.toCharArray();
        int div = 256 / grayString.length();
        StringBuilder txt = new StringBuilder();
        for (int i = 0, gray; i < myPx.length; i++) {
            gray = cM.getRed(myPx[i]);
            char c = grayChar[gray / div];
            txt.append(c).append(c);
            if (i % width == 0 && i != 0) {
                txt.append('\n');
            }
        }
        return txt.toString();

    }
    /*平滑——均值*/
    public MyImage smooth_e() {
        int[] newPx = new int[myPx.length];
        int index;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                index = j * width + i;
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    newPx[index] = myPx[index];
                } else {
                    int sum = 0;
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            sum += cM.getRed(myPx[l * width + k]);
                        }
                    }
                    newPx[index] = MyUtils.toRGBInt(sum / 9);
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }
    /*平滑——K邻域*/
    public MyImage smooth_k() {
        int[] newPx = new int[myPx.length];
        int index;
        int[] neighborhood = new int[9];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                index = j * width + i;
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    newPx[index] = myPx[index];
                } else {
                    int nIndex = 0;
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            neighborhood[nIndex++] = cM.getRed(myPx[l * width + k]);
                        }
                    }
                    newPx[index] = MyUtils.toRGBInt(
                            getNeighborhoodAvg(
                                    neighborhood, cM.getRed(myPx[index]), 5));
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);

    }
    /*平滑——中值*/
    public MyImage smooth_m() {
        int[] newPx = new int[myPx.length];
        int index;
        int[] neighborhood = new int[9];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                index = j * width + i;
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    newPx[index] = myPx[index];
                } else {
                    int nIndex = 0;
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            neighborhood[nIndex++] = cM.getRed(myPx[l * width + k]);
                        }
                    }
                    Arrays.sort(neighborhood);
                    newPx[index] = MyUtils.toRGBInt(neighborhood[4]);
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }

    private int getNeighborhoodAvg(int[] neighborhood, int x, int k) {
        Arrays.sort(neighborhood);
        int index = Arrays.binarySearch(neighborhood, x);
        int sum = 0;
        for (int i = 0, a = index, b = index; i < k; i++) {
            if (neighborhood[a] - x < neighborhood[b] - x) {
                sum += neighborhood[a];
                if (a - 1 >= 0) {
                    a--;
                }
            } else {
                sum += neighborhood[b];
                if (b + 1 < neighborhood.length) {
                    b++;
                }
            }
        }

        return ((int) Math.round(((double) sum) / k));
    }
    /*锐化*/
    public MyImage sharpen(int sharpenType) {
        MyMatrix dx_sobel = new MyMatrix(new double[]{
                1, 0, 1,
                2, 0, 2,
                1, 0, 1,

        });
        MyMatrix dy_sobel = new MyMatrix(new double[]{
                1, 2, 1,
                0, 0, 0,
                1, 2, 1,
        });
        MyMatrix dx_prewitt = new MyMatrix(new double[]{
                1, 0, 1,
                1, 0, 1,
                1, 0, 1,
        });
        MyMatrix dy_prewitt = new MyMatrix(new double[]{
                1, 1, 1,
                0, 0, 0,
                1, 1, 1
        });
        MyMatrix dx_isotropic = new MyMatrix(new double[]{
                1, 0, 1,
                1.414, 0, 1.414,
                1, 0, 1,
        });
        MyMatrix dy_isotropic = new MyMatrix(new double[]{
                1, 1.414, 1,
                0, 0, 0,
                1, 1.414, 1,
        });
        int[] newPx = new int[myPx.length];
        int index;
        int[] neighborhood = new int[9];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                index = i * width + j;
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    newPx[index] = myPx[index];
                } else {
                    int nIndex = 0;
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            neighborhood[nIndex++] = cM.getRed(myPx[k * width + l]);
                        }
                    }
                    int gray = 0;
                    switch (sharpenType) {
                        case 1:
                            gray = (int) sharpenOperator(
                                    dx_sobel.getConvolution(neighborhood),
                                    dy_sobel.getConvolution(neighborhood));
                            break;
                        case 2:
                            gray = (int) sharpenOperator(
                                    dx_prewitt.getConvolution(neighborhood),
                                    dy_prewitt.getConvolution(neighborhood));
                            break;
                        case 3:
                            gray = (int) sharpenOperator(
                                    dx_isotropic.getConvolution(neighborhood),
                                    dy_isotropic.getConvolution(neighborhood));
                            break;

                    }
                    if (gray > 255) {
                        gray = 255;
                    } else if (gray < 0) {
                        gray = 0;
                    }
                    newPx[index] = MyUtils.toRGBInt(gray);
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);

    }

    private double sharpenOperator(double dx, double dy) {
        return Math.sqrt(dx * dx + dy * dy);

    }
    /*平移旋转缩放*/
    public MyImage matrixTransform(MyMatrix myMatrix) {
        int[] newPx = new int[myPx.length];
        int index;
        MyPoint myPoint = new MyPoint(0, 0, 1);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                index = i * width + j;
                myPoint.setXYZ(j, i, 1).multiplication(myMatrix);
                if (inRange(myPoint.getX(), 0, width - 1) && inRange(myPoint.getY(), 0, height - 1)) {
                    newPx[myPoint.getY() * width + myPoint.getX()] = myPx[index];
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);

    }
    /*膨胀*/
    public MyImage expand(int[][] mask){
        int[] newPx = new int[myPx.length];
        int mh = mask.length;
        int mw = mask[1].length;
        int sh = (mh+1)/2;
        int sw = (mw+1)/2;
        for(int i=(mh-1)/2+1;i<height-(mh-1)/2;i++){
            for(int j=(mw-1)/2+1;j<width-(mw-1)/2;j++){
                newPx[j + i * width] = MY_WHITE;
                for(int m=0; m<mh ; m++){
                    for(int n=0;n<mw;n++){
                        if((mask[m][n] & cM.getRed(this.myPx[j+n-sw +(i+m-sh)*width]))==0){
                            newPx[j + i * width] = MY_BLACK;
                            break;
                        }
                    }
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }
    /*腐蚀*/
    public MyImage erosion(int[][] mask){
        int[] newPx = new int[myPx.length];
        int mh = mask.length;
        int mw = mask[1].length;
        int sh = (mh+1)/2;
        int sw = (mw+1)/2;

        for(int i=(mh-1)/2+1;i<height-(mh-1)/2;i++){
            for(int j=(mw-1)/2+1;j<width-(mw-1)/2;j++){
                newPx[j + i * width] = MY_BLACK;
                for(int m=0; m<mh ; m++){
                    for(int n=0;n<mw;n++){
                        if((mask[m][n] & cM.getRed(this.myPx[j+n-sw +(i+m-sh)*width]))!=0){
                            newPx[j + i * width] = MY_WHITE;
                            break;
                        }
                    }
                }
            }
        }
        return new MyImage(newPx, width, height, image.getType(), format);
    }
}
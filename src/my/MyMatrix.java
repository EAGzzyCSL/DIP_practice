package my;



import java.util.Arrays;

public class MyMatrix {
    private double[] myDouble;
    public MyMatrix(double[] myDouble){
        this.myDouble=myDouble;
    }
    public double getConvolution(int[] neighborhood){

        double sum=0;
        for(int i=0;i<myDouble.length;i++){
            sum+=myDouble[i]*neighborhood[i];
        }
        return sum/myDouble.length;
    }
    public double[] getMyDouble(){
        return this.myDouble;
    }
    public MyMatrix postTranslate(double dx, double dy){


        return this.preMultiply(new MyMatrix(new double[]{
                1,0,dx,
                0,1,dy,
                0,0,1,
        }));
    }
    public MyMatrix postScale(double sx, double sy){
        return this.preMultiply(new MyMatrix(new double[]{
                sx,0,0,
                0,sy,0,
                0,0,1,
        }));
    }
    public MyMatrix postRotate(double radian) {
        return this.preMultiply(new MyMatrix(new double[]{
                Math.cos(radian),Math.sin(radian),0,
                -Math.sin(radian),Math.cos(radian),0,
                0,0,1,
        }));
    }
    private MyMatrix preMultiply(MyMatrix matrix){
        double[] doublesMatrix=matrix.getMyDouble();
        double[] newDouble=new double[9];
        int index;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                index=i*3+j;
                newDouble[index]=0;
                for(int k=0;k<3;k++){
                    newDouble[index]+=doublesMatrix[k*3+j]*myDouble[i*3+k];
                }
            }
        }
        this.myDouble=newDouble;
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(myDouble);
    }
}

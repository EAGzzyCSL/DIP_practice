package my;


public class MyPoint {
    private int x;
    private int y;
    private int z;
    public MyPoint(int x,int y,int z){
        setXYZ(x,y,z);
    }
    public MyPoint setXYZ(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
        return this;
    }
    public int getX(){
        return x;

    }
    public int  getY(){
        return y;
    }
    public void multiplication(MyMatrix myMatrix){
        double[] myDouble=myMatrix.getMyDouble();
        double xd=myDouble[0]*x+myDouble[1]*y+myDouble[2]*z;
        double yd=myDouble[3]*x+myDouble[4]*y+myDouble[5]*z;
        double zd=myDouble[6]*x+myDouble[7]*y+myDouble[8]*z;
        this.x=(int)Math.round(xd);
        this.y=(int) Math.round(yd);
        this.z=(int)Math.round(zd);

    }
}

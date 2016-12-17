package my;


public class TabInfo {
    private String tabTitle;
    private String fxmlFileName;
    public TabInfo(String tabTitle,String fxmlFileName){
        this.tabTitle=tabTitle;
        this.fxmlFileName=fxmlFileName;
    }
    public String getTabTitle(){
        return this.tabTitle;
    }
    public String getFxmlFileName(){
        return this.fxmlFileName;
    }
}

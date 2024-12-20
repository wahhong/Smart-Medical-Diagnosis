package model;

public class Services {
    private int servicesID;
    private String servicesName;
    private String servicesDesc;
    private Double servicesPrice;
    private byte[] servicesImage;
    private String createDate;
    private int servicesAi;
    private int servicesStatus;
    private int isDelete;

    public Services() {
    }

    public Services(int servicesID, String servicesName, String servicesDesc, Double servicesPrice, byte[] servicesImage, String createDate, int servicesAi, int servicesStatus, int isDelete) {
        this.servicesID = servicesID;
        this.servicesName = servicesName;
        this.servicesDesc = servicesDesc;
        this.servicesPrice = servicesPrice;
        this.servicesImage = servicesImage;
        this.createDate = createDate;
        this.servicesAi = servicesAi;
        this.servicesStatus = servicesStatus;
        this.isDelete = isDelete;
    }

    public int getServicesID() {
        return servicesID;
    }

    public void setServicesID(int servicesID) {
        this.servicesID = servicesID;
    }

    public String getServicesName() {
        return servicesName;
    }

    public void setServicesName(String servicesName) {
        this.servicesName = servicesName;
    }

    public String getServicesDesc() {
        return servicesDesc;
    }

    public void setServicesDesc(String servicesDesc) {
        this.servicesDesc = servicesDesc;
    }

    public Double getServicesPrice() {
        return servicesPrice;
    }

    public void setServicesPrice(Double servicesPrice) {
        this.servicesPrice = servicesPrice;
    }

    public byte[] getServicesImage() {
        return servicesImage;
    }

    public void setServicesImage(byte[] servicesImage) {
        this.servicesImage = servicesImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getServicesAi() {
        return servicesAi;
    }

    public void setServicesAi(int servicesAi) {
        this.servicesAi = servicesAi;
    }
    
    public int getServicesStatus() {
        return servicesStatus;
    }

    public void setServicesStatus(int servicesStatus) {
        this.servicesStatus = servicesStatus;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}

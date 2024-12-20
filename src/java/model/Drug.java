package model;

public class Drug {
    private int drugID;
    private String drugName;
    private String drugDesc;
    private int drugQuantity;
    private double drugPrice;
    private String drugDate;
    private String drugExpire;
    private int drugStatus;
    private int drugDelete;

    public Drug() {
    }

    public Drug(int drugID, String drugName, String drugDesc, int drugQuantity, double drugPrice, String drugDate, String drugExpire, int drugStatus, int drugDelete) {
        this.drugID = drugID;
        this.drugName = drugName;
        this.drugDesc = drugDesc;
        this.drugQuantity = drugQuantity;
        this.drugPrice = drugPrice;
        this.drugDate = drugDate;
        this.drugExpire = drugExpire;
        this.drugStatus = drugStatus;
        this.drugDelete = drugDelete;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDesc() {
        return drugDesc;
    }

    public void setDrugDesc(String drugDesc) {
        this.drugDesc = drugDesc;
    }

    public int getDrugQuantity() {
        return drugQuantity;
    }

    public void setDrugQuantity(int drugQuantity) {
        this.drugQuantity = drugQuantity;
    }

    public double getDrugPrice() {
        return drugPrice;
    }

    public void setDrugPrice(double drugPrice) {
        this.drugPrice = drugPrice;
    }

    public String getDrugDate() {
        return drugDate;
    }

    public void setDrugDate(String drugDate) {
        this.drugDate = drugDate;
    }

    public String getDrugExpire() {
        return drugExpire;
    }

    public void setDrugExpire(String drugExpire) {
        this.drugExpire = drugExpire;
    }

    public int getDrugStatus() {
        return drugStatus;
    }

    public void setDrugStatus(int drugStatus) {
        this.drugStatus = drugStatus;
    }

    public int getDrugDelete() {
        return drugDelete;
    }

    public void setDrugDelete(int drugDelete) {
        this.drugDelete = drugDelete;
    }
}

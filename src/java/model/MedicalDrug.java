package model;

public class MedicalDrug {
    private int medicalDrugID;
    private int medicalID;
    private int drugID;
    private int quantity;

    public MedicalDrug() {
    }

    public MedicalDrug(int medicalDrugID, int medicalID, int drugID, int quantity) {
        this.medicalDrugID = medicalDrugID;
        this.medicalID = medicalID;
        this.drugID = drugID;
        this.quantity = quantity;
    }

    public int getMedicalDrugID() {
        return medicalDrugID;
    }

    public void setMedicalDrugID(int medicalDrugID) {
        this.medicalDrugID = medicalDrugID;
    }

    public int getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(int medicalID) {
        this.medicalID = medicalID;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

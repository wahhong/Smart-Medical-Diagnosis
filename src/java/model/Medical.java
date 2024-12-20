package model;

public class Medical {

    private int medicalID;
    private int appointmentID;
    private double medicalFee;
    private String diagnosis;
    private String medicalFeeDesc;

    public Medical() {
    }

    public Medical(int medicalID, int appointmentID, double medicalFee, String diagnosis, String medicalFeeDesc) {
        this.medicalID = medicalID;
        this.appointmentID = appointmentID;
        this.medicalFee = medicalFee;
        this.diagnosis = diagnosis;
        this.medicalFeeDesc = medicalFeeDesc;
    }

    public int getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(int medicalID) {
        this.medicalID = medicalID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public double getMedicalFee() {
        return medicalFee;
    }

    public void setMedicalFee(double medicalFee) {
        this.medicalFee = medicalFee;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicalFeeDesc() {
        return medicalFeeDesc;
    }

    public void setMedicalFeeDesc(String medicalFeeDesc) {
        this.medicalFeeDesc = medicalFeeDesc;
    }
}

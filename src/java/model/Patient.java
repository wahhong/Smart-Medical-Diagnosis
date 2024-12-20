package model;

public class Patient {
    private int patientID;
    private String patientName;
    private byte[] patientImage;
    private String patientEmail;
    private String patientPassword;
    private String patientPhone;
    private String patientGender;
    private String patientDOB;
    private String patientRegisterDate;
    private int patientStatus;
    private String salt;

    public Patient() {
    }

    public Patient(int patientID, String patientName, byte[] patientImage, String patientEmail, String patientPassword, String patientPhone, String patientGender, String patientDOB, String patientRegisterDate, int patientStatus, String salt) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.patientImage = patientImage;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.patientPhone = patientPhone;
        this.patientGender = patientGender;
        this.patientDOB = patientDOB;
        this.patientRegisterDate = patientRegisterDate;
        this.patientStatus = patientStatus;
        this.salt = salt;
    }

    public byte[] getPatientImage() {
        return patientImage;
    }

    public void setPatientImage(byte[] patientImage) {
        this.patientImage = patientImage;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    public String getPatientRegisterDate() {
        return patientRegisterDate;
    }

    public void setPatientRegisterDate(String patientRegisterDate) {
        this.patientRegisterDate = patientRegisterDate;
    }

    public int getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(int patientStatus) {
        this.patientStatus = patientStatus;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}

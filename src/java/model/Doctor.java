package model;

public class Doctor {
    private int doctorID;
    private int servicesID;
    private String doctorName;
    private byte[] doctorImage;
    private String doctorEmail;
    private String doctorPassword;
    private String doctorRole;
    private String doctorPhone;
    private String doctorGender;
    private String doctorDOB;
    private String doctorRegisterDate;
    private int doctorStatus;
    private String salt;

    public Doctor() {
    }

    public Doctor(int doctorID, int servicesID, String doctorName, byte[] doctorImage, String doctorEmail, String doctorPassword, String doctorRole, String doctorPhone, String doctorGender, String doctorDOB, String doctorRegisterDate, int doctorStatus, String salt) {
        this.doctorID = doctorID;
        this.servicesID = servicesID;
        this.doctorName = doctorName;
        this.doctorImage = doctorImage;
        this.doctorEmail = doctorEmail;
        this.doctorPassword = doctorPassword;
        this.doctorRole = doctorRole;
        this.doctorPhone = doctorPhone;
        this.doctorGender = doctorGender;
        this.doctorDOB = doctorDOB;
        this.doctorRegisterDate = doctorRegisterDate;
        this.doctorStatus = doctorStatus;
        this.salt = salt;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getServicesID() {
        return servicesID;
    }

    public void setServicesID(int servicesID) {
        this.servicesID = servicesID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public byte[] getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(byte[] doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public String getDoctorRole() {
        return doctorRole;
    }

    public void setDoctorRole(String doctorRole) {
        this.doctorRole = doctorRole;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorGender() {
        return doctorGender;
    }

    public void setDoctorGender(String doctorGender) {
        this.doctorGender = doctorGender;
    }

    public String getDoctorDOB() {
        return doctorDOB;
    }

    public void setDoctorDOB(String doctorDOB) {
        this.doctorDOB = doctorDOB;
    }

    public String getDoctorRegisterDate() {
        return doctorRegisterDate;
    }

    public void setDoctorRegisterDate(String doctorRegisterDate) {
        this.doctorRegisterDate = doctorRegisterDate;
    }

    public int getDoctorStatus() {
        return doctorStatus;
    }

    public void setDoctorStatus(int doctorStatus) {
        this.doctorStatus = doctorStatus;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

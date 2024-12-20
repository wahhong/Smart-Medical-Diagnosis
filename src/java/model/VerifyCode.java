package model;

public class VerifyCode {
    private int codeID;
    private int patientID;
    private String code;
    private int codeStatus;
    private String codeValidTime;

    public VerifyCode() {
    }

    public VerifyCode(int codeID, int patientID, String code, int codeStatus, String codeValidTime) {
        this.codeID = codeID;
        this.patientID = patientID;
        this.code = code;
        this.codeStatus = codeStatus;
        this.codeValidTime = codeValidTime;
    }

    public int getCodeID() {
        return codeID;
    }

    public void setCodeID(int codeID) {
        this.codeID = codeID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(int codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getCodeValidTime() {
        return codeValidTime;
    }

    public void setCodeValidTime(String codeValidTime) {
        this.codeValidTime = codeValidTime;
    }
}

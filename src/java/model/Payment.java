package model;

public class Payment {

    private String paymentID;
    private String paymentIntentId;
    private int appointmentID;
    private double paymentAmount;
    private String paymentMethod;
    private String paymentDesc;
    private String paymentDate;
    private String paymentRefundDate;
    private int paymentStatus;

    public Payment() {
    }

    public Payment(String paymentID, String paymentIntentId, int appointmentID, double paymentAmount, String paymentMethod, String paymentDesc, String paymentDate, String paymentRefundDate, int paymentStatus) {
        this.paymentID = paymentID;
        this.paymentIntentId = paymentIntentId;
        this.appointmentID = appointmentID;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDesc = paymentDesc;
        this.paymentDate = paymentDate;
        this.paymentRefundDate = paymentRefundDate;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentRefundDate() {
        return paymentRefundDate;
    }

    public void setPaymentRefundDate(String paymentRefundDate) {
        this.paymentRefundDate = paymentRefundDate;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

package model;

public class Notification {
    private int notificationID;
    private int appointmentID;
    private String notificationTitle;
    private String notificationDesc;
    private int notificationStatus;

    public Notification() {
    }

    public Notification(int notificationID, int appointmentID, String notificationTitle, String notificationDesc, int notificationStatus) {
        this.notificationID = notificationID;
        this.appointmentID = appointmentID;
        this.notificationTitle = notificationTitle;
        this.notificationDesc = notificationDesc;
        this.notificationStatus = notificationStatus;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public int getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}

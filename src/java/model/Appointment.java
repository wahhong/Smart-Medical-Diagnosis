package model;

public class Appointment {
    private int appointmentID;
    private int doctorID;
    private int servicesID;
    private int patientID;
    private int diagnosisID;
    private String appointmentDate;
    private String appointmentTime;
    private int appointmentStatus;

    public Appointment() {
    }

    public Appointment(int doctorID, int servicesID, String appointmentDate, String appointmentTime) {
        this.doctorID = doctorID;
        this.servicesID = servicesID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }
    
    public Appointment(int doctorID, int servicesID, int patientID, String appointmentDate, String appointmentTime) {
        this.doctorID = doctorID;
        this.servicesID = servicesID;
        this.patientID = patientID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public Appointment(int appointmentID, int doctorID, int servicesID, int patientID, int diagnosisID, String appointmentDate, String appointmentTime, int appointmentStatus) {
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.servicesID = servicesID;
        this.patientID = patientID;
        this.diagnosisID = diagnosisID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
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

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getDiagnosisID() {
        return diagnosisID;
    }

    public void setDiagnosisID(int diagnosisID) {
        this.diagnosisID = diagnosisID;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    @Override
    public String toString() {
        return "Appointment{" + "appointmentID=" + appointmentID + ", doctorID=" + doctorID + ", servicesID=" + servicesID + ", patientID=" + patientID + ", diagnosisID=" + diagnosisID + ", appointmentDate=" + appointmentDate + ", appointmentTime=" + appointmentTime + ", appointmentStatus=" + appointmentStatus + '}';
    }
}

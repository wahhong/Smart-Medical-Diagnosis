package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;

public class AppointmentDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public AppointmentDao(Connection conn) {
        this.conn = conn;
    }

    public int getAppointmentCount() {
        query = "SELECT COUNT(*) AS totalAppointments FROM appointment";
        int appointmentCount = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                appointmentCount = rs.getInt("totalAppointments");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentCount;
    }

    public boolean updateAppointmentStatus(int appointmentID, int newStatus) {
        query = "UPDATE appointment SET appointmentStatus = ? WHERE appointmentID = ?";
        boolean isUpdated = false;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, appointmentID);

            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }

        return isUpdated;
    }

    public boolean deleteAppointment(int appointmentID) throws SQLException {
        String query = "DELETE FROM appointment WHERE appointmentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public Appointment getAppointmentById(int appointmentID) {
        String query = "SELECT * FROM appointment WHERE appointmentID = ?";
        Appointment appointment = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    appointment = new Appointment();
                    appointment.setAppointmentID(rs.getInt("appointmentID"));
                    appointment.setDoctorID(rs.getInt("doctorID"));
                    appointment.setServicesID(rs.getInt("servicesID"));
                    appointment.setPatientID(rs.getInt("patientID"));
                    appointment.setDiagnosisID(rs.getInt("diagnosisID"));
                    appointment.setAppointmentDate(rs.getString("appointmentDate"));
                    appointment.setAppointmentTime(rs.getString("appointmentTime"));
                    appointment.setAppointmentStatus(rs.getInt("appointmentStatus"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment by ID: " + e.getMessage());
        }

        return appointment;
    }

    public List<Appointment> getAllAppointment() {
        String query = "SELECT * FROM appointment";
        List<Appointment> appointmentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(rs.getInt("appointmentID"));
                appointment.setDoctorID(rs.getInt("doctorID"));
                appointment.setServicesID(rs.getInt("servicesID"));
                appointment.setPatientID(rs.getInt("patientID"));
                appointment.setDiagnosisID(rs.getInt("diagnosisID"));
                appointment.setAppointmentDate(rs.getString("appointmentDate"));
                appointment.setAppointmentTime(rs.getString("appointmentTime"));
                appointment.setAppointmentStatus(rs.getInt("appointmentStatus"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
        return appointmentList;
    }

    public List<Appointment> getAllAppointmentPatient(int patientID) {
        String query = "SELECT * FROM appointment WHERE patientID = ?";
        List<Appointment> appointmentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, patientID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentID(rs.getInt("appointmentID"));
                    appointment.setDoctorID(rs.getInt("doctorID"));
                    appointment.setServicesID(rs.getInt("servicesID"));
                    appointment.setPatientID(rs.getInt("patientID"));
                    appointment.setDiagnosisID(rs.getInt("diagnosisID"));
                    appointment.setAppointmentDate(rs.getString("appointmentDate"));
                    appointment.setAppointmentTime(rs.getString("appointmentTime"));
                    appointment.setAppointmentStatus(rs.getInt("appointmentStatus"));
                    appointmentList.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    public List<Appointment> getAllAppointmentDoctor(int doctorID) {
        String query = "SELECT * FROM appointment WHERE doctorID = ?";
        List<Appointment> appointmentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentID(rs.getInt("appointmentID"));
                    appointment.setDoctorID(rs.getInt("doctorID"));
                    appointment.setServicesID(rs.getInt("servicesID"));
                    appointment.setPatientID(rs.getInt("patientID"));
                    appointment.setDiagnosisID(rs.getInt("diagnosisID"));
                    appointment.setAppointmentDate(rs.getString("appointmentDate"));
                    appointment.setAppointmentTime(rs.getString("appointmentTime"));
                    appointment.setAppointmentStatus(rs.getInt("appointmentStatus"));
                    appointmentList.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    public List<Appointment> getAppointmentTime(int doctorID, String appointmentDate) {
        String query = "SELECT * FROM appointment WHERE doctorID = ? AND appointmentDate = ? AND appointmentStatus IN (0, 1)";
        List<Appointment> appointmentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID);
            ps.setString(2, appointmentDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentTime(rs.getString("appointmentTime"));
                    appointmentList.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    public int insertAppointment(int doctorID, int servicesID, int patientID, int diagnosisID, String appointmentDate, String appointmentTime, int appointmentStatus) {
        String checkQuery = "SELECT COUNT(*) FROM appointment WHERE doctorID = ? AND appointmentDate = ? AND appointmentTime = ? AND appointmentStatus != 2";
        String insertQuery = "INSERT INTO appointment (doctorID, servicesID, patientID, diagnosisID, appointmentDate, appointmentTime, appointmentStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement checkPs = conn.prepareStatement(checkQuery)) {
            checkPs.setInt(1, doctorID);
            checkPs.setString(2, appointmentDate);
            checkPs.setString(3, appointmentTime);

            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        try (PreparedStatement insertPs = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertPs.setInt(1, doctorID);
            insertPs.setInt(2, servicesID);
            insertPs.setInt(3, patientID);
            if (diagnosisID == 0) {
                insertPs.setNull(4, java.sql.Types.INTEGER);
            } else {
                insertPs.setInt(4, diagnosisID);
            }
            insertPs.setString(5, appointmentDate);
            insertPs.setString(6, appointmentTime);
            insertPs.setInt(7, appointmentStatus);

            int rowsInserted = insertPs.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = insertPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}

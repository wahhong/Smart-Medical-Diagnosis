package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Medical;

public class MedicalDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public MedicalDao(Connection conn) {
        this.conn = conn;
    }
    
    public boolean deleteMedical(int medicalID) {
        String query = "DELETE FROM medical WHERE medicalID = ?";
        boolean isDeleted = false;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicalID);

            int rowsDeleted = ps.executeUpdate();
            isDeleted = rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    public Medical getMedicalByAppointmentID(int appointmentID) {
        query = "SELECT * FROM medical WHERE appointmentID = ?";
        Medical medical = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    medical = new Medical();
                    medical.setMedicalID(rs.getInt("medicalID"));
                    medical.setAppointmentID(rs.getInt("appointmentID"));
                    medical.setMedicalFeeDesc(rs.getString("medicalFeeDesc"));
                    medical.setMedicalFee(rs.getDouble("medicalFee"));
                    medical.setDiagnosis(rs.getString("diagnosis"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medical;
    }

    public int insertMedical(int appointmentID, String medicalFeeDesc, double medicalFee, String diagnosis) {
        String query = "INSERT INTO medical (appointmentID, medicalFeeDesc, medicalFee, diagnosis) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, appointmentID);
            ps.setString(2, medicalFeeDesc);
            ps.setDouble(3, medicalFee);
            ps.setString(4, diagnosis);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }
}

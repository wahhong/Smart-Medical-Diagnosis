package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.AiDiagnosis;

public class AiDiagnosisDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public AiDiagnosisDao(Connection conn) {
        this.conn = conn;
    }

    public int insertAiDiagnosis(String symptom1, String symptom2, String symptom3, String symptom4, String symptom5, String primaryDiagnosis) {
        String query = "INSERT INTO aidiagnosis (symptom1, symptom2, symptom3, symptom4, symptom5, primaryDiagnosis) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set the parameters for the insert statement
            ps.setString(1, symptom1);
            ps.setString(2, symptom2);
            ps.setString(3, symptom3);
            ps.setString(4, symptom4);
            ps.setString(5, symptom5);
            ps.setString(6, primaryDiagnosis);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public AiDiagnosis selectAiDiagnosisById(int diagnosisID) {
        query = "SELECT * FROM aidiagnosis WHERE diagnosisID = ?";
        AiDiagnosis aiDiagnosis = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, diagnosisID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    aiDiagnosis = new AiDiagnosis();
                    aiDiagnosis.setDiagnosisID(rs.getInt("diagnosisID"));
                    aiDiagnosis.setSymptom1(rs.getString("symptom1"));
                    aiDiagnosis.setSymptom2(rs.getString("symptom2"));
                    aiDiagnosis.setSymptom3(rs.getString("symptom3"));
                    aiDiagnosis.setSymptom4(rs.getString("symptom4"));
                    aiDiagnosis.setSymptom5(rs.getString("symptom5"));
                    aiDiagnosis.setPrimaryDiagnosis(rs.getString("primaryDiagnosis"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aiDiagnosis;
    }
}

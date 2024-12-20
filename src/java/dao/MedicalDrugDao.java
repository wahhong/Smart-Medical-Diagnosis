package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MedicalDrug;

public class MedicalDrugDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public MedicalDrugDao(Connection conn) {
        this.conn = conn;
    }

    public List<MedicalDrug> getMedicalDrugsByMedicalID(int medicalID) {
        String query = "SELECT * FROM medicaldrug WHERE medicalID = ?";
        List<MedicalDrug> medicalDrugs = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicalID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalDrug medicalDrug = new MedicalDrug();
                    medicalDrug.setMedicalDrugID(rs.getInt("medicalDrugID"));
                    medicalDrug.setMedicalID(rs.getInt("medicalID"));
                    medicalDrug.setDrugID(rs.getInt("drugID"));
                    medicalDrug.setQuantity(rs.getInt("quantity"));
                    medicalDrugs.add(medicalDrug);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicalDrugs;
    }

    public boolean insertMedicalDrug(int medicalID, int drugID, int quantity) {
        String query = "INSERT INTO medicaldrug (medicalID, drugID, quantity) VALUES (?, ?, ?)";
        boolean isInserted = false;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicalID);
            ps.setInt(2, drugID);
            ps.setInt(3, quantity);

            int rowsInserted = ps.executeUpdate();
            isInserted = rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isInserted;
    }

    public boolean deleteMedicalDrug(int medicalID) {
        String query = "DELETE FROM medicaldrug WHERE medicalID = ?";
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
}

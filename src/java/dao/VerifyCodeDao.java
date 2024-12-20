package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VerifyCodeDao {

    private Connection conn = null;

    public VerifyCodeDao(Connection conn) {
        this.conn = conn;
    }

    public boolean insertOrUpdateVerifyCode(int patientID, String code) {
        boolean result = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String querySelect = "SELECT * FROM verifycode WHERE patientID = ?";
        String queryUpdate = "UPDATE verifycode SET code = ?, codeStatus = ?, codeValidTime = ? WHERE patientID = ?";
        String queryInsert = "INSERT INTO verifycode (patientID, code, codeStatus, codeValidTime) VALUES (?, ?, ?, ?)";

        try {
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            ps = conn.prepareStatement(querySelect);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement(queryUpdate);
                ps.setString(1, code);
                ps.setInt(2, 1);
                ps.setString(3, formattedDateTime);
                ps.setInt(4, patientID);
            } else {
                ps = conn.prepareStatement(queryInsert);
                ps.setInt(1, patientID);
                ps.setString(2, code);
                ps.setInt(3, 1);
                ps.setString(4, formattedDateTime);
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public boolean verifyCode(int patientID, String code) {
        boolean result = false;
        PreparedStatement ps = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;

        String query = "SELECT * FROM verifycode WHERE patientID = ? AND code = ? AND codeStatus = 1 AND codeValidTime >= NOW() - INTERVAL 5 MINUTE";
        String queryUpdate = "UPDATE verifycode SET codeStatus = 0 WHERE patientID = ? AND code = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            ps.setString(2, code);

            rs = ps.executeQuery();

            if (rs.next()) {
                psUpdate = conn.prepareStatement(queryUpdate);
                psUpdate.setInt(1, patientID);
                psUpdate.setString(2, code);
                psUpdate.executeUpdate();
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}

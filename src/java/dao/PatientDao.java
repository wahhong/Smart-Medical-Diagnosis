package dao;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import model.Patient;

public class PatientDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public PatientDao(Connection conn) {
        this.conn = conn;
    }

    public int getPatientCount() {
        query = "SELECT COUNT(*) AS totalPatients FROM patient WHERE isDelete = 0";
        int patientCount = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                patientCount = rs.getInt("totalPatients");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientCount;
    }

    public boolean updatePatientDetails(int patientID, String patientName, String patientEmail, String patientPhone, String patientGender, String patientDOB, int patientStatus) {
        PreparedStatement checkPs = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;

        // Check if the new email already exists in the database (excluding the current patient's email)
        String checkEmailQuery = "SELECT * FROM patient WHERE patientEmail = ? AND patientID != ?";
        String updateQuery = "UPDATE patient SET patientName = ?, patientEmail = ?, patientPhone = ?, patientGender = ?, patientDOB = ?, patientStatus = ? WHERE patientID = ?";

        try {
            // Check if the email already exists
            checkPs = this.conn.prepareStatement(checkEmailQuery);
            checkPs.setString(1, patientEmail);
            checkPs.setInt(2, patientID);
            rs = checkPs.executeQuery();

            if (rs != null && rs.next()) {
                // If email exists, print an error message and return false
                System.out.println("Email already exists.");
                return false;
            } else {
                // Prepare to update the patient details (excluding password)
                updatePs = this.conn.prepareStatement(updateQuery);
                updatePs.setString(1, patientName);
                updatePs.setString(2, patientEmail);
                updatePs.setString(3, patientPhone);
                updatePs.setString(4, patientGender);
                updatePs.setString(5, patientDOB);
                updatePs.setInt(6, patientStatus);
                updatePs.setInt(7, patientID);

                // Execute the update statement
                int rowsUpdated = updatePs.executeUpdate();

                // If rows were updated, return true
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            System.out.println("Exception during update: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (checkPs != null) {
                    checkPs.close();
                }
                if (updatePs != null) {
                    updatePs.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean deletePatient(int patientID) throws SQLException {
        String query = "UPDATE patient SET isDelete = 1, patientStatus = 0 WHERE patientID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, patientID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public boolean insertPatient(String name, String gender, String email, String dob, String phone) throws SQLException {
        String query = "INSERT INTO patient (patientName, patientGender, patientEmail, patientDOB, patientPhone, patientStatus) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, email);
            ps.setString(4, dob);
            ps.setString(5, phone);
            ps.setInt(6, 0);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public List<Patient> getAllPatients() {
        query = "SELECT * FROM patient WHERE isDelete = 0";
        List<Patient> patientsList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientID(rs.getInt("patientID"));
                patient.setPatientName(rs.getString("patientName"));
                patient.setPatientEmail(rs.getString("patientEmail"));
                patient.setPatientImage(rs.getBytes("patientImage"));
                patient.setPatientPassword(rs.getString("patientPassword"));
                patient.setPatientPhone(rs.getString("patientPhone"));
                patient.setPatientGender(rs.getString("patientGender"));
                patient.setPatientDOB(rs.getString("patientDOB"));
                patient.setPatientRegisterDate(rs.getString("patientRegisterDate"));
                patient.setPatientStatus(rs.getInt("patientStatus"));
                patient.setSalt(rs.getString("salt"));

                patientsList.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientsList;
    }

    public Patient getPatient(int patientID) {
        PreparedStatement ps = null;
        query = "SELECT * FROM patient WHERE patientID = ?";
        Patient patient = null;

        try {
            ps = this.conn.prepareStatement(query);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                patient = new Patient();
                patient.setPatientID(rs.getInt("patientID"));
                patient.setPatientName(rs.getString("patientName"));
                patient.setPatientEmail(rs.getString("patientEmail"));
                patient.setPatientImage(rs.getBytes("patientImage"));
                patient.setPatientPassword(rs.getString("patientPassword"));
                patient.setPatientPhone(rs.getString("patientPhone"));
                patient.setPatientGender(rs.getString("patientGender"));
                patient.setPatientDOB(rs.getString("patientDOB"));
                patient.setPatientRegisterDate(rs.getString("patientRegisterDate"));
                patient.setPatientStatus(rs.getInt("patientStatus"));
                patient.setSalt(rs.getString("salt"));
            }

        } catch (SQLException e) {
            System.out.println("Exception during patient retrieval: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return patient;
    }

    public int getPatientID(String patientEmail) {
        PreparedStatement ps = null;
        int patientID = -1;
        String query = "SELECT * FROM patient WHERE patientEmail = ?";

        try {
            ps = this.conn.prepareStatement(query);
            ps.setString(1, patientEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                patientID = rs.getInt("patientID");
            } else {
                patientID = -1;
            }

        } catch (SQLException e) {
            System.out.println("Exception during login: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return patientID;
    }

    public boolean updatePatientImage(int patientID, Part imagePart) throws SQLException {
        if (imagePart == null) {
            Logger.getLogger(PatientDao.class.getName()).log(Level.WARNING, "Image part is null.");
            return false;
        }

        String query = "UPDATE patient SET patientImage = ? WHERE patientID = ?";
        try (
                InputStream patientImage = imagePart.getInputStream(); PreparedStatement ps = this.conn.prepareStatement(query)) {
            ps.setBinaryStream(1, patientImage);
            ps.setInt(2, patientID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (IOException ex) {
            Logger.getLogger(PatientDao.class.getName()).log(Level.SEVERE, "Error processing image input stream", ex);
            return false;
        }
    }

    public String getPatientEmail(int patientID) {
        PreparedStatement ps = null;
        String query = "SELECT * FROM patient WHERE patientID = ?";
        String patientEmail = "";

        try {
            ps = this.conn.prepareStatement(query);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                patientEmail = rs.getString("patientEmail");
            } else {
                patientEmail = "";
            }

        } catch (SQLException e) {
            System.out.println("Exception during login: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return patientEmail;
    }

    public Patient patientLogin(String patientEmail, String patientPassword) {
        Patient patient = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM patient WHERE patientEmail = ? AND patientStatus = 1";

        try {
            ps = this.conn.prepareStatement(query);
            ps.setString(1, patientEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("patientPassword");
                String storedSalt = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(storedSalt);
                String enteredHashedPassword = hashPassword(patientPassword, salt);

                if (storedHashedPassword.equals(enteredHashedPassword)) {
                    patient = new Patient();
                    patient.setPatientID(rs.getInt("patientID"));
                }
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Exception during login: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return patient;
    }

    public int patientRegister(String patientName, String patientEmail, String patientPassword, String patientPhone, String patientGender, String patientDOB) {
        PreparedStatement checkPs = null;
        PreparedStatement insertPs = null;
        ResultSet rs = null;
        ResultSet generatedKeys = null;

        String checkEmailQuery = "SELECT * FROM patient WHERE patientEmail = ?";
        String query = "INSERT INTO patient (patientName, patientEmail, patientPassword, salt, patientPhone, patientGender, patientDOB) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            checkPs = this.conn.prepareStatement(checkEmailQuery);
            checkPs.setString(1, patientEmail);
            rs = checkPs.executeQuery();

            if (rs != null && rs.next()) {
                System.out.println("Email already exists.");
                return -1;
            } else {
                byte[] salt = getSalt();
                String hashedPassword = hashPassword(patientPassword, salt);
                String saltString = Base64.getEncoder().encodeToString(salt);

                insertPs = this.conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                insertPs.setString(1, patientName);
                insertPs.setString(2, patientEmail);
                insertPs.setString(3, hashedPassword);
                insertPs.setString(4, saltString);
                insertPs.setString(5, patientPhone);
                insertPs.setString(6, patientGender);
                insertPs.setString(7, patientDOB);

                int rowsInserted = insertPs.executeUpdate();

                if (rowsInserted > 0) {
                    generatedKeys = insertPs.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
                return -1;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Exception during registration: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (checkPs != null) {
                    checkPs.close();
                }
                if (insertPs != null) {
                    insertPs.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean setPatientPassword(int patientID, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "UPDATE patient SET patientPassword = ?, salt = ?, patientStatus = 1 WHERE patientID = ?";
        String selectQuery = "SELECT salt FROM patient WHERE patientID = ?";

        try {
            ps = this.conn.prepareStatement(selectQuery);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                byte[] salt = getSalt();
                String hashedPassword = hashPassword(password, salt);
                String saltString = Base64.getEncoder().encodeToString(salt);

                ps = this.conn.prepareStatement(query);
                ps.setString(1, hashedPassword);
                ps.setString(2, saltString);
                ps.setInt(3, patientID);

                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            } else {
                return false;
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean updatePatientStatus(int patientID) {
        PreparedStatement ps = null;
        String query = "UPDATE patient SET patientStatus = ? WHERE patientID = ?";

        try {
            ps = conn.prepareStatement(query);

            ps.setInt(1, 1);
            ps.setInt(2, patientID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePatientPassword(int patientID, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "UPDATE patient SET patientPassword = ? WHERE patientID = ?";
        String selectQuery = "SELECT salt FROM patient WHERE patientID = ?";

        try {
            ps = this.conn.prepareStatement(selectQuery);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedSalt = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(storedSalt);
                String enteredHashedPassword = hashPassword(password, salt);

                ps = this.conn.prepareStatement(query);
                ps.setString(1, enteredHashedPassword);
                ps.setInt(2, patientID);

                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            } else {
                return false;
            }

        } catch (SQLException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean updatePatientName(int patientID, String name) {
        String query = "UPDATE patient SET patientName = ? WHERE patientID = ?";
        try (PreparedStatement ps = this.conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, patientID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatientEmail(int patientID, String email) {
        String checkEmailQuery = "SELECT COUNT(*) FROM patient WHERE patientEmail = ?";
        String updateQuery = "UPDATE patient SET patientEmail = ? WHERE patientID = ?";

        try (PreparedStatement checkPs = this.conn.prepareStatement(checkEmailQuery); PreparedStatement updatePs = this.conn.prepareStatement(updateQuery)) {

            checkPs.setString(1, email);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            // Step 2: Update the email if it doesn't exist
            updatePs.setString(1, email);
            updatePs.setInt(2, patientID);
            return updatePs.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatientPhone(int patientID, String phone) {
        String query = "UPDATE patient SET patientPhone = ? WHERE patientID = ?";
        try (PreparedStatement ps = this.conn.prepareStatement(query)) {
            ps.setString(1, phone);
            ps.setInt(2, patientID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}

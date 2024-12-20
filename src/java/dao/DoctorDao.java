package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import model.Doctor;

public class DoctorDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public DoctorDao(Connection conn) {
        this.conn = conn;
    }

    public int getDoctorCount() {
        query = "SELECT COUNT(*) AS totalDoctors FROM doctor WHERE doctorRole = 'Doctor'";
        int doctorCount = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                doctorCount = rs.getInt("totalDoctors");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorCount;
    }
    
    public boolean updateDoctorImage(int doctorID, Part imagePart) throws SQLException {
        if (imagePart == null) {
            Logger.getLogger(PatientDao.class.getName()).log(Level.WARNING, "Image part is null.");
            return false;
        }

        String query = "UPDATE doctor SET doctorImage = ? WHERE doctorID = ?";
        try (
                InputStream patientImage = imagePart.getInputStream(); PreparedStatement ps = this.conn.prepareStatement(query)) {
            ps.setBinaryStream(1, patientImage);
            ps.setInt(2, doctorID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (IOException ex) {
            Logger.getLogger(PatientDao.class.getName()).log(Level.SEVERE, "Error processing image input stream", ex);
            return false;
        }
    }

    public boolean doctorUpdatePassward(int doctorID, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "UPDATE doctor SET doctorPassword = ? WHERE doctorID = ?";
        String selectQuery = "SELECT salt FROM doctor WHERE doctorID = ?";

        try {
            ps = this.conn.prepareStatement(selectQuery);
            ps.setInt(1, doctorID);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedSalt = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(storedSalt);
                String enteredHashedPassword = hashPassword(password, salt);

                ps = this.conn.prepareStatement(query);
                ps.setString(1, enteredHashedPassword);
                ps.setInt(2, doctorID);

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

    public boolean updateDoctor(Integer doctorID, Integer serviceID, String name, String role, String email, String dob, String phone, String gender, int status) {
        PreparedStatement checkPs = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;
        String checkEmailQuery = "SELECT doctorID FROM doctor WHERE doctorEmail = ? AND doctorID != ?";
        String updateQuery = "UPDATE doctor SET servicesID = ?, doctorName = ?, doctorRole = ?, doctorEmail = ?, doctorDOB = ?, doctorPhone = ?, doctorGender = ?, doctorStatus = ? WHERE doctorID = ?";

        try {
            checkPs = this.conn.prepareStatement(checkEmailQuery);
            checkPs.setString(1, email);
            checkPs.setInt(2, doctorID);
            rs = checkPs.executeQuery();

            if (rs != null && rs.next()) {
                return false;
            }

            updatePs = this.conn.prepareStatement(updateQuery);

            if (serviceID != null) {
                updatePs.setInt(1, serviceID);
            } else {
                updatePs.setNull(1, java.sql.Types.INTEGER);
            }
            updatePs.setString(2, name);
            updatePs.setString(3, role);
            updatePs.setString(4, email);
            updatePs.setString(5, dob);
            updatePs.setString(6, phone);
            updatePs.setString(7, gender);
            updatePs.setInt(8, status);
            updatePs.setInt(9, doctorID);

            int rowsUpdated = updatePs.executeUpdate();
            return rowsUpdated > 0;

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

    public boolean deleteDoctor(int doctorID) throws SQLException {
        String query = "DELETE FROM doctor WHERE doctorID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public List<Doctor> getAllDoctor() {
        String query = "SELECT * FROM doctor";
        List<Doctor> doctorsList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorID(rs.getInt("doctorID"));
                doctor.setServicesID(rs.getInt("servicesID"));
                doctor.setDoctorName(rs.getString("doctorName"));
                doctor.setDoctorEmail(rs.getString("doctorEmail"));
                doctor.setDoctorImage(rs.getBytes("doctorImage"));
                doctor.setDoctorRole(rs.getString("doctorRole"));
                doctor.setDoctorPhone(rs.getString("doctorPhone"));
                doctor.setDoctorGender(rs.getString("doctorGender"));
                doctor.setDoctorDOB(rs.getString("doctorDOB"));
                doctor.setDoctorRegisterDate(rs.getString("doctorRegisterDate"));
                doctor.setDoctorStatus(rs.getInt("doctorStatus"));

                doctorsList.add(doctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorsList;
    }

    public List<Doctor> getAllDoctors() {
        String query = "SELECT * FROM doctor WHERE doctorRole = 'Doctor'";
        List<Doctor> doctorsList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorID(rs.getInt("doctorID"));
                doctor.setServicesID(rs.getInt("servicesID"));
                doctor.setDoctorName(rs.getString("doctorName"));
                doctor.setDoctorEmail(rs.getString("doctorEmail"));
                doctor.setDoctorImage(rs.getBytes("doctorImage"));
                doctor.setDoctorRole(rs.getString("doctorRole"));
                doctor.setDoctorPhone(rs.getString("doctorPhone"));
                doctor.setDoctorGender(rs.getString("doctorGender"));
                doctor.setDoctorDOB(rs.getString("doctorDOB"));
                doctor.setDoctorRegisterDate(rs.getString("doctorRegisterDate"));
                doctor.setDoctorStatus(rs.getInt("doctorStatus"));

                doctorsList.add(doctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorsList;
    }

    public Doctor getDoctor(int doctorID) {
        String query = "SELECT * FROM doctor WHERE doctorID = ?";
        Doctor doctor = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID); // Set the doctorID parameter correctly

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("doctorID"));
                    doctor.setServicesID(rs.getInt("servicesID"));
                    doctor.setDoctorName(rs.getString("doctorName"));
                    doctor.setDoctorEmail(rs.getString("doctorEmail"));
                    doctor.setDoctorImage(rs.getBytes("doctorImage"));
                    doctor.setDoctorRole(rs.getString("doctorRole"));
                    doctor.setDoctorPhone(rs.getString("doctorPhone"));
                    doctor.setDoctorGender(rs.getString("doctorGender"));
                    doctor.setDoctorDOB(rs.getString("doctorDOB"));
                    doctor.setDoctorRegisterDate(rs.getString("doctorRegisterDate"));
                    doctor.setDoctorStatus(rs.getInt("doctorStatus"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    public List<Doctor> getAllDoctorsServices(int servicesID) {
        String query = "SELECT * FROM doctor WHERE doctorRole = 'Doctor' AND doctorStatus = 1 AND servicesID = ?";
        List<Doctor> doctorsList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, servicesID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("doctorID"));
                    doctor.setServicesID(rs.getInt("servicesID"));
                    doctor.setDoctorName(rs.getString("doctorName"));
                    doctor.setDoctorEmail(rs.getString("doctorEmail"));
                    doctor.setDoctorImage(rs.getBytes("doctorImage"));
                    doctor.setDoctorRole(rs.getString("doctorRole"));
                    doctor.setDoctorPhone(rs.getString("doctorPhone"));
                    doctor.setDoctorGender(rs.getString("doctorGender"));
                    doctor.setDoctorDOB(rs.getString("doctorDOB"));
                    doctor.setDoctorRegisterDate(rs.getString("doctorRegisterDate"));
                    doctor.setDoctorStatus(rs.getInt("doctorStatus"));

                    doctorsList.add(doctor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorsList;
    }

    public int doctorRegister(Integer servicesID, String doctorName, String doctorEmail, String doctorPassword, String doctorRole, String doctorPhone, String doctorGender, String doctorDOB, int doctorStatus) {
        PreparedStatement checkPs = null;
        PreparedStatement insertPs = null;
        ResultSet rs = null;
        ResultSet generatedKeys = null;

        String checkEmailQuery = "SELECT * FROM doctor WHERE doctorEmail = ?";

        query = "INSERT INTO doctor (servicesID, doctorName, doctorEmail, doctorPassword, doctorRole, doctorPhone, doctorGender, doctorDOB, doctorStatus, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            checkPs = this.conn.prepareStatement(checkEmailQuery);
            checkPs.setString(1, doctorEmail);
            rs = checkPs.executeQuery();

            if (rs != null && rs.next()) {
                return -1;
            } else {
                byte[] salt = getSalt();
                String hashedPassword = hashPassword(doctorPassword, salt);
                String saltString = Base64.getEncoder().encodeToString(salt);

                insertPs = this.conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                if (servicesID != null) {
                    insertPs.setInt(1, servicesID);
                } else {
                    insertPs.setNull(1, java.sql.Types.INTEGER);
                }
                insertPs.setString(2, doctorName);
                insertPs.setString(3, doctorEmail);
                insertPs.setString(4, hashedPassword);
                insertPs.setString(5, doctorRole);
                insertPs.setString(6, doctorPhone);
                insertPs.setString(7, doctorGender);
                insertPs.setString(8, doctorDOB);
                insertPs.setInt(9, doctorStatus);
                insertPs.setString(10, saltString);

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

    public Doctor doctorLogin(String doctorEmail, String doctorPassword) {
        Doctor doctor = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM doctor WHERE doctorEmail = ? AND doctorStatus = 1";

        try {
            ps = this.conn.prepareStatement(query);
            ps.setString(1, doctorEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("doctorPassword");
                String storedSalt = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(storedSalt);
                String enteredHashedPassword = hashPassword(doctorPassword, salt);

                if (storedHashedPassword.equals(enteredHashedPassword)) {
                    doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("doctorID"));
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
        return doctor;
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

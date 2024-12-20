package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Payment;

public class PaymentDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public PaymentDao(Connection conn) {
        this.conn = conn;
    }
    
    public boolean deleteDrugPayment(int appointmentID) throws SQLException {
        query = "DELETE FROM payment WHERE appointmentID = ? AND paymentDesc = 'Drug payment'";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public List<Payment> getAllPaymentPaid() {
        query = " SELECT * FROM payment WHERE paymentStatus = 1";
        List<Payment> paymentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentIntentId(rs.getString("paymentIntentId"));
                payment.setAppointmentID(rs.getInt("appointmentID"));
                payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                payment.setPaymentMethod(rs.getString("paymentMethod"));
                payment.setPaymentDesc(rs.getString("paymentDesc"));
                payment.setPaymentDate(rs.getString("paymentDate"));
                payment.setPaymentRefundDate(rs.getString("paymentRefundDate"));
                payment.setPaymentStatus(rs.getInt("paymentStatus"));

                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    public List<Payment> getRecentPaidPayments() {
        query = " SELECT * FROM payment WHERE paymentStatus = 1 ORDER BY paymentDate DESC LIMIT 10";
        List<Payment> paymentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentIntentId(rs.getString("paymentIntentId"));
                payment.setAppointmentID(rs.getInt("appointmentID"));
                payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                payment.setPaymentMethod(rs.getString("paymentMethod"));
                payment.setPaymentDesc(rs.getString("paymentDesc"));
                payment.setPaymentDate(rs.getString("paymentDate"));
                payment.setPaymentRefundDate(rs.getString("paymentRefundDate"));
                payment.setPaymentStatus(rs.getInt("paymentStatus"));

                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    public double getTotalPaymentsMadeToday() {
        query = "SELECT SUM(paymentAmount) AS totalAmount FROM payment WHERE DATE(paymentDate) = CURDATE()";
        double totalPayment = 0.0;

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalPayment = rs.getDouble("totalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPayment;
    }

    public boolean updateInsertPayment(int appointmentID, double paymentAmount) {
        String checkQuery = "SELECT paymentStatus, paymentAmount FROM payment WHERE appointmentID = ?";
        String insertQuery = "INSERT INTO payment (paymentID, paymentDesc, appointmentID, paymentAmount, paymentMethod, paymentStatus) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(checkQuery);
            ps.setInt(1, appointmentID);
            rs = ps.executeQuery();

            if (rs.next()) {
                int paymentStatus = rs.getInt("paymentStatus");

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String paymentID = now.format(formatter);
                ps.close();
                ps = conn.prepareStatement(insertQuery);
                ps.setString(1, paymentID);
                ps.setString(2, "Services & Drug Payment");
                ps.setInt(3, appointmentID);
                ps.setDouble(4, paymentAmount);
                ps.setString(5, "cash");
                ps.setInt(6, 0);

                int rowsInserted = ps.executeUpdate();
                return rowsInserted > 0;

            }
        } catch (SQLException e) {
            System.out.println("SQLException during payment handling: " + e.getMessage());
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
                System.out.println("SQLException during resource cleanup: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean deletePayment(String paymentID) throws SQLException {
        query = "DELETE FROM payment WHERE paymentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, paymentID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public List<Payment> getAllPaymentByID(int appointmentID) {
        String query = "SELECT * FROM payment WHERE appointmentID = ?";
        List<Payment> paymentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentID(rs.getString("paymentID"));
                    payment.setPaymentIntentId(rs.getString("paymentIntentId"));
                    payment.setAppointmentID(rs.getInt("appointmentID"));
                    payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                    payment.setPaymentMethod(rs.getString("paymentMethod"));
                    payment.setPaymentDesc(rs.getString("paymentDesc"));
                    payment.setPaymentDate(rs.getString("paymentDate"));
                    payment.setPaymentRefundDate(rs.getString("paymentRefundDate"));
                    payment.setPaymentStatus(rs.getInt("paymentStatus"));

                    paymentList.add(payment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    public boolean updateDeletePayment(String paymentID) {
        String query = "SELECT paymentStatus FROM payment WHERE paymentID = ?";
        String updateQuery = "UPDATE payment SET paymentStatus = ?, paymentRefundDate = ? WHERE paymentID = ?";

        try {
            PreparedStatement selectPs = conn.prepareStatement(query);
            PreparedStatement updatePs = conn.prepareStatement(updateQuery);

            selectPs.setString(1, paymentID);
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    int currentStatus = rs.getInt("paymentStatus");

                    int newStatus = (currentStatus == 1) ? 3 : 2;

                    updatePs.setInt(1, newStatus);

                    if (newStatus == 3) {
                        updatePs.setNull(2, java.sql.Types.TIMESTAMP);
                    } else {
                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                        updatePs.setTimestamp(2, currentTimestamp);
                    }

                    updatePs.setString(3, paymentID);

                    int rowsAffected = updatePs.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePayment(String paymentID, double paymentAmount, int paymentStatus) {
        String query1 = "UPDATE payment SET paymentAmount = ?, paymentStatus = ?, paymentDate = ?, paymentRefundDate = NULL WHERE paymentID = ?";
        String query2 = "UPDATE payment SET paymentAmount = ?, paymentStatus = ?, paymentRefundDate = ? WHERE paymentID = ?";
        String query3 = "UPDATE payment SET paymentAmount = ?, paymentStatus = ?, paymentDate = NULL, paymentRefundDate = NULL WHERE paymentID = ?";
        String query4 = "UPDATE payment SET paymentStatus = ?, paymentRefundDate = NULL WHERE paymentID = ?";

        try {
            if (paymentStatus == 1) {
                try (PreparedStatement ps = conn.prepareStatement(query1)) {
                    ps.setDouble(1, paymentAmount);
                    ps.setInt(2, paymentStatus);
                    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                    ps.setTimestamp(3, currentTimestamp);
                    ps.setString(4, paymentID);
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                }
            } else if (paymentStatus == 2) {
                try (PreparedStatement ps = conn.prepareStatement(query2)) {
                    ps.setDouble(1, paymentAmount);
                    ps.setInt(2, paymentStatus);
                    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                    ps.setTimestamp(3, currentTimestamp);
                    ps.setString(4, paymentID);
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                }
            } else if (paymentStatus == 0) {
                try (PreparedStatement ps = conn.prepareStatement(query3)) {
                    ps.setDouble(1, paymentAmount);
                    ps.setInt(2, paymentStatus);
                    ps.setString(3, paymentID);
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(query4)) {
                    ps.setInt(1, paymentStatus);
                    ps.setString(2, paymentID);
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Payment getPaymentByID(String paymentID) {
        query = "SELECT * FROM payment WHERE paymentID = ?";
        Payment payment = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, paymentID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    payment = new Payment();
                    payment.setPaymentID(rs.getString("paymentID"));
                    payment.setPaymentIntentId(rs.getString("paymentIntentId"));
                    payment.setAppointmentID(rs.getInt("appointmentID"));
                    payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                    payment.setPaymentMethod(rs.getString("paymentMethod"));
                    payment.setPaymentDesc(rs.getString("paymentDesc"));
                    payment.setPaymentDate(rs.getString("paymentDate"));
                    payment.setPaymentRefundDate(rs.getString("paymentRefundDate"));
                    payment.setPaymentStatus(rs.getInt("paymentStatus"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payment;
    }

    public List<Payment> getAllPayment() {
        String query = "SELECT * FROM payment";
        List<Payment> paymentList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentIntentId(rs.getString("paymentIntentId"));
                payment.setAppointmentID(rs.getInt("appointmentID"));
                payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                payment.setPaymentMethod(rs.getString("paymentMethod"));
                payment.setPaymentDesc(rs.getString("paymentDesc"));
                payment.setPaymentDate(rs.getString("paymentDate"));
                payment.setPaymentRefundDate(rs.getString("paymentRefundDate"));
                payment.setPaymentStatus(rs.getInt("paymentStatus"));

                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    public boolean insertPayment(String paymentID, String paymentIntentId, String paymentDesc, int appointmentID, double paymentAmount, String paymentMethod, int paymentStatus) {
        PreparedStatement ps = null;

        query = "INSERT INTO payment (paymentID, paymentIntentId, paymentDesc, appointmentID, paymentAmount, paymentMethod, paymentStatus, paymentDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(query);

            ps.setString(1, paymentID);
            if (paymentIntentId != null) {
                ps.setString(2, paymentIntentId);
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }
            ps.setString(3, paymentDesc);
            ps.setInt(4, appointmentID);
            ps.setDouble(5, paymentAmount);
            ps.setString(6, paymentMethod);
            ps.setInt(7, paymentStatus);

            if (paymentStatus == 1) {
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                ps.setTimestamp(8, currentTimestamp);
            } else {
                ps.setNull(8, java.sql.Types.TIMESTAMP);
            }

            int rowsInserted = ps.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("SQLException during payment insertion: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException during resource cleanup: " + e.getMessage());
            }
        }
    }
}

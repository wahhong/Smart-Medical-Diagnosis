package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Notification;

public class NotificationDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public NotificationDao(Connection conn) {
        this.conn = conn;
    }

    public List<Notification> getNotificationsByDoctorID(int doctorID) {
        String query = "SELECT * FROM notification WHERE doctorID = ? ORDER BY notificationID DESC";
        List<Notification> notificationList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("notificationID"));
                    notification.setAppointmentID(rs.getInt("appointmentID"));
                    notification.setNotificationTitle(rs.getString("notificationTitle"));
                    notification.setNotificationDesc(rs.getString("notificationDesc"));
                    notification.setNotificationStatus(rs.getInt("notificationStatus"));
                    notificationList.add(notification);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving notifications: " + e.getMessage());
        }

        return notificationList;
    }

    public boolean insertNotification(int appointmentID, int doctorID, String notificationTitle, String notificationDesc, int notificationStatus) {
        query = "INSERT INTO notification (appointmentID, doctorID, notificationTitle, notificationDesc, notificationStatus) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentID);
            ps.setInt(2, doctorID);
            ps.setString(3, notificationTitle);
            ps.setString(4, notificationDesc);
            ps.setInt(5, notificationStatus);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting notification: " + e.getMessage());
            return false;
        }
    }

    public boolean updateNotification(int notificationID) {
        query = "UPDATE notification SET notificationStatus = ? WHERE notificationID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setInt(2, notificationID);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating notification: " + e.getMessage());
            return false;
        }
    }
}

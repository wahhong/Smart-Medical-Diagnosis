package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Part;
import model.Services;

public class ServicesDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public ServicesDao(Connection conn) {
        this.conn = conn;
    }
    
    public boolean deleteService(int servicesID) throws SQLException {
        String query = "UPDATE services SET isDelete = 1 WHERE servicesID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, servicesID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public boolean updateService(int servicesID, String servicesName, String servicesDesc, double servicePrice, int servicesAi, int servicesStatus) {
        String query = "UPDATE services SET servicesName = ?, servicesDesc = ?, servicesPrice = ?, servicesAi= ?, servicesStatus = ? WHERE servicesID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, servicesName);
            ps.setString(2, servicesDesc);
            ps.setDouble(3, servicePrice);
            ps.setInt(4, servicesAi);
            ps.setInt(5, servicesStatus);
            ps.setInt(6, servicesID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateServiceImage(int servicesID, Part servicesImage) {
        query = "UPDATE services SET servicesImage = ? WHERE servicesID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            if (servicesImage != null) {
                InputStream imageStream = servicesImage.getInputStream();
                ps.setBlob(1, imageStream);
            } else {
                ps.setNull(1, java.sql.Types.BLOB);
            }
            ps.setInt(2, servicesID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertService(String servicesName, String servicesDesc, double servicesPrice, Part servicesImage, int servicesAi, int servicesStatus) {
        query = "INSERT INTO services (servicesName, servicesDesc, servicesPrice, servicesImage, servicesAi, servicesStatus) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, servicesName);
            ps.setString(2, servicesDesc);
            ps.setDouble(3, servicesPrice);
            if (servicesImage != null) {
                InputStream imageStream = servicesImage.getInputStream();
                ps.setBlob(4, imageStream);
            } else {
                ps.setNull(4, java.sql.Types.BLOB);
            }
            ps.setInt(5, servicesAi);
            ps.setInt(6, servicesStatus);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getServiceNameById(int servicesID) {
        query = "SELECT servicesName FROM services WHERE servicesID = ?;";
        String serviceName = null;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, servicesID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                serviceName = rs.getString("servicesName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceName;
    }

    public List<Services> getAllServices() {
        String query = "SELECT * FROM services WHERE isDelete = 0;";
        List<Services> servicesList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Services services = new Services();
                services.setServicesID(rs.getInt("servicesID"));
                services.setServicesName(rs.getString("servicesName"));
                services.setServicesDesc(rs.getString("servicesDesc"));
                services.setServicesPrice(rs.getDouble("servicesPrice"));
                services.setServicesImage(rs.getBytes("servicesImage"));
                services.setCreateDate(rs.getString("createDate"));
                services.setServicesAi(rs.getInt("servicesAi"));
                services.setServicesStatus(rs.getInt("servicesStatus"));

                servicesList.add(services);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicesList;
    }

    public List<Services> getAllServicesActive() {
        String query = "SELECT * FROM services WHERE servicesStatus = 1 AND isDelete = 0;";
        List<Services> servicesList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Services services = new Services();
                services.setServicesID(rs.getInt("servicesID"));
                services.setServicesName(rs.getString("servicesName"));
                services.setServicesDesc(rs.getString("servicesDesc"));
                services.setServicesPrice(rs.getDouble("servicesPrice"));
                services.setServicesImage(rs.getBytes("servicesImage"));
                services.setCreateDate(rs.getString("createDate"));
                services.setServicesAi(rs.getInt("servicesAi"));
                services.setServicesStatus(rs.getInt("servicesStatus"));

                servicesList.add(services);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicesList;
    }

    public Services getService(int servicesID) {
        String query = "SELECT * FROM services WHERE servicesID = ?;";
        Services service = null;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, servicesID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                service = new Services();
                service.setServicesID(rs.getInt("servicesID"));
                service.setServicesName(rs.getString("servicesName"));
                service.setServicesDesc(rs.getString("servicesDesc"));
                service.setServicesPrice(rs.getDouble("servicesPrice"));
                service.setServicesImage(rs.getBytes("servicesImage"));
                service.setCreateDate(rs.getString("createDate"));
                service.setServicesAi(rs.getInt("servicesAi"));
                service.setServicesStatus(rs.getInt("servicesStatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }
}

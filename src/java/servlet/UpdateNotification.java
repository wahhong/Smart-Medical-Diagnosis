package servlet;

import connection.DbConn;
import dao.DoctorDao;
import dao.NotificationDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateNotification extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int notificationID = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : null;
        int aptID = request.getParameter("aptID") != null ? Integer.parseInt(request.getParameter("aptID")) : null;
        RequestDispatcher dispatcher = null;

        try {
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());
            boolean isUpdate = notificationDao.updateNotification(notificationID);
            
            response.sendRedirect("editAppointment.jsp?id="+aptID);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}

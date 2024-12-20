package servlet;

import connection.DbConn;
import dao.AppointmentDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAppointment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        int appointmentID = Integer.parseInt(request.getParameter("id"));
        
        try {
            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());

            boolean isDeleted = appointmentDao.deleteAppointment(appointmentID);

            if (isDeleted) {
                request.setAttribute("successMsg", "Appointment has been deleted successfully.");
            } else {
                request.setAttribute("errorMsg", "Appointment has been deleted unsuccessfully.");
            }

            dispatcher = request.getRequestDispatcher("appointment.jsp");
            dispatcher.forward(request, response);

        } catch (ClassNotFoundException | SQLException ex) {
            request.setAttribute("errorMsg", "Appointment has been deleted unsuccessfully.");
            dispatcher = request.getRequestDispatcher("appointment.jsp");
            dispatcher.forward(request, response);
        }
    }
}

package servlet;

import connection.DbConn;
import dao.ServicesDao;
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
import javax.servlet.http.HttpSession;
import model.Appointment;
import model.Services;

public class MakeAppointment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            RequestDispatcher dispatcher;
            
            String selectedDate = request.getParameter("selectedDate");
            String selectedTime = request.getParameter("selectedTime") == null ? null : request.getParameter("selectedTime");
            int doctorID = Integer.parseInt(request.getParameter("doctorID"));
            int servicesID = Integer.parseInt(request.getParameter("servicesID"));
            
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            Services service = servicesDao.getService(servicesID);
            
            Appointment appointment = new Appointment(doctorID, servicesID, selectedDate, selectedTime);
            
            session.setAttribute("appointmentDB", appointment);
            
            if (service.getServicesAi() == 1) {
                response.sendRedirect("aiDiagnosis.jsp");
            } else {
                response.sendRedirect("patientDetails.jsp");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MakeAppointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

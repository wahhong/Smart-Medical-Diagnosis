package servlet;

import connection.DbConn;
import dao.ServicesDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Appointment;
import model.Services;

@WebServlet(name = "SubmitAppointment", urlPatterns = {"/SubmitAppointment"})
public class SubmitAppointment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            
            String selectedDate = request.getParameter("selectedDate");
            String selectedTime = request.getParameter("selectedTime");
            int doctorID = Integer.parseInt(request.getParameter("doctorID"));
            int servicesID = Integer.parseInt(request.getParameter("servicesID"));
            int patientID = (Integer) session.getAttribute("userAuthID");
            
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            Services service = servicesDao.getService(servicesID);
            
            Appointment appointment = new Appointment(doctorID, servicesID, patientID, selectedDate, selectedTime);
            
            session.setAttribute("appointment", appointment);
            
            if (service.getServicesAi() == 1) {
                response.sendRedirect("aiDiagnosis.jsp");
            } else {
                response.sendRedirect("payment.jsp");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SubmitAppointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

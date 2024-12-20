package servlet;

import connection.DbConn;
import dao.PatientDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientID = Integer.parseInt(request.getParameter("patientID"));
        HttpSession session = request.getSession();

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            
            boolean updateSuccess = patientDao.updatePatientStatus(patientID);
            
            if (updateSuccess) {
                response.sendRedirect("login.jsp");
            } else {
                response.getWriter().println("<html><body><p>Failed to verify account. Please try again.</p></body></html>");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            session.setAttribute("msgError", "Error occur");
            response.sendRedirect("home.jsp");
        }
    }
}

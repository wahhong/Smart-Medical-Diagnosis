package servlet;

import connection.DbConn;
import dao.PatientDao;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Patient;

@WebServlet(name = "PatientLogin", urlPatterns = {"/PatientLogin"})
public class PatientLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        RequestDispatcher dispatcher = null;

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            Patient patient = patientDao.patientLogin(email, password);

            if (patient != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userAuthID", patient.getPatientID());

                request.removeAttribute("userAuthError");
                response.sendRedirect("home.jsp");
                return;
            } else {
                request.setAttribute("userAuthError", "Invalid email or password!");
                dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
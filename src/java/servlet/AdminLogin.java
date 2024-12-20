package servlet;

import connection.DbConn;
import dao.DoctorDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Doctor;

public class AdminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        RequestDispatcher dispatcher = null;
        
        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            Doctor doctor = doctorDao.doctorLogin(email, password);

            if (doctor != null) {
                HttpSession session = request.getSession();
                session.setAttribute("adminAuthID", doctor.getDoctorID());

                request.removeAttribute("errorLogin");
                response.sendRedirect("dashboard.jsp");
                return;
            } else {
                request.setAttribute("errorLogin", "Invalid email or password!");
                dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp");
        }
        
    }
}

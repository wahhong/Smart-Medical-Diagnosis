package servlet;

import connection.DbConn;
import dao.DoctorDao;
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

public class DeleteDoctor extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        int doctorID = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();

        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());

            boolean isDeleted = doctorDao.deleteDoctor(doctorID);

            if (isDeleted) {
                session.setAttribute("successMsg", "Doctor has been deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Doctor delete unsuccessfully.");
            }

            response.sendRedirect("doctor.jsp");

        } catch (ClassNotFoundException | SQLException ex) {
            session.setAttribute("errorMsg", "Doctor delete unsuccessfully.");
            response.sendRedirect("doctor.jsp");
        }
    }
}

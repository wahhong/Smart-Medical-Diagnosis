package servlet;

import connection.DbConn;
import dao.PatientDao;
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

public class DeletePatient extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        int patientID = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());

            boolean isDeleted = patientDao.deletePatient(patientID);

            if (isDeleted) {
                session.setAttribute("successMsg", "Patient has been deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Patient has been deleted unsuccessfully.");
            }

            response.sendRedirect("patient.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            request.setAttribute("errorMsg", "Patient has been deleted unsuccessfully.");
            dispatcher = request.getRequestDispatcher("patient.jsp");
            dispatcher.forward(request, response);
        }
    }
}

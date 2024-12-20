package servlet;

import connection.DbConn;
import dao.PatientDao;
import dao.VerifyCodeDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mail.ForgetPasswordSender;

public class VerifyEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        RequestDispatcher dispatcher = null;
        String email = request.getParameter("email");
        String invalidEmail = "";
        int patientID;

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            invalidEmail = "* Invalid email format.";
        }

        if (invalidEmail != "") {
            request.setAttribute("invalidEmail", invalidEmail);
            dispatcher = request.getRequestDispatcher("forgetPassword.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                patientID = patientDao.getPatientID(email);

                if (patientID > 0) {
                    request.removeAttribute("invalidEmail");
                    String code = String.format("%06d", new java.util.Random().nextInt(999999));
                    ForgetPasswordSender.sendEmail(email, code + " is your verify code", code);

                    VerifyCodeDao verifyCodeDao = new VerifyCodeDao(DbConn.getConnection());
                    verifyCodeDao.insertOrUpdateVerifyCode(patientID, code);

                    response.sendRedirect("verifyCode.jsp?id=" + patientID);
                    return;
                } else {
                    request.setAttribute("invalidEmail", "Email Not Found!");
                    dispatcher = request.getRequestDispatcher("forgetPassword.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (ClassNotFoundException ex) {
                session.setAttribute("msgError", "Error occur");
                response.sendRedirect("home.jsp");
            } catch (SQLException ex) {
                session.setAttribute("msgError", "Error occur");
                response.sendRedirect("home.jsp");
            }
        }
    }
}

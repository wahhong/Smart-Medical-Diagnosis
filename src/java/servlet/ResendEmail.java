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
import mail.ForgetPasswordSender;

@WebServlet(name = "ResendEmail", urlPatterns = {"/ResendEmail"})
public class ResendEmail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestDispatcher dispatcher = null;
        String email = "";
        int patientID = Integer.parseInt(request.getParameter("id"));

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            email = patientDao.getPatientEmail(patientID);
            
            String code = String.format("%06d", new java.util.Random().nextInt(999999));
            ForgetPasswordSender.sendEmail(email, code + " is your verify code", code);

            VerifyCodeDao verifyCodeDao = new VerifyCodeDao(DbConn.getConnection());
            verifyCodeDao.insertOrUpdateVerifyCode(patientID, code);

            response.sendRedirect("verifyCode.jsp?id=" + patientID);
            return;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VerifyEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VerifyEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

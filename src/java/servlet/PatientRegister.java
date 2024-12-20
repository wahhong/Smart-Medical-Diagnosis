package servlet;

import connection.DbConn;
import dao.PatientDao;
import dao.VerifyCodeDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mail.RegisterEmailSender;

@WebServlet(name = "PatientRegister", urlPatterns = {"/PatientRegister"})
public class PatientRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestDispatcher dispatcher = null;
        int patientID;
        String errorMessage = "";
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");

        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("dob", dob);
        request.setAttribute("gender", gender);
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorMessage += "* Invalid email format.<br>";
        }
        if (!password.equals(confirmPassword)) {
            errorMessage += "* Passwords do not match.<br>";
        } else if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,}$")) {
            errorMessage += "* Password must be at least 10 characters, contain at least 1 number and 1 special character.<br>";
        }
        if (!phone.matches("^[0-9]{10,11}$")) {
            errorMessage += "* Invalid phone number.<br>";
        }
        if (errorMessage != "") {
            request.setAttribute("errorMessage", errorMessage);
            dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                patientID = patientDao.patientRegister(name, email, password, phone, gender, dob);

                if (patientID > 0) {
                    request.removeAttribute("errorMessage");
                    RegisterEmailSender.sendEmail(email, "Click the link to verify your account", "http://localhost:8080/Smart_Medical_Diagnostic_System/VerifyAccount?patientID=" + patientID);

                    dispatcher = request.getRequestDispatcher("login.jsp");
                    dispatcher.forward(request, response);
                } else {
                    errorMessage = "* Email already exists.<br>";
                    request.setAttribute("errorMessage", errorMessage);
                    dispatcher = request.getRequestDispatcher("register.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

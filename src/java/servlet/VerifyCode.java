package servlet;

import connection.DbConn;
import dao.VerifyCodeDao;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
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

@WebServlet(name = "VerifyCode", urlPatterns = {"/VerifyCode"})
public class VerifyCode extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        RequestDispatcher dispatcher = null;
        boolean codeBool;
        String code = request.getParameter("code");
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();

        try {
            VerifyCodeDao verifyCodeDao = new VerifyCodeDao(DbConn.getConnection());
            codeBool = verifyCodeDao.verifyCode(id, code);

            if (codeBool == true) {
                request.removeAttribute("errorCode");
                response.sendRedirect("resetPassword.jsp?id=" + id);
                return;
            } else {
                request.setAttribute("errorCode", "Invalid Code");
                dispatcher = request.getRequestDispatcher("verifyCode.jsp?id=" + id);
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            session.setAttribute("msgError", "Error occur");
            response.sendRedirect("home.jsp");
        }
    }
}

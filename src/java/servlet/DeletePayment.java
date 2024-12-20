package servlet;

import connection.DbConn;
import dao.PaymentDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeletePayment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        String patientID = request.getParameter("id");
        HttpSession session = request.getSession();
        
        try {
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());

            boolean isDeleted = paymentDao.deletePayment(patientID);

            if (isDeleted) {
                session.setAttribute("successMsg", "Payment has been deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Payment has been deleted unsuccessfully.");
            }

            response.sendRedirect("payment.jsp");

        } catch (ClassNotFoundException | SQLException ex) {
            session.setAttribute("errorMsg", "Payment has been deleted unsuccessfully.");
            response.sendRedirect("payment.jsp");
        }
    }
}

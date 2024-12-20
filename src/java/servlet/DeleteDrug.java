package servlet;

import connection.DbConn;
import dao.DrugDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteDrug extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        int drugID = Integer.parseInt(request.getParameter("id"));

        try {
            DrugDao drugDao = new DrugDao(DbConn.getConnection());

            boolean isDeleted = drugDao.deleteDrug(drugID);

            if (isDeleted) {
                session.setAttribute("successMsg", "Drug has been deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Drug has been deleted unsuccessfully.");
            }
            
            response.sendRedirect("drug.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            session.setAttribute("errorMsg", "Drug has been deleted unsuccessfully.");
            response.sendRedirect("drug.jsp");
        }
    }
}

<%@page import="model.Payment"%>
<%@page import="java.util.List"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="dao.PaymentDao"%>
<%@page import="connection.DbConn"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>

<%
    String yearParam = request.getParameter("year");
    int selectedYear = (yearParam != null) ? Integer.parseInt(yearParam) : java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

    PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
    List<Payment> paymentAllList = paymentDao.getAllPaymentPaid();

    double[] monthlyTotals = new double[12];
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    for (Payment payment : paymentAllList) {
        String paymentDateString = payment.getPaymentDate();

        if (paymentDateString != null && !paymentDateString.isEmpty()) {
            try {
                Date paymentDate = dateFormat.parse(paymentDateString);
                calendar.setTime(paymentDate);

                int paymentYear = calendar.get(java.util.Calendar.YEAR);
                int paymentMonth = calendar.get(java.util.Calendar.MONTH);

                if (paymentYear == selectedYear) {
                    monthlyTotals[paymentMonth] += payment.getPaymentAmount();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    StringBuilder monthlyData = new StringBuilder();
    for (double total : monthlyTotals) {
        monthlyData.append(total).append(",");
    }

    if (monthlyData.length() > 0) {
        monthlyData.setLength(monthlyData.length() - 1);  // Remove trailing comma
    }

    // Return data as JSON
    response.getWriter().write("{\"salesData\":[" + monthlyData.toString() + "],\"labels\":[\"Jan\",\"Feb\",\"Mar\",\"Apr\",\"May\",\"Jun\",\"Jul\",\"Aug\",\"Sep\",\"Oct\",\"Nov\",\"Dec\"]}");
%>

<%@page import="model.Payment"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Patient"%>
<%@page import="java.util.List"%>
<%@page import="dao.PaymentDao"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String paymentID = request.getParameter("id");
    PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());

    Payment payment = paymentDao.getPaymentByID(paymentID);

    if (payment == null) {
        response.sendRedirect("payment.jsp");
        return;
    }

    String errorPrice = (String) request.getAttribute("errorPrice");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Payment</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="payment.jsp">Payment</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Edit Payment Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditPayment">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" value="<%= payment.getPaymentID()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="pytID">Payment Intent ID</label>
                                        <input type="text" class="form-control" name="pytID" value="<%= payment.getPaymentIntentId()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="desc">Description</label>
                                        <textarea class="form-control" name="desc" rows="2" readonly><%= payment.getPaymentDesc()%></textarea>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="aptID">Appointment ID</label>
                                        <input type="text" class="form-control" name="aptID" value="<%= payment.getAppointmentID()%>" readonly/>
                                    </div>
                                    <div class="form-group ${errorPrice != null ? 'has-error' : ''}">
                                        <label for="price">Price</label>
                                        <input type="text" class="form-control" name="price" value="<%= String.format("%.2f", payment.getPaymentAmount())%>" readonly/>
                                        <% if (errorPrice != null) {%>
                                        <small class="form-text text-muted"><%= errorPrice%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="payMethod">Payment Method</label>
                                        <input type="text" class="form-control" name="payMethod" value="<%
                                            String paymentMethod = payment.getPaymentMethod();
                                            if ("creditCard".equalsIgnoreCase(paymentMethod)) {
                                                out.print("Credit Card");
                                            } else if ("cash".equalsIgnoreCase(paymentMethod)) {
                                                out.print("Cash");
                                            } else if ("paypal".equalsIgnoreCase(paymentMethod)) {
                                                out.print("Paypal");
                                            } else {
                                                out.print("Other");
                                            }
                                               %>"  readonly />
                                    </div>
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status" <%= payment.getPaymentStatus() == 1 || payment.getPaymentStatus() == 2 ? "disabled" : ""%>>
                                            <% if (payment.getPaymentStatus() != 3) {%>
                                            <option value="0" <%= "0".equals(request.getAttribute("status")) || payment.getPaymentStatus() == 0 ? "selected" : ""%>>Unpaid</option>
                                            <option value="1" <%= "1".equals(request.getAttribute("status")) || payment.getPaymentStatus() == 1 ? "selected" : ""%>>Paid</option>
                                            <% } %>
                                            <% if (payment.getPaymentStatus() != 0) {%>
                                            <option value="2" <%= "2".equals(request.getAttribute("status")) || payment.getPaymentStatus() == 2 ? "selected" : ""%>>Refund</option>
                                            <option value="3" <%= "3".equals(request.getAttribute("status")) || payment.getPaymentStatus() == 3 ? "selected" : ""%>>In progress</option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <%if (payment.getPaymentStatus() == 0 || payment.getPaymentStatus() == 3) {%>
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="payment.jsp">Cancel</a>
                            <%}%>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />

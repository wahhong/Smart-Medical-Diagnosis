<%@page import="model.*"%>
<%@page import="dao.*"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String doctorImage = null;
    PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
    List<Payment> paymentList = paymentDao.getAllPayment();
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());

    String errorMsgEmail = (String) session.getAttribute("errorMsgEmail");
    String successMsg = (String) session.getAttribute("successMsg");
    String errorMsg = (String) session.getAttribute("errorMsg");
%>
<div class="container">
    <div class="page-inner">
        <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
            <div>
                <h3 class="fw-bold mb-3">Payment</h3>
            </div>
        </div>

        <% if (successMsg != null) {%>
        <div class="alert alert-success"><%= successMsg%></div>
        <%
            session.removeAttribute("successMsg");
        } else if (errorMsg != null) {%>
        <div class="alert alert-danger"><%= errorMsg%></div>
        <%
            session.removeAttribute("errorMsg");
        } else if (errorMsgEmail != null) {%>
        <div class="alert alert-danger"><%= errorMsgEmail%></div>
        <%
                session.removeAttribute("errorMsgEmail");
            }%>

        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="add-row" class="display table table-striped table-hover" >
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Payment ID</th>
                                        <th>Appointment ID</th>
                                        <th>Patient Name</th>
                                        <th>Description</th>
                                        <th>Payment Amount</th>
                                        <th>Payment Method</th>
                                        <th>Payment Date</th>
                                        <th>Refund Date</th>
                                        <th>Status</th>
                                        <th style="width: 10%">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        if (paymentList != null) {
                                            for (Payment payment : paymentList) {
                                                Appointment appointment = appointmentDao.getAppointmentById(payment.getAppointmentID());
                                                Patient patient = patientDao.getPatient(appointment.getPatientID());

                                    %>
                                    <tr>
                                        <td><%= payment.getPaymentID()%></td>
                                        <td><%= (payment.getPaymentIntentId() == null || payment.getPaymentIntentId().isEmpty()) ? "" : payment.getPaymentIntentId() %></td>
                                        <td><%= payment.getAppointmentID()%></td>
                                        <td><%= patient.getPatientName() %></td>
                                        <td><%= payment.getPaymentDesc()%></td>
                                        <td><%= String.format("%.2f", payment.getPaymentAmount())%></td>
                                        <td>
                                            <%
                                                String paymentMethod = payment.getPaymentMethod();
                                                if ("cash".equalsIgnoreCase(paymentMethod)) {
                                            %>
                                            Cash
                                            <%
                                            } else if ("creditCard".equalsIgnoreCase(paymentMethod)) {
                                            %>
                                            Credit Card
                                            <%
                                            } else if ("paypal".equalsIgnoreCase(paymentMethod)) {
                                            %>
                                            Paypal
                                            <%
                                                }
                                            %>
                                        </td>
                                        <td><%= (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty()) ? "" : payment.getPaymentDate()%></td>
                                        <td><%= (payment.getPaymentRefundDate() == null || payment.getPaymentRefundDate().isEmpty()) ? "" : payment.getPaymentRefundDate()%></td>
                                        <td>
                                            <%
                                                int paymentStatus = payment.getPaymentStatus();
                                                if (paymentStatus == 1) {
                                            %>
                                            <span class="badge badge-success">Paid</span>
                                            <%
                                            } else if (paymentStatus == 0) {
                                            %>
                                            <span class="badge badge-warning">Unpaid</span>
                                            <%
                                            } else if (paymentStatus == 2) {
                                            %>
                                            <span class="badge badge-danger">Refund</span>
                                            <%
                                            } else if (paymentStatus == 3) {
                                            %>
                                            <span class="badge badge-info">In progress</span>

                                            <%
                                                }
                                            %>
                                        </td>

                                        <td>
                                            <div class="form-button-action">
                                                <a href="editPayment.jsp?id=<%= payment.getPaymentID()%>" data-bs-toggle="tooltip" title="Edit" class="btn btn-link btn-primary btn-lg">
                                                    <i class="fa fa-edit"></i>
                                                </a>
<!--                                                <button data-id="<%= payment.getPaymentID()%>" data-bs-toggle="tooltip" title="Remove" class="btn btn-link btn-danger delete-btn">
                                                    <i class="fa fa-times"></i>
                                                </button>-->
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                <h3>No Record</h3>
                                <%
                                    }
                                %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="assets/js/core/jquery-3.7.1.min.js"></script>
<script src="assets/js/core/popper.min.js"></script>
<!--<script src="assets/js/core/bootstrap.min.js"></script>-->
<script src="assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>
<script src="assets/js/kaiadmin.min.js"></script>
<script src="assets/js/plugin/sweetalert/sweetalert.min.js"></script>
<script>
    var SweetAlert2Demo = function () {

        var initDemos = function () {
            $('.delete-btn').click(function (e) {
                e.preventDefault();

                var paymentID = $(this).data('id');

                swal({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    type: 'warning',
                    buttons: {
                        confirm: {
                            text: 'Yes, delete it!',
                            className: 'btn btn-success'
                        },
                        cancel: {
                            visible: true,
                            className: 'btn btn-danger'
                        }
                    }
                }).then((Delete) => {
                    if (Delete) {
                        window.location.href = 'DeletePayment?id=' + paymentID;
                    } else {
                        swal.close();
                    }
                });
            });
        };

        return {
            init: function () {
                initDemos();
            },
        };
    }();

    jQuery(document).ready(function () {
        SweetAlert2Demo.init();
    });
</script>
<script >
    $(document).ready(function () {
        $('#basic-datatables').DataTable({
        });

        $('#multi-filter-select').DataTable({
            "pageLength": 5,
            initComplete: function () {
                this.api().columns().every(function () {
                    var column = this;
                    var select = $('<select class="form-select"><option value=""></option></select>')
                            .appendTo($(column.footer()).empty())
                            .on('change', function () {
                                var val = $.fn.dataTable.util.escapeRegex(
                                        $(this).val()
                                        );

                                column
                                        .search(val ? '^' + val + '$' : '', true, false)
                                        .draw();
                            });

                    column.data().unique().sort().each(function (d, j) {
                        select.append('<option value="' + d + '">' + d + '</option>')
                    });
                });
            }
        });

        // Add Row
        $('#add-row').DataTable({
            "pageLength": 5,
        });

        var action = '<td> <div class="form-button-action"> <button type="button" data-bs-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="Edit Task"> <i class="fa fa-edit"></i> </button> <button type="button" data-bs-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="Remove"> <i class="fa fa-times"></i> </button> </div> </td>';

        $('#addRowButton').click(function () {
            $('#add-row').dataTable().fnAddData([
                $("#addName").val(),
                $("#addPosition").val(),
                $("#addOffice").val(),
                action
            ]);
            $('#addRowModal').modal('hide');

        });
    });
</script>
<jsp:include page="adminFooter.jsp" />

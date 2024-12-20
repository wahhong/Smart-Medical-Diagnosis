<%@page import="model.Services"%>
<%@page import="dao.ServicesDao"%>
<%@page import="model.Doctor"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Patient"%>
<%@page import="java.util.List"%>
<%@page import="dao.DoctorDao"%>
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
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    List<Doctor> doctorList = doctorDao.getAllDoctor();
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());

    String errorMsgEmail = (String) session.getAttribute("errorMsgEmail");
    String successMsg = (String) session.getAttribute("successMsg");
    String errorMsg = (String) session.getAttribute("errorMsg");
%>
<div class="container">
    <div class="page-inner">
        <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
            <div>
                <h3 class="fw-bold mb-3">Doctor</h3>
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
                    <div class="card-header">
                        <div class="d-flex align-items-center">
                            <h4 class="card-title">Add Doctor</h4>
                            <a class="btn btn-primary btn-round ms-auto" href="addDoctor.jsp">
                                <i class="fa fa-plus"></i>
                                Add Row
                            </a>
                        </div>
                    </div>
                    <div class="card-body">

                        <div class="table-responsive">
                            <table id="add-row" class="display table table-striped table-hover" >
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Services</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Phone</th>
                                        <th>Gender</th>
                                        <th>DOB</th>
                                        <th>Register Date</th>
                                        <th>Status</th>
                                        <th>Password</th>
                                        <th style="width: 10%">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        if (doctorList != null) {
                                            for (Doctor doctor : doctorList) {
                                                if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
                                                    doctorImage = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
                                                } else {
                                                    doctorImage = null;
                                                }
                                    %>
                                    <tr>
                                        <td>
                                            <a href="editDoctorImage.jsp?id=<%= doctor.getDoctorID()%>"><img class="avatar-img rounded-circle" src="<%= (doctorImage != null && !doctorImage.isEmpty()) ? "data:image/jpeg;base64," + doctorImage : "img/team-1.jpg"%>" alt="" style="width: 50px; height: 50px;"></a>
                                        </td>
                                        <td><%= doctor.getDoctorID()%></td>
                                        <td><%= doctor.getDoctorName()%></td>
                                        <td><%= servicesDao.getServiceNameById(doctor.getServicesID())%></td>
                                        <td><%= doctor.getDoctorEmail()%></td>
                                        <td><%= doctor.getDoctorRole()%></td>
                                        <td><%= doctor.getDoctorPhone()%></td>
                                        <td><%= doctor.getDoctorGender()%></td>
                                        <td><%= doctor.getDoctorDOB()%></td>
                                        <td><%= doctor.getDoctorRegisterDate()%></td>
                                        <td>
                                            <% if (doctor.getDoctorStatus() == 1) { %>
                                            <span class="badge badge-success">Active</span>
                                            <% } else { %>
                                            <span class="badge badge-danger">Inactive</span>
                                            <% }%>
                                        </td>
                                        <td>
                                            <div class="form-button-action">
                                                <a href="editDoctorPassword.jsp?id=<%= doctor.getDoctorID()%>" data-bs-toggle="tooltip" title="Edit" class="btn btn-link btn-primary btn-lg">
                                                    <i class="fa fa-edit"></i>
                                                </a>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-button-action">
                                                <a href="editDoctor.jsp?id=<%= doctor.getDoctorID()%>" data-bs-toggle="tooltip" title="Edit" class="btn btn-link btn-primary btn-lg">
                                                    <i class="fa fa-edit"></i>
                                                </a>
                                                <button data-id="<%= doctor.getDoctorID()%>" data-bs-toggle="tooltip" title="Remove" class="btn btn-link btn-danger delete-btn">
                                                    <i class="fa fa-times"></i>
                                                </button>

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

                var doctorID = $(this).data('id');

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
                        window.location.href = 'DeleteDoctor?id=' + doctorID;
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

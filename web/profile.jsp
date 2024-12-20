<%@page import="java.util.Base64"%>
<%@page import="dao.PatientDao"%>
<%@page import="connection.DbConn"%>
<%@page import="model.Patient"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String patientImage;%>
<%
    Integer patientID = (Integer) session.getAttribute("userAuthID");
    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    Patient patient = patientDao.getPatient(patientID);

    if (patient.getPatientImage() != null && patient.getPatientImage().length > 0) {
        patientImage = Base64.getEncoder().encodeToString(patient.getPatientImage());
    } else {
        patientImage = null;
    }

    String imageError = (String) session.getAttribute("imageError");
    String imageSuccess = (String) session.getAttribute("imageSuccess");

    String nameError = (String) session.getAttribute("nameError");
    String nameSuccess = (String) session.getAttribute("nameSuccess");

    String emailError = (String) session.getAttribute("emailError");
    String emailSuccess = (String) session.getAttribute("emailSuccess");

    String passwordError = (String) session.getAttribute("passwordError");
    String passwordSuccess = (String) session.getAttribute("passwordSuccess");

    String phoneError = (String) session.getAttribute("phoneError");
    String phoneSuccess = (String) session.getAttribute("phoneSuccess");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Profile</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/profile.css">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container py-5">
            <% if (imageError != null) {%>
            <div class="alert alert-danger"><%= imageError%></div>
            <%
                    session.removeAttribute("imageError");
                } %>
            <% if (imageSuccess != null) {%>
            <div class="alert alert-success"><%= imageSuccess%></div>
            <%
                    session.removeAttribute("imageSuccess");
                }%>
            <div class="row">
                <div class="col-lg-4">
                    <form method="post" action="EditPatientImage" enctype="multipart/form-data">
                        <div class="profile-container">
                            <div class="profile-header">
                                <div class="form-group">
                                    <label for="profile-pic-input">
                                        <img 
                                            id="profile-image" 
                                            src="<%= (patientImage != null && !patientImage.isEmpty()) ? "data:image/jpeg;base64," + patientImage : "img/author.jpg"%>" 
                                            class="rounded-circle mb-2" 
                                            alt="User Photo" 
                                            style="border-radius: 8px 8px 0 0;width: 100px; height: 100px; cursor: pointer;"
                                            >
                                    </label>
                                    <input type="hidden" name="patientID" value="<%= patient.getPatientID()%>">
                                    <input 
                                        type="file" 
                                        id="profile-pic-input" 
                                        name="profileImage" 
                                        style="display:none;" 
                                        accept="image/*"
                                        onchange="previewImage(event)"
                                        >
                                    <h4><%= patient.getPatientName()%></h4>
                                    <p><%= patient.getPatientDOB()%></p>
                                </div>
                            </div>
                            <div class="p-3">
                                <button type="submit" class="btn w-100 mb-0">Change Profile Picture</button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="col-lg-8">
                    <div class="info-card">
                        <div class="container">
                            <div class="row">
                                <form method="post" action="EditPatientName">
                                    <h5>Name</h5>
                                    <div class="form-group">
                                        <input type="hidden" name="patientID" value="<%= patient.getPatientID()%>">
                                        <input type="text" name="currentName" value="<%= patient.getPatientName()%>" readonly>
                                        <input type="text" name="name" placeholder="Name">
                                    </div>
                                    <% if (nameError != null) {%>
                                    <div class="alert alert-danger"><%= nameError%></div>
                                    <%
                                            session.removeAttribute("nameError");
                                        } %>
                                    <% if (nameSuccess != null) {%>
                                    <div class="alert alert-success"><%= nameSuccess%></div>
                                    <%
                                            session.removeAttribute("nameSuccess");
                                        }%>
                                    <button class="btn btn-outline-primary btn-sm btn-add edit-btn" data-target="name">Save changes</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="info-card">
                        <div class="container">
                            <div class="row">
                                <form method="post" action="EditPatientEmail">
                                    <h5>Email Address</h5>
                                    <div class="form-group">
                                        <input type="hidden" name="patientID" value="<%= patient.getPatientID()%>">
                                        <input type="text" name="currentEmail" value="<%= patient.getPatientEmail()%>" readonly>
                                        <input type="email" name="email" placeholder="Email">
                                    </div>
                                    <% if (emailError != null) {%>
                                    <div class="alert alert-danger"><%= emailError%></div>
                                    <%
                                            session.removeAttribute("emailError");
                                        } %>
                                    <% if (emailSuccess != null) {%>
                                    <div class="alert alert-success"><%= emailSuccess%></div>
                                    <%
                                            session.removeAttribute("emailSuccess");
                                        }%>
                                    <button class="btn btn-outline-primary btn-sm btn-add edit-btn" data-target="name">Save changes</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="info-card">
                        <div class="container">
                            <div class="row">
                                <form>
                                    <h5>Gender</h5>
                                    <div class="form-group">
                                        <input type="text" name="name" placeholder="Name" value="<%= patient.getPatientGender()%>" readonly>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="info-card">
                        <div class="container">
                            <div class="row">
                                <form method="post" action="EditPatientPassword">
                                    <h5>Change Password</h5>
                                    <div class="form-group">
                                        <input type="hidden" name="patientID" value="<%= patient.getPatientID()%>">
                                        <input type="password" name="password" placeholder="Password">
                                        <input type="password" name="retype-password" placeholder="Retype-Password">
                                    </div>
                                    <% if (passwordError != null) {%>
                                    <div class="alert alert-danger"><%= passwordError%></div>
                                    <%
                                            session.removeAttribute("passwordError");
                                        } %>
                                    <% if (passwordSuccess != null) {%>
                                    <div class="alert alert-success"><%= passwordSuccess%></div>
                                    <%
                                            session.removeAttribute("passwordSuccess");
                                        }%>
                                    <button class="btn btn-outline-primary btn-sm btn-add edit-btn" data-target="name">Save changes</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="info-card">
                        <div class="container">
                            <div class="row">
                                <form method="post" action="EditPatientPhone">
                                    <h5>Phone</h5>
                                    <div class="form-group">
                                        <input type="hidden" name="patientID" value="<%= patient.getPatientID()%>">
                                        <input type="text" name="currentPhone" value="<%= patient.getPatientPhone()%>" readonly>
                                        <input type="text" name="phone" placeholder="Phone Number">
                                    </div>
                                    <% if (phoneError != null) {%>
                                    <div class="alert alert-danger"><%= phoneError%></div>
                                    <%
                                            session.removeAttribute("phoneError");
                                        } %>
                                    <% if (phoneSuccess != null) {%>
                                    <div class="alert alert-success"><%= phoneSuccess%></div>
                                    <%
                                            session.removeAttribute("phoneSuccess");
                                        }%>
                                    <button class="btn btn-outline-primary btn-sm btn-add edit-btn">Save changes</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function previewImage(event) {
                const input = event.target;
                const reader = new FileReader();

                reader.onload = function () {
                    const imgElement = document.getElementById("profile-image");
                    imgElement.src = reader.result;
                };

                if (input.files && input.files[0]) {
                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>

        <jsp:include page="footer.jsp" />
    </body>
</html>

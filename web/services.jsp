<%@page import="dao.ServicesDao"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.Services"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String servicesImage;
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    List<Services> servicesList = servicesDao.getAllServicesActive();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Services</title>
        <link rel="icon" href="img/favicon.png">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="breadcrumbs overlay">
            <div class="container">
                <div class="bread-inner">
                    <div class="row">
                        <div class="col-12">
                            <h2>Services</h2>
                            <ul class="bread-list">
                                <li><a href="home.jsp">Home</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="services.jsp">Services</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border  py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Services</p>
                    <h1>Our Services</h1>
                </div>

                <div class="row g-4">
                    <%
                        if (servicesList != null) {
                            for (Services services : servicesList) {
                                if (services.getServicesImage() != null && services.getServicesImage().length > 0) {
                                    servicesImage = Base64.getEncoder().encodeToString(services.getServicesImage());
                                } else {
                                    servicesImage = null;
                                }

                    %>
                    <div class="col-lg-4 col-md-6 wow fadeInUp" style="padding: 15px;">
                        <div class="overflow-hidden">
                            <img class="img-fluid" src="<%= (servicesImage != null && !servicesImage.isEmpty()) ? "data:image/jpeg;base64," + servicesImage : "img/team-1.jpg"%>" alt="Services Image" style="border-radius: 8px 8px 0 0;height: 250px;width: 100%">
                        </div>
                        <div class="p-4" style="border-radius: 0 0 8px 8px;background-color: #EFF5FF;height: 52%;">
                            <h4 class="mb-3"><%= services.getServicesName()%></h4>
                            <p><%= services.getServicesDesc()%></p>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>

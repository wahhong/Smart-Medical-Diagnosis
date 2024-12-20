<%@page import="java.util.Base64"%>
<%@page import="model.Drug"%>
<%@page import="java.util.List"%>
<%@page import="dao.DrugDao"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int drugID = Integer.parseInt(request.getParameter("id"));
    DrugDao drugDao = new DrugDao(DbConn.getConnection());
    Drug drug = drugDao.getDrugById(drugID);

    String errorName = (String) request.getAttribute("errorName");
    String errorDesc = (String) request.getAttribute("errorDesc");
    String errorQty = (String) request.getAttribute("errorQty");
    String errorPrice = (String) request.getAttribute("errorPrice");
    String errorExp = (String) request.getAttribute("errorExp");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Drug</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="drug.jsp">Drug</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="addDrug.jsp">Edit Drug Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditDrug">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" placeholder="ID" value="<%= drug.getDrugID()%>" disabled/>
                                        <input type="hidden" name="id" value="<%= drug.getDrugID()%>"/>
                                    </div>
                                    <div class="form-group ${errorQty != null ? 'has-error' : ''}">
                                        <label for="qty">Quantity</label>
                                        <input type="text" class="form-control" name="qty" placeholder="Enter Quantity" value="<%= request.getAttribute("qty") != null ? request.getAttribute("qty") : drug.getDrugQuantity()%>"/>
                                        <% if (errorQty != null) {%>
                                        <small class="form-text text-muted"><%= errorQty%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="1" <%= Integer.valueOf(1).equals(request.getAttribute("status") != null
                                                    ? Integer.parseInt(request.getAttribute("status").toString())
                                                    : drug.getDrugStatus()) ? "selected" : ""%>>Active</option>
                                            <option value="0" <%= Integer.valueOf(0).equals(request.getAttribute("status") != null
                                                    ? Integer.parseInt(request.getAttribute("status").toString())
                                                    : drug.getDrugStatus()) ? "selected" : ""%>>Inactive</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorName != null ? 'has-error' : ''}">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" name="name" placeholder="Enter Drug Name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : (drug.getDrugName() != null ? drug.getDrugName() : "")%>"/>
                                        <% if (errorName != null) {%>
                                        <small class="form-text text-muted"><%= errorName%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorPrice != null ? 'has-error' : ''}">
                                        <label for="price">Price</label>
                                        <input type="text" class="form-control" name="price" placeholder="Enter Price" value="<%= request.getAttribute("price") != null ? String.format("%.2f", request.getAttribute("price")) : String.format("%.2f",drug.getDrugPrice())%>"/>
                                        <% if (errorPrice != null) {%>
                                        <small class="form-text text-muted"><%= errorPrice%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorDesc != null ? 'has-error' : ''}">
                                        <label for="desc">Description</label>
                                        <input type="text" class="form-control" name="desc" placeholder="Enter Description" value="<%= request.getAttribute("desc") != null ? request.getAttribute("desc") : (drug.getDrugDesc() != null ? drug.getDrugDesc() : "")%>"/>
                                        <% if (errorDesc != null) {%>
                                        <small class="form-text text-muted"><%= errorDesc%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorExp != null ? 'has-error' : ''}">
                                        <label for="exp">Expire Date</label>
                                        <input type="date" class="form-control" id="exp" name="exp" value="<%= request.getAttribute("exp") != null ? request.getAttribute("exp") : (drug.getDrugExpire() != null ? drug.getDrugExpire() : "")%>"/>
                                        <% if (errorExp != null) {%>
                                        <small class="form-text text-muted"><%= errorExp%></small>
                                        <% }%>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="drug.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />

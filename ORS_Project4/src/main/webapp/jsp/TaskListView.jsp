<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.controller.TaskListCtl"%>
<%@page import="com.rays.pro4.Bean.TaskBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Task List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
</head>
<body>
     <%@include file="Header.jsp"%>
	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;"></h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>
            <jsp:useBean id="bean" class="com.rays.pro4.Bean.TaskBean" scope="request"></jsp:useBean>

            <div align="center">
                <h1><font color="navy">Task List</font></h1>
            </div>

            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                
                int nextPageSize = request.getAttribute("nextListSize") != null 
                    ? DataUtility.getInt(request.getAttribute("nextListSize").toString()) 
                    : 0;
                
                List list = ServletUtility.getList(request);
                if (list != null && !list.isEmpty()) {
                    Iterator it = list.iterator();
            %>

            <input type="hidden" name="pageNo" value="<%= pageNo %>">
            <input type="hidden" name="pageSize" value="<%= pageSize %>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Task Title:</b></label>
                        <input type="text" name="taskTitle" value="<%= ServletUtility.getParameter("taskTitle", request) %>">&nbsp;
                        <label><b>Creation Date:</b></label>
                        <input type="date" name="creationDate" id="udate" value="<%= ServletUtility.getParameter("creationDate", request) %>">&nbsp;
                        <label><b>Details:</b></label>
                        <input type="text" name="details" value="<%= ServletUtility.getParameter("details", request) %>">&nbsp;
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_SEARCH %>"> &nbsp;
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_RESET %>">
                    </td>
                </tr>
            </table>

            <br>

            <table border="1" style="width: 100%">
                <tr style="background-color: lavender; color: black;">
                    <th><input type="checkbox" id="selectall">
					</th>
                    <th width="5%">S.No.</th>
                    <th width="20%">Creation Date</th>
                    <th width="15%">Task Title</th>
                    <th width="20%">Details</th>
                    <th width="15%">Assigned To</th>
                    <th width="15%">Task Status</th>
                    <th>Edit</th>
                </tr>
                <%
                    while (it.hasNext()) {
                        TaskBean taskBean = (TaskBean) it.next();
                %>
                <tr align="center">
                    <td><input type="checkbox" class="case" name="ids" value="<%=taskBean.getId() %>"></td>
                    <td style="text-align: center;"><%= index++ %></td>
                    <td style="text-align: center; text-transform: capitalize;"><%= taskBean.getCreationDate() %></td>
                    <td style="text-align: center; text-transform: capitalize;"><%= taskBean.getTaskTitle() %></td>
                    <td style="text-align: center; text-transform: capitalize;"><%= taskBean.getDetails() %></td>
                    <td style="text-align: center; text-transform: capitalize;"><%= taskBean.getAssignedTo() %></td>
                    <td style="text-align: center; text-transform: capitalize;"><%= taskBean.getTaskStatus() %></td>
                    <td style="text-align: center;"><a href="<%= ORSView.TASK_CTL %>?id=<%= taskBean.getId() %>">Edit</a></td>
                </tr>
                <%
                    }
                %>
            </table>

            <br>

            <table style="width: 100%">
                <tr>
                    <td style="width: 30%">
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_PREVIOUS %>" 
                               <%= (pageNo == 1) ? "disabled" : "" %>>
                    </td>
                    <td style="width: 30%">
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_NEW %>">
                    </td>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_DELETE %>">
                    </td>
                    <td style="text-align: right;">
                        <input type="submit" name="operation" value="<%= TaskListCtl.OP_NEXT %>" 
                               <%= (nextPageSize != 0) ? "" : "disabled" %>>
                    </td>
                </tr>
            </table>
            <%
				}
				if (list.size() == 0) {
			%>
            <br>
            <table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=TaskListCtl.OP_BACK%>"></td>
				</tr>
			</table>
			<%
				}
			%>
        </form>
    </div>
    <%@ include file="Footer.jsp" %>

</body>
</html>

<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.TaskCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Add Task page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2002',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>


</head>
<body>
	<form action="<%=ORSView.TASK_CTL%>" method="post">
		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="com.rays.pro4.Bean.TaskBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1>
				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th>Update Task</th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th>Add Task</th>
				</tr>
				<%
					}
				%>
			</h1>

			<h3>
				<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>
			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>
				<tr>
					<th>Creation Date:</th>
					<td><input type="text" name="creationDate" id="udate"
						placeholder="Enter Creation Date" size="26"
						value="<%=DataUtility.getDateString(bean.getCreationDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("creationDate", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th>Task Title:</th>
					<td><input type="text" name="taskTitle"
						placeholder="Enter Task Title" size="26"
						value="<%=DataUtility.getStringData(bean.getTaskTitle())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("taskTitle", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th>Details:</th>
					<td><input type="text" name="details"
						placeholder="Enter Details" size="26"
						value="<%=DataUtility.getStringData(bean.getDetails())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("details", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th>Assigned To:</th>
					<td>
						<%
							HashMap map = new HashMap();
							map.put("prince", "prince");
							map.put("avnish", "avnish");
							map.put("abhishek", "abhishek");
							map.put("aryan", "aryan");

							String hlist = HTMLUtility.getList("assignedTo", bean.getAssignedTo(), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("assignedTo", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th>Task Status:</th>
					<td>
						<%
							HashMap map1 = new HashMap();
							map1.put("New", "New");
							map1.put("Started", "Started");
							map1.put("On Hold", "On Hold");
							map1.put("Completed", "Completed");
							map1.put("Closed", "Closed");
							String hlist1 = HTMLUtility.getList("taskStatus", bean.getTaskStatus(), map1);
						%> <%=hlist1%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("taskStatus", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<th></th>
				<%
					if (bean.getId() > 0) {
				%>
				<td>&nbsp; &emsp; <input type="submit" name="operation"
					value="<%=TaskCtl.OP_UPDATE%>"> &nbsp; &nbsp; <input
					type="submit" name="operation" value="<%=TaskCtl.OP_CANCEL%>">
				</td>
				<%
					} else {
				%>

				<td>&nbsp; &emsp; <input type="submit" name="operation"
					value="<%=TaskCtl.OP_SAVE%>"> &nbsp; &nbsp; &nbsp; &nbsp;
					&nbsp; &nbsp; <input type="submit" name="operation"
					value="<%=TaskCtl.OP_RESET%>">
				</td>
				<%
					}
				%>
				</tr>
			</table>
		</div>
	</form>

</body>
</html>
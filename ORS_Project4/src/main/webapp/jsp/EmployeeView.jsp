<%@page import="com.rays.pro4.controller.EmployeeCtl"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Employee Registration Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udate4").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2007',

		});
	});
</script>


</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.EmployeeBean"
		scope="request"></jsp:useBean>

	<form action="<%=ORSView.EMPLOYEE_CTL%>" method="post">
		<%@include file="Header.jsp"%>



		<center>
			<h1>
				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th><font>Update Employee</font></th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th><font>Add Employee</font></th>
				</tr>
				<%
					}
				%>
			</h1>

			<div>
				<h3>
					<font style="color: green"><%=ServletUtility.getSuccessMessage(request)%></font>
					</h1>
					<h3>
						<font style="color: red"><%=ServletUtility.getErrorMessage(request)%></font>
						</h1>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdby" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedby"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createddatetime" value="<%=bean.getCreatedDatetime()%>">
			<input type="hidden" name="modifieddatetime"
				value="<%=bean.getModifiedDatetime()%>">

			<table>





				<tr>
					<th align="left">FullName <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="fullName"
						placeholder="Enter Full Name" size="25"
						value="<%=DataUtility.getStringData(bean.getFullName())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("fullName", request)%></font>
					</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">UserName <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="userName"
						placeholder="Enter User Name" size="25"
						value="<%=DataUtility.getStringData(bean.getUserName())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("userName", request)%></font>
					</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left">Password <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="password"
						placeholder="Enter Password" size="25"
						value="<%=DataUtility.getStringData(bean.getPassword())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("password", request)%></font>
					</td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left">Date Of Birth <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="birthDate" id="udate4"
						readonly="readonly" placeholder=" Date of Birth" size="25"
						value="<%=DataUtility.getDateString(bean.getBirthDate())%>"></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("birthDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">ContactNumber <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="contactNumber" maxlength="10"
						placeholder="Enter Contact Number" size="25"
						value="<%=DataUtility.getStringData(bean.getContactNumber())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("contactNumber", request)%></font>
					</td>
				</tr>


				<tr>
					<th style="padding: 3px"></th>
				</tr>


				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td>&nbsp; &emsp; <input type="submit" name="operation"
						value="<%=EmployeeCtl.OP_UPDATE%>"> &nbsp; &nbsp; <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_CANCEL%>"></td>
					<%
						} else {
					%>
					<td>&nbsp; &emsp; <input type="submit" name="operation"
						value="<%=EmployeeCtl.OP_SAVE%>"> &nbsp; &nbsp; <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>

			</table>
	</form>
	</center>

	<%@ include file="Footer.jsp"%>
</body>
</html>

package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.EmployeeBean;
import com.rays.pro4.Bean.EmployeeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.EmployeeModel;
import com.rays.pro4.Model.EmployeeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "EmployeeCtl", urlPatterns = { "/ctl/EmployeeCtl" })
public class EmployeeCtl extends BaseCtl {

	/** The log. */
	private static Logger log = Logger.getLogger(EmployeeCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#preload(javax.servlet.http.
	 * HttpServletRequest)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("validate started ... std ctl");
		log.debug("EmployeeCtl Method validate Started");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "Full Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.name", "Full Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.require", "User Name"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.email", "User Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", "Contact Number . must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.require", "Date Of Birth"));
			pass = false;
		} else if (!DataValidator.isAge(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", "Employee Age must be Greater then 18 year ");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		System.out.println("validate over ,.... Employee ctl");
		log.debug("EmployeeCtl Method validate Ended");
		return pass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("EmployeeCtl Method populatebean Started");

		EmployeeBean bean = new EmployeeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));
		bean.setUserName(DataUtility.getString(request.getParameter("userName")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		System.out.println("dob" + bean.getBirthDate());
		bean.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

		populateDTO(bean, request);
		log.debug("EmployeeCtl Method populatebean Ended");
		return bean;
	}

	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EmployeeCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		// get model

		EmployeeModel model = new EmployeeModel();
		if (id > 0 || op != null) {
			EmployeeBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("EmployeeCtl Method doGett Ended");
	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EmployeeCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));
		// get model

		EmployeeModel model = new EmployeeModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			EmployeeBean bean = (EmployeeBean) populateBean(request);
			try {
				if (id > 0) {
					model.Update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage(" Employee is successfully Updated", request);
				} else {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage(" Employee is successfully Added", request);
					// bean.setId(pk);
				}
				ServletUtility.setBean(bean, request);
				// ServletUtility.setSuccessMessage(" Employee is successfully Added",request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Employee Email Id already exists", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
			return;
		}
		/*
		 * else if (OP_DELETE.equalsIgnoreCase(op)) {
		 * 
		 * EmployeeBean bean = (EmployeeBean) populateBean(request); try {
		 * model.delete(bean); ServletUtility.redirect(ORSView.Employee_CTL, request,
		 * response); return;
		 * 
		 * } catch (ApplicationException e) { log.error(e);
		 * ServletUtility.handleException(e, request, response); return; } }
		 */ ServletUtility.forward(getView(), request, response);

		log.debug("EmployeeCtl Method doPost Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_VIEW;
	}

}

package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Bean.TaskBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Model.TaskModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "TaskListCtl", urlPatterns = { "/ctl/TaskListCtl" })
public class TaskListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(SubjectListCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		TaskBean bean = new TaskBean();
		bean.setTaskTitle(DataUtility.getString(request.getParameter("taskTitle")));
		bean.setCreationDate(DataUtility.getDate(request.getParameter("creationDate")));
		bean.setDetails(DataUtility.getString(request.getParameter("details")));
		bean.setAssignedTo(DataUtility.getString(request.getParameter("assignedTo")));
		bean.setTaskStatus(DataUtility.getString(request.getParameter("taskStatus")));
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TaskListCtl doGet Start");

		System.out.println("insdie get method Task ctl");
		List list = null;
		List next = null;

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TaskBean bean = (TaskBean) populateBean(request);

		@SuppressWarnings("unused")
		String op = DataUtility.getString(request.getParameter("operation"));

		TaskModel model = new TaskModel();
		try {

			list = model.search(bean, pageNo, pageSize);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			next = model.search(bean, pageNo + 1, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ServletUtility.setList(list, request);
		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		request.setAttribute("nextListSize", next.size());
		ServletUtility.setList(list, request);
		Collections.sort(list);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("TaskListCtl doGet End");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TaskListCtl doPost Start");
		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		TaskBean bean = (TaskBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		TaskModel model = new TaskModel();

		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.TASK_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					TaskBean deletebean = new TaskBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(DataUtility.getLong(id));
						ServletUtility.setSuccessMessage("Data is deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.TASK_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			Collections.sort(list);
			ServletUtility.setBean(bean, request);
			next = model.search(bean, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (!OP_DELETE.equalsIgnoreCase(op)) {
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			// ServletUtility.setList(list, request);
			// ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("TaskListCtl doGet End");
	}

	@Override
	protected String getView() {
		return ORSView.TASK_LIST_VIEW;
	}

}

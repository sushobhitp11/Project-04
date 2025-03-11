package com.rays.pro4.controller;


import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.TaskBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.TaskModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet (name = "TaskCtl" , urlPatterns = {"/ctl/TaskCtl"})
public class TaskCtl extends BaseCtl {
	
    private static final long serialVersionUID = 1L;
	
	/** The log. */
	private static Logger log = Logger.getLogger(TaskCtl.class);
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("validate Method of Task Ctl start");
		System.out.println("validate inn");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("creationDate"))) {
			request.setAttribute("creationDate", PropertyReader.getValue("error.require", "CreationDate"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("taskTitle"))) {
			request.setAttribute("taskTitle", PropertyReader.getValue("error.require", "Task Title"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("details"))) {
			request.setAttribute("details", PropertyReader.getValue("error.require", "Details"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("assignedTo"))) {
			request.setAttribute("assignedTo", PropertyReader.getValue("error.require", "Assigned To"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("taskStatus"))) {
			request.setAttribute("taskStatus", PropertyReader.getValue("error.require", "Task Status"));
			pass = false;
		}
		log.debug("validate Method of Task Ctl  End");
		System.out.println("validate out");
		return pass;
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("Populate bean Method of Task Ctl start");
		System.out.println("populate bean enter");
		
		TaskBean bean = new TaskBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setCreationDate(DataUtility.getDate(request.getParameter("creationDate")));
//		System.out.println("date => " + request.getParameter("creationDate"));
		bean.setTaskTitle(DataUtility.getString(request.getParameter("taskTitle")));
		bean.setDetails(DataUtility.getString(request.getParameter("details")));
		bean.setAssignedTo(DataUtility.getString(request.getParameter("assignedTo")));
		System.out.println("assignedTo => " + request.getParameter("assignedTo"));
		bean.setTaskStatus(DataUtility.getString(request.getParameter("taskStatus")));
		populateDTO(bean, request);
		
		log.debug("PopulateBean Method of Task Ctl End");
		System.out.println("populate bean out");
		return bean;

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.debug("Do get Method of Task Ctl start ");
		System.out.println("do get in ");
		String op = DataUtility.getString(request.getParameter("operation"));
		
		TaskModel model = new TaskModel();
		TaskBean bean = null;
		long id =DataUtility.getLong(request.getParameter("id"));
		
		if(id > 0 || op != null){
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} 
				catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
				return;
				}
		}
		System.out.println("do get out");
		log.debug("Do get Method of Task Ctl End");
		ServletUtility.forward(getView(), request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		log.debug("Do post Method of Task Ctl start");
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		
		TaskModel model = new TaskModel();	
		
		TaskBean bean = (TaskBean)populateBean(request);
		
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {	
			
		//	System.out.println("post in operaion save  ");
		try{	
			if(id > 0){
				model.Update(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage(" Task is Succesfully Updated ", request);
			
			}else{
				System.out.println("date =>>>>>>>>>>>>>>>>>>>>> " + bean.getCreationDate().getTime());
				long pk = model.add(bean);
				ServletUtility.setSuccessMessage(" Task is Succesfully Added ", request);
		//		bean.setId(pk);
			}
			ServletUtility.setBean(bean, request);
			//ServletUtility.setSuccessMessage(" Task is Succesfully Added ", request);
		}catch(ApplicationException e){
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("TaskTitle already Exsist", request);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TASK_CTL, request, response);
			return;
		}
		else if (OP_CANCEL.equalsIgnoreCase(op) ) {
			ServletUtility.redirect(ORSView.TASK_LIST_CTL, request, response);
			return;
		}
		
	
		ServletUtility.forward(getView(), request, response);
		log.debug("Do post Method of Task Ctl End");
		}


	@Override
	protected String getView() {
		return ORSView.TASK_VIEW;
	}

}


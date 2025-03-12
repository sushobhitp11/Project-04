package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.rays.pro4.Bean.TaskBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.JDBCDataSource;

public class TaskModel {

	private static Logger log = Logger.getLogger(StudentModel.class);

	public Integer nextPk() throws Exception {

		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_task");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception .....", e);
			throw new DatabaseException("Exception :Exception in getting PK");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPk End");
		return pk + 1;
	}

	public long add(TaskBean bean) throws Exception {

		log.debug("Model add Started");
		System.out.println("add started");

		Connection conn = null;

		TaskBean duplicateName = findByTaskTitle(bean.getTaskTitle());
		int pk = 0;
		if (duplicateName != null) {
			throw new DuplicateRecordException("TaskTitle already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPk();

			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("insert into st_task values(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);

			pstmt.setDate(2, new java.sql.Date(bean.getCreationDate().getTime()));
			pstmt.setString(3, bean.getTaskTitle());
			pstmt.setString(4, bean.getDetails());
			pstmt.setString(5, bean.getAssignedTo());
			pstmt.setString(6, bean.getTaskStatus());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			pstmt.executeUpdate();

			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();

			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}

	public void Update(TaskBean bean) throws Exception {
		log.debug("Model Update Started");
		Connection conn = null;
		TaskBean beanExist = findByTaskTitle(bean.getTaskTitle());

		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Task Title is already exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_task SET creation_date=?,task_title=?,details=?,assigned_to=?,task_status=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setDate(1, new java.sql.Date(bean.getCreationDate().getTime()));
			pstmt.setString(2, bean.getTaskTitle());
			pstmt.setString(3, bean.getDetails());
			pstmt.setString(4, bean.getAssignedTo());
			pstmt.setString(5, bean.getTaskStatus());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception....", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				// throw new ApplicationException("Exception : Delete rollback
				// exception"+ex.getMessage());
			}
			// throw new ApplicationException("Exception in updating Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");

	}

	public void delete(long id) throws Exception {

		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_task where id=?");
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception.." + e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : delete rollback exception  " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete End");
	}

	public TaskBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("select * from st_task where id=?");
		TaskBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TaskBean();
				bean.setId(rs.getLong(1));
				bean.setCreationDate(rs.getDate(2));
				bean.setTaskTitle(rs.getString(3));
				bean.setDetails(rs.getString(4));
				bean.setAssignedTo(rs.getString(5));
				bean.setTaskStatus(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;

	}

	private TaskBean findByTaskTitle(String taskTitle) throws Exception {
		log.debug("Model findBy TaskTitle Started");
		StringBuffer sql = new StringBuffer("select * from st_task where id=?");
		TaskBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, taskTitle);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TaskBean();
				bean.setId(rs.getLong(1));
				bean.setCreationDate(rs.getDate(2));
				bean.setTaskTitle(rs.getString(3));
				bean.setDetails(rs.getString(4));
				bean.setAssignedTo(rs.getString(5));
				bean.setTaskStatus(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			// throw new ApplicationException("Exception : Exception in getting User by
			// Email");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy Email End");
		return bean;
	}

	public List list() throws Exception {
		return search(null, 0, 0);
	}

	public List search(TaskBean bean) throws Exception {
		return search(bean, 0, 0);
	}

	public List search(TaskBean bean, int pageNo, int pageSize) throws Exception {
		log.debug("Model search Started");

		StringBuffer sql = new StringBuffer("select * from st_task where 1=1");

		if (bean != null) {
			if (bean.getTaskTitle() != null && bean.getTaskTitle().length() > 0) {
				sql.append(" and task_title like '" + bean.getTaskTitle() + "%'");
			}
			if (bean.getCreationDate() != null && bean.getCreationDate().getTime() > 0) {
				Date d = new Date(bean.getCreationDate().getTime());
				sql.append(" AND creation_Date = " + DataUtility.getDateString(d));
			}
			if (bean.getDetails() != null && bean.getDetails().length() > 0) {
				sql.append(" AND details like '" + bean.getDetails() + "%'");
			}
			if(bean.getTaskStatus()!= null && bean.getTaskStatus().length()> 0) {
				sql.append(" and task_status like '" + bean.getTaskStatus() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		System.out.println("sql ==>> " + sql.toString());

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TaskBean();
				bean.setId(rs.getLong(1));
				bean.setCreationDate(rs.getDate(2));
				bean.setTaskTitle(rs.getString(3));
				bean.setDetails(rs.getString(4));
				bean.setAssignedTo(rs.getString(5));
				bean.setTaskStatus(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}
}

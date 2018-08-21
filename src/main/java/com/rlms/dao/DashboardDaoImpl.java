package com.rlms.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.DateUtils;

@Repository
public class DashboardDaoImpl implements DashboardDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts() {

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsLiftAmcDtls.class);
		criteria.createAlias("liftCustomerMap", "lcm");
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		criteria.addOrder(Order.asc("craetedDate"));
		List<RlmsLiftAmcDtls> listOFAMCdtlsForAllLifts = criteria.list();
		return listOFAMCdtlsForAllLifts;

	}
	@SuppressWarnings("unchecked")
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(
			Integer branchCompanyMapId, Integer branchCustomerMapId,
			List<Integer> listOfLiftCustoMapId, List<Integer> statusList,
			Date fromDate, Date toDate,Integer callType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsComplaintMaster.class);
		criteria.createAlias("liftCustomerMap.branchCustomerMap", "bcm");
		criteria.createAlias("bcm.companyBranchMapDtls", "cbm");
		if (null != branchCompanyMapId) {
			criteria.add(Restrictions.eq("cbm.companyBranchMapId",
					branchCompanyMapId));
		}
		if (null != branchCustomerMapId
				&& !RLMSConstants.MINUS_ONE.getId().equals(branchCustomerMapId)) {
			criteria.add(Restrictions.eq("bcm.branchCustoMapId",
					branchCustomerMapId));
		}
		if (null != listOfLiftCustoMapId && !listOfLiftCustoMapId.isEmpty()) {
			criteria.add(Restrictions.in("liftCustomerMap.liftCustomerMapId",
					listOfLiftCustoMapId));
		}
		if (null != fromDate && null != toDate) {
			criteria.add(Restrictions.ge("registrationDate", fromDate));
			criteria.add(Restrictions.le("registrationDate", toDate));
		}
		if (null != statusList && !statusList.isEmpty()) {
			criteria.add(Restrictions.in("status", statusList));
		}
		if(null != callType){
			 criteria.add(Restrictions.eq("callType", callType));
		 }
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsComplaintMaster> listOfAllcomplaints = criteria.list();
		return listOfAllcomplaints;
	}

	@SuppressWarnings("unchecked")
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(
			Integer complaintId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(
				RlmsComplaintTechMapDtls.class).add(
				Restrictions.eq("complaintMaster.complaintId", complaintId));
		RlmsComplaintTechMapDtls complaintMapDtls = (RlmsComplaintTechMapDtls) criteria
				.uniqueResult();
		return complaintMapDtls;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsUserRoles> getAllUserWithRoleFor(
			List<Integer> commpBranchMapId, Integer spocRoleId) {
		Session session = this.sessionFactory.getCurrentSession();
		/*
		 * Criteria criteria = session.createCriteria(RlmsUserRoles.class)
		 * .add(Restrictions.in("rlmsCompanyBranchMapDtls.companyBranchMapId",
		 * commpBranchMapId))
		 * .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId));
		 * //.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 * List<RlmsUserRoles> listOfAllTechnicians = criteria.list();
		 */
		String str = "";
		for (Integer mapId : commpBranchMapId) {
			if (StringUtils.isEmpty(str)) {
				str = str.concat(String.valueOf(mapId));
			} else {
				str = str.concat("," + mapId);
			}
		}

	/*	String sql = "select a.* from rlms_db.rlms_user_roles a where (a.user_id,a.updated_date,a.company_branch_map_id,a.spoc_role_id) in (SELECT user_id,max(updated_date) max_date,company_branch_map_id,spoc_role_id FROM rlms_db.rlms_user_roles where company_branch_map_id in ("
				+ str
				+ ") and spoc_role_id="
				+ spocRoleId
				+ " and user_id=a.user_id and updated_date < now() order by ABS(DATEDIFF( updated_date, now())))group by a.user_id,a.company_branch_map_id,a.spoc_role_id";*/
		
		
		String sql = "select * from rlms_user_roles a where  spoc_role_id ="+ spocRoleId+" and company_branch_map_id in ("+ str+")";
				
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(RlmsUserRoles.class);
		List<RlmsUserRoles>listOfAllTechnicians = query.list();
		return listOfAllTechnicians;
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getTechnicianActiveStatusCountByCompanyBranchMap(
			Integer  commpBranchMapId, Integer spocRoleId) {
		Session session = this.sessionFactory.getCurrentSession();
		
		String sql ="select active_flag,count(*) from rlms_user_roles where company_branch_map_id in ("+commpBranchMapId+") and spoc_role_id="+spocRoleId+" group by active_flag";
    	SQLQuery query = session.createSQLQuery(sql);
		
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getTechnicianCountByCompanyBranchMap(
			List<Integer> commpBranchMapId, Integer spocRoleId) {
		Session session = this.sessionFactory.getCurrentSession();
		
		String str = "";
		for (Integer mapId : commpBranchMapId) {
			if (StringUtils.isEmpty(str)) {
				str = str.concat(String.valueOf(mapId));
			} else {
				str = str.concat("," + mapId);
			}
		}
	  String sql ="select company_branch_map_id,count(*) from rlms_user_roles where company_branch_map_id in ("+ str+") and spoc_role_id="+ spocRoleId+"  group by company_branch_map_id ";
    	SQLQuery query = session.createSQLQuery(sql);
		List<Object[]>techniciansCount = query.list();
		return techniciansCount;
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(
			Integer companyId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session
				.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
		// criteria.add(Restrictions.eq("activeFlag",
		// RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;

	}

	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranchDtlsForDashboard(
			List<Integer> ListOfCompanyIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session
				.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.in("rlmsCompanyMaster.companyId",
				ListOfCompanyIds));
		// criteria.add(Restrictions.eq("activeFlag",
		// RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;
	}
	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtlsForDashboard(
			Integer compBranchMapId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(
				RlmsCompanyBranchMapDtls.class).add(
				Restrictions.eq("companyBranchMapId", compBranchMapId));
		// .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId())
		RlmsCompanyBranchMapDtls companyBranchMapDtls = (RlmsCompanyBranchMapDtls) criteria
				.uniqueResult();
		return companyBranchMapDtls;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RlmsEventDtls> getAllEventDtlsForDashboard(
			List<Integer> liftCustMapIds,String eventType) {
		    List<RlmsEventDtls> eventDtls = new ArrayList<>();
		    try {
		    	Session session = this.sessionFactory.getCurrentSession();
		    	Criteria criteria = session.createCriteria(RlmsEventDtls.class).add(
		    			Restrictions.in("rlmsLiftCustomerMap.liftCustomerMapId", liftCustMapIds));
		    	if(eventType!=null&&eventType=="-1") {
		    		criteria.add(Restrictions.ne("eventType","RES"));
		    	}
		    	if(eventType!=null&&eventType!="-1") {
		    		criteria.add(Restrictions.eq("eventType",eventType));
		    	}
		    	criteria.addOrder(Order.desc("generatedDate"));
		    	eventDtls = criteria.list();
		    } catch (HibernateException e) {
		    	e.printStackTrace();
		    }
		    return eventDtls;
	}

	@Override
	public void saveEventDtls(RlmsEventDtls eventDtls){
		this.sessionFactory.getCurrentSession().save(eventDtls);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<RlmsEventDtls>getListOfEventsByType(RlmsEventDtls rlmsEventDtls) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(RlmsEventDtls.class);
			/*	add(Restrictions.eq("eventType", rlmsEventDtls.getEventType()));*/
				
		return criteria.list();
	}

@Override
	public List<Object[]> getEventCountDtlsForDashboard(
			List<Integer> liftCustMapIds) {
		
		String str = "";
		for (Integer mapId : liftCustMapIds) {
			if (StringUtils.isEmpty(str)) {
				str = str.concat(String.valueOf(mapId));
			} else {
				str = str.concat("," + mapId);
			}
		}
		Session session = this.sessionFactory.getCurrentSession();
       String sql ="SELECT lift_customer_map_id,event_type,count(*) FROM rlms_event where lift_customer_map_id in("+str+") group by event_type,lift_customer_map_id order by lift_customer_map_id";
	    	SQLQuery query = session.createSQLQuery(sql);
		 	List<Object[]>EventCount = query.list();
			return EventCount;
		
	}

	@Override
	public List<Object[]> getTodaysEventCountDtlsForDashboard(
			List<Integer> liftCustMapIds) {
		
		String str = "";
		for (Integer mapId : liftCustMapIds) {
			if (StringUtils.isEmpty(str)) {
				str = str.concat(String.valueOf(mapId));
			} else {
				str = str.concat("," + mapId);
			}
		}
		Session session = this.sessionFactory.getCurrentSession();
       String sql ="SELECT lift_customer_map_id,event_type,count(*) FROM rlms_event where (DATE(generated_date)=CURDATE()) and lift_customer_map_id in("+str+") group by event_type,lift_customer_map_id order by lift_customer_map_id";
	    	SQLQuery query = session.createSQLQuery(sql);
		 	List<Object[]>EventCount = query.list();
			return EventCount;
		
	}

@Override
public List<Object[]> getBranchCountDtlsForDashboard(List<Integer> branchIds) {
	String str = "";
	for (Integer mapId : branchIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	String sql ="SELECT city,active_flag,count(*) FROM rlms_branch_master  where branch_id in ("+str+") group by active_flag,city order by city" ;
  // String sql ="SELECT city,count(*) FROM rlms_branch_master  where branch_id in("+str+") group by city";
    	SQLQuery query = session.createSQLQuery(sql);
	 	List<Object[]>EventCount = query.list();
		return EventCount;
}
@Override
public List<Object[]> getTotalComplaintsCallTypeCount(List<Integer> liftCustomerMapIds) {
	 Date pivotDate = DateUtils.addDaysToDate(new Date(), -30);
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 String fromDatet = formatter.format(pivotDate);
	 String str = "";
	for (Integer mapId : liftCustomerMapIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	String sql ="SELECT call_type,count(*) FROM rlms_complaint_master where (registration_date >= '"+fromDatet+"') and  registration_date <= NOW() and lift_customer_map_id in ("+str+") group by call_type";
		SQLQuery query = session.createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object[]>complaintCount = query.list();
		return complaintCount;
	}

@Override
public List<Object[]> getTodaysComplaintsCallTypeCount(List<Integer> liftCustomerMapIds) {
	String str = "";
	for (Integer mapId : liftCustomerMapIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	String sql ="SELECT call_type,count(*) FROM rlms_complaint_master where (DATE(registration_date)=CURDATE()) and lift_customer_map_id in ("+str+") group by call_type";
	SQLQuery query = session.createSQLQuery(sql);
	 	@SuppressWarnings("unchecked")
		List<Object[]>complaintCount = query.list();
		return complaintCount;
	}
@Override
public List<Object[]> getTotalComplaintsStatusCount(List<Integer> liftCustomerMapIds) {
	 Date pivotDate = DateUtils.addDaysToDate(new Date(), -30);
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 String fromDatet = formatter.format(pivotDate);
	String str = "";
	for (Integer mapId : liftCustomerMapIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	String sql ="SELECT call_type,status,count(*) FROM rlms_complaint_master where  (registration_date >='"+fromDatet+"' )and  registration_date <=NOW() and lift_customer_map_id in ("+str+") group by status,call_type";
     SQLQuery query = session.createSQLQuery(sql);
	 	@SuppressWarnings("unchecked")
		List<Object[]>complaintCount = query.list();
		return complaintCount;
	}

@Override
public List<Object[]> getTodaysComplaintsStatusCount(List<Integer> liftCustomerMapIds) {
	String str = "";
	for (Integer mapId : liftCustomerMapIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	//String sql = "SELECT status,count(*) FROM rlms_complaint_master where lift_customer_map_id in("+str+") group by status";	
	String sql ="SELECT call_type,status,count(*) FROM rlms_complaint_master where (DATE(registration_date)=CURDATE()) and lift_customer_map_id in ("+str+") group by status,call_type";
   SQLQuery query = session.createSQLQuery(sql);
	 	@SuppressWarnings("unchecked")
		List<Object[]>complaintCount = query.list();
		return complaintCount;
	}
@Override
public List<Object[]> getTodaysTotalComplaintsStatusCount(List<Integer> liftCustomerMapIds) {
	String str = "";
	for (Integer mapId : liftCustomerMapIds) {
		if (StringUtils.isEmpty(str)) {
			str = str.concat(String.valueOf(mapId));
		} else {
			str = str.concat("," + mapId);
		}
	}
	Session session = this.sessionFactory.getCurrentSession();
	String sql ="SELECT call_type,status,count(*) FROM rlms_complaint_master where (DATE(updated_date)=CURDATE()) and status in("+Status.ASSIGNED.getStatusId()+","+Status.RESOLVED.getStatusId()+") and lift_customer_map_id in ("+str+") group by status,call_type";
	SQLQuery query = session.createSQLQuery(sql);
	 	@SuppressWarnings("unchecked")
		List<Object[]>complaintCount = query.list();
		return complaintCount;
	}

@Override
public List<RlmsComplaintMaster> getAllComplaintsForAvgLogs(Date fromDate, Date toDate, ComplaintsDtlsDto dto) {
	Session session = this.sessionFactory.getCurrentSession();
	Criteria criteria = session.createCriteria(RlmsComplaintMaster.class);
	criteria.add(Restrictions.ge("registrationDate",fromDate));
	criteria.add(Restrictions.le("registrationDate",toDate));
	criteria.add(Restrictions.in("liftCustomerMap.liftCustomerMapId", dto.getListOfLiftCustoMapId()));
	List<RlmsComplaintMaster>complaintList = criteria.list();
	return complaintList;
}

@Override
public List<RlmsEventDtls> getUnidentifiedEventCountDtlsForDashboard() {
	 Session session = this.sessionFactory.getCurrentSession();
	 Criteria criteria = session.createCriteria(RlmsEventDtls.class);
	 criteria.add(Restrictions.eq("rlmsLiftCustomerMap.liftCustomerMapId",null));
	 List<RlmsEventDtls>eventList = criteria.list();
	 return eventList;
 	}
}

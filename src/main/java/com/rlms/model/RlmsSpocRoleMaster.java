package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "rlms_spoc_role_m")
public class RlmsSpocRoleMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer spocRoleId;
	private String roleName;
	private Integer roleLevel;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	private Integer activeFlag;
	
	public RlmsSpocRoleMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "spoc_role_id", unique = true, nullable = false)
	public Integer getSpocRoleId() {
		return spocRoleId;
	}

	public void setSpocRoleId(Integer spocRoleId) {
		this.spocRoleId = spocRoleId;
	}

	@Column(name = "role_name", unique = true, nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Column(name = "role_level", unique = true, nullable = false)
	public Integer getRoleLevel() {
		return roleLevel;
	}


	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	
	
}

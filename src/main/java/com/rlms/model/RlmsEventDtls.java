package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_event")
public class RlmsEventDtls implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer eventId;
	private String eventType;
	private String eventDescription;
	
	private String eventService;
	private String imeiId;
	private String eventCode;
	private int floorNo;
	private String FromContact;
	private Date eventDate;
	
	private RlmsLiftCustomerMap  rlmsLiftCustomerMap;
	
	private Date generatedDate;	
	private Integer generatedBy;
	private Date updatedDate;
	private Integer updatedBy;
	private Integer activeFlag;
	private String lmsResponseUserContactNo;
	
	public RlmsEventDtls() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "event_id", unique = true, nullable = false)
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	@Column(name = "event_type", unique = true, nullable = false)
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Column(name = "event_description")
	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	@Column(name = "generated_date")
	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	@Column(name = "generated_by")
	public Integer getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(Integer generatedBy) {
		this.generatedBy = generatedBy;
	}

	@Column(name = "updated_date")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by")
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "active_flag")
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Column(name="service_type")
	public String getEventService() {
		return eventService;
	}

	public void setEventService(String eventService) {
		this.eventService = eventService;
	}

	@Column(name="imei")
	public String getImeiId() {
		return imeiId;
	}

	public void setImeiId(String imeiId) {
		this.imeiId = imeiId;
	}
    
	@Column(name="event_code")
	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	@Column(name="floor_no")
	public int getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(int floorNo) {
		this.floorNo = floorNo;
	}
    
	@Column(name="lms_contact_no")
	public String getFromContact() {
		return FromContact;
	}

	public void setFromContact(String fromContact) {
		FromContact = fromContact;
	}
    @Column(name="event_date")
	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_customer_map_id")
	public RlmsLiftCustomerMap getRlmsLiftCustomerMap() {
		return rlmsLiftCustomerMap;
	}
	public void setRlmsLiftCustomerMap(RlmsLiftCustomerMap rlmsLiftCustomerMap) {
		this.rlmsLiftCustomerMap = rlmsLiftCustomerMap;
	}
	@Column(name="lms_response_user_contact_no")
	public String getLmsResponseUserContactNo() {
		return lmsResponseUserContactNo;
	}

	public void setLmsResponseUserContactNo(String lmsResponseUserContactNo) {
		this.lmsResponseUserContactNo = lmsResponseUserContactNo;
	}
		
}

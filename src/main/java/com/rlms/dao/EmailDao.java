package com.rlms.dao;

import com.rlms.model.EmailTemplate;

public interface EmailDao {
	public EmailTemplate getEmailTemplate(Integer templateId);
}

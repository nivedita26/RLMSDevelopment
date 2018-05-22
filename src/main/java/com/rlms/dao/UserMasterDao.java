package com.rlms.dao;

import java.util.List;

import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;



public interface UserMasterDao{
  
   public RlmsUsersMaster getUserByUserNameAndPassword(String username, String password);
   public List<RlmsUsersMaster> getAllUsersForCompany(Integer companyId);
   public RlmsUsersMaster getUserByUserId(Integer userId);
   public void saveUser(RlmsUsersMaster usermMaster);
   public void updateUser(RlmsUsersMaster usermMaster);
   public void mergerUser(RlmsUsersMaster usermMaster);
   public void mergerUserRole(RlmsUserRoles userRoles);
   public RlmsUsersMaster getUserByEmailID(String emailId);
   public void deleteUser(UserDtlsDto dto, UserMetaInfo metaInfo);
}

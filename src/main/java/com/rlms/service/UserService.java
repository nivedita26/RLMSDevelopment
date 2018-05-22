package com.rlms.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.RegisterDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCompanyRoleMap;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.model.User;



public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers(); 
	
	void deleteAllUsers();
	
	public boolean isUserExist(User user);
	
	public RlmsUserRoles getUserByUserName(String userName);
	public RlmsUserRoles getUserRoleObj(Integer userID, String userName, String password);
	public List<RlmsSpocRoleMaster> getAllRoles(UserMetaInfo metaInfo);
	public List<UserDtlsDto> getAllUsersForCompany( Integer companyId);
	
	public String validateAndAssignRole(UserRoleDtlsDTO userRoleDtlsDTO, UserMetaInfo metaInfo) throws IOException, ValidationException, InvalidKeyException, Exception;
	
	public String registerUser(RegisterDto dto) throws IOException, InvalidKeyException, Exception;
	
	public String validateAndRegisterNewUser(AddNewUserDto userDto, UserMetaInfo metaInfo) throws ValidationException;
	
//	public RlmsCompanyRoleMap getTechnicianRoleForBranch(Integer commpBranchMapId);
	
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer spocRoleId);
	
	public List<RlmsUserRoles> getListOfTechniciansForBranch(Integer compBranchMapId);
	
	public List<UserDtlsDto> getAllRegisteredUsers(Integer compamyId, UserMetaInfo metaInfo);
	
	public UserDtlsDto registerTechnicianDeviceByMblNo(UserDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException;
	
	public RlmsUserRoles getUserRoleObjhById(Integer userRoleId);
	
	public String validateAndEditUser(UserDtlsDto userDto, UserMetaInfo metaInfo)
			throws ValidationException;
	public String deleteUserObj(UserDtlsDto dto, UserMetaInfo metaInfo);

	public String updateTechnicianLocation(UserDtlsDto userDtlsDto,
			UserMetaInfo metaInfo);
}

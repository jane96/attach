/**
 * @(#)SystemService.java     1.0 15:31:50
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sys.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.util.Region;
import org.apache.poi.util.SystemOutLogger;
import org.apache.shiro.SecurityUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.Collections3;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.modules.sys.dao.CandidateDao;
import com.sccl.attech.modules.sys.dao.DictDao;
import com.sccl.attech.modules.sys.dao.MenuDao;
import com.sccl.attech.modules.sys.dao.OfficeDao;
import com.sccl.attech.modules.sys.dao.RoleDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.dao.UserOfficeDao;
import com.sccl.attech.modules.sys.dao.UserVoteDao;
import com.sccl.attech.modules.sys.dao.VoteDao;
import com.sccl.attech.modules.sys.entity.Candidate;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Menu;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.Role;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;
import com.sccl.attech.modules.sys.entity.UserVote;
import com.sccl.attech.modules.sys.entity.Vote;
import com.sccl.attech.modules.sys.security.SystemAuthorizingRealm;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author sccl
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService  {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final String HASH_MD5 = "MD5";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserOfficeDao userOfficeDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	@Autowired
	private DictDao dictDao;
	@Autowired
	private CandidateDao candidateDao;
//	@Autowired
//	private IdentityService identityService;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private VoteDao voteDao;
	//-- User Service --//
	@Autowired
	private UserVoteDao userVoteDao;
	public User getUser(String id) {
		return userDao.get(id);
	}
	/**
	 * 根据ids查询多个用户
	 * @param ids 
	 * @return
	 */
	public List<User> findShowAddress(String ids){
		
		ids = ids.substring(0, ids.lastIndexOf(","));
		String[] array = ids.split(",");
		DetachedCriteria dc = userDao.createDetachedCriteria();
		dc.add(Restrictions.in("id", array));
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		List<User> list = userDao.find(dc);
		
		return list;
	}
	/**
	 * 根据条件查询所有员工（用户拉框选人）
	 * @param user
	 * @return
	 */
	public List<User> findAllUserConditon(User user){
		
		DetachedCriteria dc = userDao.createDetachedCriteria();
		dc.add(Restrictions.eq("userType", "2"));
		User currentUser = UserUtils.getUser();
		dc.createAlias("office","office");
		dc.add(dataScopeFilter(currentUser, "office", "createBy"));
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		
		return userDao.find(dc);
	}
	
	/**
	 * 查询公司部门及一下部门的所有员工
	 * @param offices
	 * @param parentId
	 * @return
	 */
	public List<User> findUserByOffices(String parentId,User user){
	
		String sql ="select distinct u.* from sys_user u,sys_user_office uo where uo.user_id=u.id and u.del_flag ='"+User.DEL_FLAG_NORMAL+"' and uo.del_flag='"+UserOffice.DEL_FLAG_NORMAL+"' "+
					"and uo.office_id in (select o.id from sys_office o where o.parent_ids like '%"+parentId+"%' and o.del_flag ='"+Office.DEL_FLAG_NORMAL+"') ";
					
		if(user!=null && user.getName()!=null &&!user.getName().equals(""))
		{
			sql += "and u.name like '%"+user.getName().trim()+"%'";
		}
		
		List<User> userList = this.userDao.findBySql(sql, User.class);
		
		return userList;
	}
	
	
	
	/**
	 * 根据userId获取用户
	 * @param outId 省管项目经理id
	 * @return
	 */
	public User findByOutId(String outId){
		return userDao.findByOutId(outId);
	}
	
	/**
	 * 给对应公司保存统一考勤制度
	 * @param atId 用于判断添加考勤，还是修改考勤
	 * @param areaSetIds 考勤区域id
	 * @param attenTimeId 考勤时间id
	 */
	public void setAllUserByCondition(String atId,String areaSetIds,String attenTimeId){
		List<User> listByCompany = getListByCompany();
		if(StringUtils.isNotBlank(atId)){//修改
			for (User user : listByCompany) {
				String timeSetupId = user.getTimeSetupId();
				if(StringUtils.isNotBlank(attenTimeId)&&attenTimeId.equals(timeSetupId)){
					user.setAreaSetupId(areaSetIds);
					user.setTimeSetupId(attenTimeId);
					updateUser(user);
				}
			}
		}else{//新增
			for (User user : listByCompany) {
					user.setAreaSetupId(areaSetIds);
					user.setTimeSetupId(attenTimeId);
					updateUser(user);
			}
		}
	}	
	
	
	
	
	/**
	 * 根据条件查询所有员工（用户拉框选人）
	 * @param user
	 * @return
	 */
	public List<User> findAllUserConditonAll(User user){
		
		DetachedCriteria dc = userDao.createDetachedCriteria();
		//dc.add(Restrictions.eq("userType", "2"));
		User currentUser = UserUtils.getUser();
		dc.createAlias("office","office");
		dc.add(dataScopeFilter(currentUser, "office", "createBy"));
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		
		return userDao.find(dc);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
//		User currentUser = UserUtils.getUser();
//		DetachedCriteria dc = userDao.createDetachedCriteria();
//		
//		dc.createAlias("company", "company"); 
//		if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())){
//			dc.add(Restrictions.or(
//				Restrictions.eq("company.id", user.getCompany().getId()),
//				Restrictions.like("company.parentIds", "%," + user.getCompany().getId() + ",%")
//			));
//		}
		
		
//		if(user.getOffice() != null)
//		{
			Map<String,Object> map = new HashMap<String, Object>();
			
			if (StringUtils.isNotEmpty(user.getLoginName()))
			{
				String loginName=EncodedUtil.decodeValue(user.getLoginName());
				map.put("loginName",  "%" + loginName + "%");
			}
			
			if (StringUtils.isNotEmpty(user.getName()))
			{
				String userName=EncodedUtil.decodeValue(user.getName());
				map.put("name", "%" + userName + "%");
			}
			
			if(StringUtils.isNotEmpty(user.getOfficeName()))
			{
				String officeName=EncodedUtil.decodeValue(user.getOfficeName());
				map.put("officeName", "%" + officeName + "%");
			
			}
			if(StringUtils.isNotEmpty(user.getMobile()))
			{
				String mobile=EncodedUtil.decodeValue(user.getMobile());
				map.put("mobile", "%"+mobile+"%");
			}
			
			String officeId = null;
			
			User userSession = UserUtils.getUser();
			if(userSession == null)
			{
				return null;
			}
			else 
			{
				if(user.getOffice()!=null)
				{
					officeId = user.getOffice().getId();
				}
				else
				{
					if(userSession.getCompany()!=null)
					{
						officeId=userSession.getCompany().getId();
					}
				}
			}
			
			return this.officeDao.getPageUserList(page, officeId,map);
			
//		}
		
//		dc.createAlias("office", "office");
//		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())){
//			dc.add(Restrictions.or(
//				Restrictions.eq("office.id", user.getOffice().getId()),
//				Restrictions.like("office.parentIds", "%," + user.getOffice().getId() + ",%")
//			));
//		}
//		
//		// 如果不是超级管理员，则不显示超级管理员用户
////		if (!currentUser.isAdmin()){
////			dc.add(Restrictions.ne("id", "1"));
////			//只能查询自己创建的用户信息
////			dc.createAlias("createBy","createBy");
////			dc.add(Restrictions.eq("createBy.id",UserUtils.getUser().getId()));
////		}else{
//			dc.add(dataScopeFilter(currentUser, "office", "createBy"));
////		}
//		//dc.add(Restrictions.eq("company.id", UserUtils.getUser().getCompany().getId()));
//		//System.out.println(dataScopeFilter(currentUser, "office", ""));
//	    
//		if(StringUtils.isNotEmpty(user.getOfficeName()))
//		{
//			if(!"1".equals(currentUser.getId()))
//			{
//				dc.add(Restrictions.eq("company.id", currentUser.getCompany().getId()));
//			}
//			
//			dc.add(Restrictions.or(
//					Restrictions.like("office.name", "%" + EncodedUtil.decodeValue( user.getOfficeName()) + "%"),
//					Restrictions.like("company.name", "%" + EncodedUtil.decodeValue( user.getOfficeName()) + "%")
//			));
//		}
//			
//		if (StringUtils.isNotEmpty(user.getLoginName()))
//		{
//			dc.add(Restrictions.like("loginName", "%" + user.getLoginName() + "%"));
//		}
//		
//		if (StringUtils.isNotEmpty(user.getName()))
//		{
//			String userName=EncodedUtil.decodeValue( user.getName());
//			dc.add(Restrictions.like("name", "%" + userName + "%"));
//		}
//		
//		if(StringUtils.isNotEmpty(user.getUserType()))
//		{
//			dc.add(Restrictions.eq("userType",user.getUserType()));
//		
//		}
//		if(StringUtils.isNotEmpty(user.getMobile()))
//		{
//			dc.add(Restrictions.like("mobile","%"+user.getMobile()+"%"));
//		}
//		
//		dc.add(Restrictions.ne("id","1"));
//		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
//		
//		return userDao.find(page, dc);
	}
	/**
	 * 获取投票项
	 * @return
	 * /
	
	/**
	 * 获取公司下的所有用户
	 * @return
	 */
	public List<User> getListByCompany(){
		User user = UserUtils.getUser();
		DetachedCriteria dc = userDao.createDetachedCriteria();
			dc.createAlias("company", "company"); 
			if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())){
				dc.add(Restrictions.eq("company.id", user.getCompany().getId()));
			}
			dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		return userDao.find(dc);
	}
	
	
	/**
	 * 用于查找投票标题
	 * @param vote
	 * @return
	 */
	public Page<Vote> find(Page<Vote> page, Vote vote,int type) {
		//System.out.println(page.getPageNo() + "---------------- 页面count：");
		//System.out.println(page.getPageSize() + "----------------页面size：");
		DetachedCriteria dc = voteDao.createDetachedCriteria();
		
//		dc.createAlias("createBy.office", "office");
//		dc.add(dataScopeFilter(user, "office", "createBy"));
		if(type == 0){//载入所有投票项
			if (StringUtils.isNotEmpty(vote.getVotingContent())){
				dc.add(Restrictions.like("votingContent", "%"+vote.getVotingContent()+"%"));
			}
			dc.add(Restrictions.eq(Vote.FIELD_DEL_FLAG, Vote.DEL_FLAG_NORMAL));
			System.out.println("执行0方法");
		}
		else if(type == 1){//载入正在进行中的投票项
			dc.add(Restrictions.eq(Vote.SEL_ISEND,Vote.IS_END_NO));
			dc.add(Restrictions.ge(Vote.SEL_DEADLINE,Vote.SEl_DATE));
			System.out.println("执行1方法");
		}else if(type == 2){//载入已中止的投票项
			dc.add(Restrictions.eq(Vote.SEL_ISEND,Vote.IS_END_YES));
			System.out.println("执行2方法");
		}else if(type == 3){//载入已结束的投票项
			dc.add(Restrictions.lt(Vote.SEL_DEADLINE,Vote.SEl_DATE));
			System.out.println("执行3方法");
		}else if(type == 4){//载入当前用户未投过并且进行中的投票项
			if (StringUtils.isNotEmpty(vote.getVotingContent())){
				dc.add(Restrictions.like("votingContent", "%"+vote.getVotingContent()+"%"));
			}
			dc.add(Restrictions.eq(Vote.SEL_ISEND,Vote.IS_END_NO));
			dc.add(Restrictions.ge(Vote.SEL_DEADLINE,Vote.SEl_DATE));
			//找出当前用户已经投过的票
			User user = UserUtils.getUser();
			DetachedCriteria dc2 = userVoteDao.createDetachedCriteria();
			dc2.add(Restrictions.eq(UserVote.USER_VOTE_USER_ID, user.getId()));
			List<UserVote> list = userVoteDao.find(dc2);
			//增加条件
			for(UserVote ut:list){
				dc.add(Restrictions.ne(Vote.VOTE_ID, ut.getVoteId()));
			}
		}else if(type == 5){//找出当前用户已经投过的票
			if (StringUtils.isNotEmpty(vote.getVotingContent())){
				dc.add(Restrictions.like("votingContent", "%"+vote.getVotingContent()+"%"));
			}
			User user = UserUtils.getUser();
			DetachedCriteria dc2 = userVoteDao.createDetachedCriteria();
			dc2.add(Restrictions.eq(UserVote.USER_VOTE_USER_ID, user.getId()));
			List<UserVote> list = userVoteDao.find(dc2);
			List<String> list1 = new ArrayList();
			for(UserVote uVote:list){
				list1.add(uVote.getVoteId());
			}
			dc.add(Restrictions.in(Vote.VOTE_ID, list1));
		}
		dc.addOrder(Order.desc("createDate"));
		return voteDao.find(page, dc);
	}
	/**
	 * 用于查找候选项
	 * @param vote
	 * @return
	 */
	/*public Page<Candidate> find(Page<Candidate> page, Candidate candidate) {
		
		DetachedCriteria dc = candidateDao.createDetachedCriteria();
		
//		dc.createAlias("createBy.office", "office");
//		dc.add(dataScopeFilter(user, "office", "createBy"));
		dc.add(Restrictions.eq(Candidate.FIELD_DEL_FLAG, Candidate.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("id"));
		return candidateDao.find(page, dc);
	}*/
	public List<Candidate> find() {
		
		
		return candidateDao.find();
	}
	/**
	 * 用于导出数据的查询
	 * @param user
	 * @return
	 */
	public List<User> getList(User user){
		User currentUser = UserUtils.getUser();
		DetachedCriteria dc = userDao.createDetachedCriteria();
		
		dc.createAlias("company", "company"); 
		if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())){
			dc.add(Restrictions.or(
				Restrictions.eq("company.id", user.getCompany().getId()),
				Restrictions.like("company.parentIds", "%," + user.getCompany().getId() + ",%")
			));
		}
		
		dc.createAlias("office", "office");
		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())){
			dc.add(Restrictions.or(
				Restrictions.eq("office.id", user.getOffice().getId()),
				Restrictions.like("office.parentIds", "%," + user.getOffice().getId() + ",%")
			));
		}
		
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()){
			dc.add(Restrictions.ne("id", "1"));  
		}
		if(user.getUserType() != null && !"".equals(user.getUserType())){
			dc.add(Restrictions.eq("userType", user.getUserType()));
		}
		
		dc.add(dataScopeFilter(currentUser, "office", ""));
		
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%" + user.getLoginName() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			String userName=EncodedUtil.decodeValue( user.getName());
			dc.add(Restrictions.like("name", "%" + userName + "%"));
		}
		if(StringUtils.isNotEmpty(user.getUserType())){
			dc.add(Restrictions.eq("userType",user.getUserType()));
		}
		
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		
		return userDao.find(dc);
		
	}

	//取用户的数据范围
	public String getDataScope(User user){
		return dataScopeFilterString(user, "office", "");
	}
	
	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	public List<User> getUserByNames(String Names){
		
		return userDao.findByNames(Names);
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.clear();
		User currentUser = UserUtils.getUser();
		if(StringUtils.isBlank(user.getId())){
			if(currentUser.getAreaSetupId()!=null){
		    	user.setAreaSetupId(currentUser.getAreaSetupId());
		    }
			if(currentUser.getTimeSetupId()!=null){
				user.setTimeSetupId(currentUser.getTimeSetupId());
			}
		}
		userDao.save(user);
		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user,List<Office> officeList) throws Exception
	{
		userDao.clear();
		
		User currentUser = UserUtils.getUser();
		if(currentUser == null || currentUser.getCompany() == null)
		{
			throw new Exception("用户未登陆！");
		}
		
		if(StringUtils.isBlank(user.getId()))
		{
			if(currentUser.getAreaSetupId()!=null)
			{
		    	user.setAreaSetupId(currentUser.getAreaSetupId());
		    }
			
			if(currentUser.getTimeSetupId()!=null){
				user.setTimeSetupId(currentUser.getTimeSetupId());
			}
		}
		
		userDao.save(user);
		systemRealm.clearAllCachedAuthorizationInfo();
		
		String idStr = this.officeDao.getCompanyIds(currentUser.getCompany().getId());
		
		//删除用户下的所有角色
		String roleSql="delete sys_user_role t where t.user_id='"+user.getId()+"'";
		this.roleDao.updateBySql(roleSql);
		
		//删除用户下的所有部门
		String userSql="delete sys_user_office o where o.user_id='"+user.getId()+"' and o.company_id in ("+idStr+")";
		this.roleDao.updateBySql(userSql);
		
		for(Office office:officeList)
		{
			UserOffice userOffice=new UserOffice();
			userOffice.setUser(user);
			userOffice.setDept(office);
			userOffice.setCompany(office.getParent());
			userOffice.setRole(office.getDict());
			
			this.userOfficeDao.save(userOffice);
		}
	}

	@Transactional(readOnly = false)
	public void deleteUser(String id,String userOfficeId) {
		String sql = "select * from sys_user_office u where u.user_id ='"+id+"'";
		List<UserOffice> userOfficeList= this.userOfficeDao.findBySql(sql);
		if(userOfficeList.size() == 0)
		{
			this.userDao.deleteById(id);
		}
		else
		{
			this.userOfficeDao.deleteForId(userOfficeId);
		}
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(String id) {
		userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
//		byte[] salt = Digests.generateSalt(SALT_SIZE);
//		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
//		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
		return Md5Util.md5Encoder(plainPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
//		byte[] salt = Encodes.decodeHex(password.substring(0,16));
//		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
//		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
		return password.equals(entryptPassword(plainPassword));
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	/**
	 * 根据组织机构查询角色信息
	 * @param companyId
	 * @param offerId
	 * @return
	 */
	public  List<Role> findByOrangAllRole(String companyId,String offerId){
		List<Role> list = null;
		User user = UserUtils.getUser();
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.like("office.parentIds","%"+companyId+"%"));
		dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
		list = roleDao.find(dc);
		return list;
	}
	
	/**
	 * 角色分页查询
	 * @param page
	 * @param area
	 * @return
	 */
	public Page<Role> findRole(Page<Role> page, Role role) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(role.getName())){
			dc.add(Restrictions.like("name", "%" + role.getName() + "%"));
		}
		dc.createAlias("office", "office");
//		dc.createAlias("userList","userList");
//		dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//		dc.add(dataScopeFilter(user, "office", "userList"));
		if(role.getOffice()!=null){
			dc.add(Restrictions.eq("office.id",role.getOffice().getId()));
		}
		dc.add(dataScopeFilter(user, "office", "createBy"));
		//dc.createAlias("createBy","createBy");
		//dc.add(Restrictions.eq("createBy.id",UserUtils.getUser().getId()));
		dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));  
		return roleDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param role
	 * @return
	 */
	public List<Role> getRoleList(Role role){
        DetachedCriteria dc = roleDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(role.getName())){
			dc.add(Restrictions.like("name", "%" + role.getName() + "%"));
		}
		dc.createAlias("office", "office");
		if(role.getOffice()!=null){
			dc.add(Restrictions.eq("office.id",role.getOffice().getId()));
		}
		dc.createAlias("createBy","createBy");
		dc.add(Restrictions.eq("createBy.id",UserUtils.getUser().getId()));
		dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
		return roleDao.find(dc);

		}

	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		
	}

	@Transactional(readOnly = false)
	public void deleteRole(String id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, String userId) {
		User user = userDao.get(userId);
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);		
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	public List<Candidate> findAllCandidate(){
		return UserUtils.getCandidateList();
	}
	/**
	 * @return
	 * 获取自定格式的菜单列表
	 */
	public List<Map<String, Object>> findMenuByParentId(){
	    List<Menu> menuList = findAllMenu();//获取所有节点
	    if(CollectionUtils.isEmpty(menuList))
	    	return new ArrayList<Map<String,Object>>();
	    return Collections3.extractToListAsMap(menuList, new String[]{"id","parentId","name"});
	}
	
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		String oldParentIds = "";
		if(menu.getParent()!=null){
			menu.setParent(this.getMenu(menu.getParent().getId()));
			oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
			menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		}
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
		//saveActiviti(menu);
	}
	//------vote类--------//
	public Vote getVote(String id) {
		return voteDao.get(id);
	}
	//为什么voteDao.clear()不能用？
	@Transactional(readOnly = false)
	public void saveVote(Vote vote,List<String> list) {
		//voteDao.clear();
		voteDao.flush();
		voteDao.save(vote);
		//判断选项是否存在，如果存在则删除
		String id = vote.getId();
		String candidateDeleteSql="delete sys_vote_candidate t where t.vote_id='"+id+"'";
		candidateDao.updateBySql(candidateDeleteSql);
		//candidateDao.clear();
		//保存或者更新数据
		for(String cd:list){
			System.out.println("--------------------------一次--------------------");
			Candidate candidate = new Candidate();
			candidate.setCandidate(cd);
			candidate.setVote(vote);
			candidateDao.flush();
			candidateDao.saveOnly(candidate);
			
		}
		
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_VOTE);
		UserUtils.removeCache(UserUtils.CACHE_CANDIDATE_LIST);
		
		// 同步到Activiti
		//saveActiviti(menu);
	}
	@Transactional(readOnly = false)
	public void stopVote(String id) {
		String candidateDeleteSql="update sys_votes set is_End = 2 where id='"+id+"'";
		candidateDao.updateBySql(candidateDeleteSql);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_VOTE);
		// 同步到Activiti
		//saveActiviti(menu);
	}
	@Transactional(readOnly = false)
	public void deleteVote(String id) {
		String voteDeleteSql = "delete from sys_votes where id = '" + id + "'";
		String candidateDeleteSql="delete from sys_vote_candidate where vote_id='"+id+"'";
		
		
		voteDao.updateBySql(voteDeleteSql);
		candidateDao.updateBySql(candidateDeleteSql);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_VOTE);
		UserUtils.removeCache(UserUtils.CACHE_CANDIDATE_LIST);
		// 同步到Activiti
		//saveActiviti(menu);
	}
	@Transactional(readOnly = false)
	public void voteVote(String voteId,String candidateId) {
		String voteSql = "update sys_votes set num = num + 1 where id ='" + voteId + "'";
		voteDao.updateBySql(voteSql);
		String candidateSql = "update sys_vote_candidate set num = num + 1 where id = '" + candidateId +"'"; 
		candidateDao.updateBySql(candidateSql);
		UserVote userVote = new UserVote();
		userVote.setUserId(UserUtils.getUser().getId());
		userVote.setVoteId(voteId);
		userVoteDao.saveOnly(userVote);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_VOTE);
		UserUtils.removeCache(UserUtils.CACHE_CANDIDATE_LIST);
		// 同步到Activiti
		//saveActiviti(menu);
	}
	@Transactional(readOnly = false)
	public void voteVote2(String voteId,String[] cArray) {
		
		String voteSql = "update sys_votes set num = num + " + cArray.length + "where id ='" + voteId + "'";
		voteDao.updateBySql(voteSql);
		for(String s: cArray){
			String candidateSql = "update sys_vote_candidate set num = num + 1 where id = '" + s +"'"; 
			candidateDao.updateBySql(candidateSql);
		}
		UserVote userVote = new UserVote();
		userVote.setUserId(UserUtils.getUser().getId());
		userVote.setVoteId(voteId);
		userVoteDao.saveOnly(userVote);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_VOTE);
		UserUtils.removeCache(UserUtils.CACHE_CANDIDATE_LIST);
		// 同步到Activiti
		//saveActiviti(menu);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
		//deleteActiviti(id);
	}
	public Page<Menu> find(Page<Menu> page, Menu menu) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = menuDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(menu.getName())){
			dc.add(Restrictions.like("name", "%"+menu.getName()+"%"));
		}
//		dc.createAlias("createBy.office", "office");
//		dc.add(dataScopeFilter(user, "office", "createBy"));
		dc.add(Restrictions.eq(Menu.FIELD_DEL_FLAG, Menu.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		return menuDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param menu
	 * @return
	 */
	public List<Menu> getMenuList(Menu menu){
		Page<Menu> page=new Page<Menu>();
		page.setPageSize(-1);
		return find(page,menu).getList();
		
	}
	public List<Vote> getVoteList(Vote vote,int type,int pageSize,int pageCount){
		Page<Vote> page=new Page<Vote>();
		page.setPageSize(pageSize);
		page.setPageCount(pageCount);
		return find(page,vote,type).getList();
		
	}
	///////////////// Synchronized to the Activiti end //////////////////

	public AllTreeVo getMenuTree(AllTreeVo allTreeVo, Menu menu, String extId) {
		if(allTreeVo==null){
			List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
			allTreeVo = new AllTreeVo(menu,extId);
			for (Menu o : menu.getChildList()) {
				AllTreeVo t = new AllTreeVo(o,extId);
				treeVos.add(t);
			}
			allTreeVo.setChildren(treeVos);
			allTreeVo = getMenuTree(allTreeVo,null,extId);
		}else{
			for (AllTreeVo treeVo : allTreeVo.getChildren()) {
				List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
				Menu off = menuDao.get(treeVo.getId());
				for (Menu o : off.getChildList()) {
					AllTreeVo treeVo1 = new AllTreeVo(o,extId);
					treeVos.add(treeVo1);
				}
				treeVo.setChildren(treeVos);
				treeVo = getMenuTree(treeVo,null,extId);
			}
		}
		return allTreeVo;
	}

	@Transactional(readOnly = false)
	public void updateUser(User user) {
		userDao.merageUser(user);
	}
	/**
	 * 根据部门id获取非外勤人员
	 * @param page
	 * @param searchName
	 * @param officeId
	 * @return
	 */
	public Page<User> findByOffice(Page<User> page, String searchName,
			String officeId) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		
		dc.createAlias("office", "office");
		if (StringUtils.isNotBlank(officeId)){
			dc.add(Restrictions.eq("office.id",officeId));
		}
		
		if(StringUtils.isNotBlank(searchName)){
			dc.add(Restrictions.or(Restrictions.like("name","%"+searchName+"%" ),
					Restrictions.like("mobile", "%"+searchName+"%")));
		}
		dc.add(Restrictions.ne("userType","2"));//外勤人员
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		return userDao.find(page, dc);
	}
	
	/**
	 * 根据部门id获取外勤人员
	 * @param page
	 * @param searchName
	 * @param officeId
	 * @return
	 */
	public Page<User> findWorkOutByOffice(Page<User> page, String searchName,
			String officeId) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		
		dc.createAlias("office", "office");
		if (StringUtils.isNotBlank(officeId)){
			dc.add(Restrictions.eq("office.id",officeId));
		}
		
		if(StringUtils.isNotBlank(searchName)){
			dc.add(Restrictions.or(Restrictions.like("name","%"+searchName+"%" ),
					Restrictions.like("mobile", "%"+searchName+"%")));
		}
		
		dc.add(Restrictions.eq("userType","2"));//外勤人员
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		return userDao.find(page, dc);
	}
	/**
	 * 根据Integer类型表示的激活状态获取字符串激活状态，用于导出数据时显示
	 * @param locationOn
	 * @return
	 */
	public String getLocationOn(Integer locationOn){
		String locationValue="  ";
		if(locationOn!=null){
			if(locationOn==0){
				locationValue="激活定位";
			}
			else if(locationOn==1){
				locationValue="取消定位";
			}
		}		
		return locationValue;
	}
	
	/**
	 * 根据Integer类型表示的用户状态获取字符串类型用户状态，用于导出数据时显示
	 * @param state
	 * @return
	 */
	public String getState(Integer state){
		String stateValue="  ";
		if(state!=null){
			if(state==0){
				stateValue="正式用户";
			}
			else if(state==1){
				stateValue="试用用户";
			}
		}		
		return stateValue;
	}
	//用户类型 (0:企业管理员,1:部门管理员,2:外勤人员)
	public String getUserType(String userType){
		String typeValue="";
		if(userType!=null){
			if(userType.equals("0")){
				typeValue="企业管理员";
			}
			else if(userType.equals("1")){
				typeValue="部门管理员";
			}
			else if(userType.equals("2")){
				typeValue="外勤人员";
			}
		}		
		return typeValue;
	}
	/**
	 * 根据数字类型的数据范围获取中文类型的数据范围，用于导出数据时显示
	 * @param dataScope
	 * @return
	 */
	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public String getDataScopeValue(String dataScope){
		String dateValue="";
		if(dataScope!=null){
			if(dataScope.equals("1")){
				dateValue="所有数据";
			}
			else if(dataScope.equals("2")){
				dateValue="所在公司及以下数据";
			}
			else if(dataScope.equals("3")){
				dateValue="所在公司数据";
			}
			else if(dataScope.equals("4")){
				dateValue="所在部门及以下数据";
			}
			else if(dataScope.equals("5")){
				dateValue="所在部门数据";
			}
			else if(dataScope.equals("8")){
				dateValue="仅本人数据";
			}
			else if(dataScope.equals("9")){
				dateValue="按明细设置";
			}
		}		
		return dateValue;
	}
	
	//导出投票文件
	public String exportVote(Vote vote, int type,String filePath,HttpServletRequest request, HttpServletResponse response,int pageSize,int pageCount) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		System.out.println(request.getSession().getServletContext().getRealPath("/templates")+"--------------------");
		String n = filePath+".xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "投票名称");
			e.setTitleCell(2, "投票总数");	
			e.setTitleCell(3, "创建日期");	
			e.setTitleCell(4, "截至日期");	
			e.setTitleCell(5, "是否中止");	
			e.setTitleCell(6, "选择类型");	
				
			List<Vote> list=getVoteList(vote,type,pageSize,pageCount);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getVotingContent());
					e.setCell(2, list.get(i-1).getNum());
					e.setCell(3, list.get(i-1).getCreateDate());
					e.setCell(4, list.get(i-1).getDeadline());
					e.setCell(5, list.get(i-1).getIsEnd());
					e.setCell(6, list.get(i-1).getSelectType());
										
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}                                                                         
	//导出投票及选项文件
	public String exportCVote(Vote vote, int type,String filePath,HttpServletRequest request, HttpServletResponse response,int pageSize,int pageCount) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		System.out.println(request.getSession().getServletContext().getRealPath("/templates")+"--------------------");
		String n = filePath+".xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "投票名称");
			e.setTitleCell(2, "投票总数");	
			e.setTitleCell(3, "创建日期");	
			e.setTitleCell(4, "截至日期");	
			e.setTitleCell(5, "是否中止");	
			e.setTitleCell(6, "选择类型");	
			e.setTitleCell(7, "投票项");
			e.setTitleCell(8, "票数");
			List<Vote> list=getVoteList(vote,type,pageSize,pageCount);
			List<Candidate> list2 = findAllCandidate();
			int current = 1;
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					int count = 0;
					for(int j = 0; j < list2.size(); j++){
						try{
							if (list.get(i - 1).getId().equals(list2.get(j).getVote().getId())) {
								
								e.createRow(current + count);
								count++;
								e.setCell(0, i);
								e.setCell(1, list.get(i - 1).getVotingContent());
								e.setCell(2, list.get(i - 1).getNum());
								e.setCell(3, list.get(i - 1).getCreateDate());
								e.setCell(4, list.get(i - 1).getDeadline());
								if(list.get(i - 1).getIsEnd() == 1)
									e.setCell(5, "未中止");
								else
									e.setCell(5, "已中止");
								if(list.get(i - 1).getSelectType() == 1)
									e.setCell(6,"单选");
								else 
									e.setCell(6,"多选");
								e.setCell(7, list2.get(j).getCandidate());
								e.setCell(8, list2.get(j).getNum());
							}
							}catch(Exception e1){
								//System.out.println("出错");
							}
					}
					for (int k = 0; k <= 6; k++) {
						e.sheet.addMergedRegion(new Region(current, (short) k, current + count - 1, (short) k));
					}
					current += count;
					
				}
				
				
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		return n;
	}
	//导出菜单文件
		public String exportMenu(Menu menu, HttpServletRequest request, HttpServletResponse response) {
			System.out.println("进入导出页面---------------------");
			String path = request.getSession().getServletContext().getRealPath("/templates");
			System.out.println(request.getSession().getServletContext().getRealPath("/templates")+"--------------------");
			String n = DateUtils.getDate("yyyyMMddHHmmss")+"menu.xls";
			File f = new File(path+"/"+n);
			ExcelWriter e = new ExcelWriter();
			try {
				e = new ExcelWriter(new FileOutputStream(f));
				e.createRow(0);
				e.setTitleCell(0, "序号 ");
				e.setTitleCell(1, "菜单名称");
				e.setTitleCell(2, "菜单链接");	
				e.setTitleCell(3, "排序");	
				e.setTitleCell(4, "可见");	
				e.setTitleCell(5, "权限标示");	
				e.setTitleCell(6, "上级菜单");	
				e.setTitleCell(7, "目标");	
				List<Menu> list=getMenuList(menu);
				if(list!=null&&list.size()>0){
					for(int i=1;i<=list.size();i++){
						e.createRow(i);
						e.setCell(0, i);
						e.setCell(1, list.get(i-1).getName());
						if(list.get(i-1).getHref()==null){
							e.setCell(2, "  ");
						}else{
							e.setCell(2, list.get(i-1).getHref());
						}					
						e.setCell(3, list.get(i-1).getSort());
						e.setCell(4, list.get(i-1).getIsShowName());
						if(list.get(i-1).getPermission()==null){
							e.setCell(5, "  ");
						}else{
							e.setCell(5, list.get(i-1).getPermission());
						}					
						e.setCell(6, list.get(i-1).getParentName());
						if(list.get(i-1).getTarget()==null){
							e.setCell(7, "  ");
						}else{
							e.setCell(7, list.get(i-1).getTarget());
						}					
					}
				}
				try {
					e.export();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
			return n;
		}
	//导出角色文件
	public String exportRole(Role role, HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"role.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "角色名称");
			e.setTitleCell(2, "归属机构");	
			e.setTitleCell(3, "角色范围");	
			List<Role> list=getRoleList(role);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getName());
					e.setCell(2, list.get(i-1).getOfficeName());
					String dataScope=list.get(i-1).getDataScope();
					String dataValue=getDataScopeValue(dataScope);
					e.setCell(3, dataValue);
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}
	
	//导出用户文件
	public String exportUser(User user , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"user.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "姓名");
			e.setTitleCell(2, "归属公司");
			e.setTitleCell(3, "归属部门");	
			e.setTitleCell(4, "电话");	
			e.setTitleCell(5, "手机");	
			e.setTitleCell(6, "激活状态");	
			e.setTitleCell(7, "角色");
			e.setTitleCell(8, "工号");
			e.setTitleCell(9, "邮箱");
			e.setTitleCell(10, "用户类型");
			e.setTitleCell(11, "最后登录IP");
			e.setTitleCell(12, "最后登录日期");
			e.setTitleCell(13, "用户状态");
			e.setTitleCell(14, "到期日期");			
			List<User> list=getList(user);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getName());
					e.setCell(2, list.get(i-1).getCompanyName());
					e.setCell(3, list.get(i-1).getOfficeName());
					if(list.get(i-1).getPhone()==null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getPhone());
					}
					if(list.get(i-1).getMobile()==null){
						e.setCell(5, "   ");
					}else{
						e.setCell(5, list.get(i-1).getMobile());
					}										
					Integer locationOn=list.get(i-1).getLocationOn();
					String locationOnValue=getLocationOn(locationOn);
					e.setCell(6, locationOnValue);	
					if(list.get(i-1).getRoleNames() == null || list.get(i-1).getRoleNames()==""){
						e.setCell(7, "  ");
					}else{
						e.setCell(7, list.get(i-1).getRoleNames());
					}
					if(list.get(i-1).getNo() == null || list.get(i-1).getNo()==""){
						e.setCell(8, " ");
					}else{
						e.setCell(8, list.get(i-1).getNo());
					}
					
					if(list.get(i-1).getEmail()==null){
						e.setCell(9, "   ");
					}else{
						e.setCell(9, list.get(i-1).getEmail());
					}					
					String userType=list.get(i-1).getUserType();
					String typeValue=getUserType(userType);
					e.setCell(10, typeValue);
					if(list.get(i-1).getLoginIp()==null){
						e.setCell(11, "   ");
					}else{
						e.setCell(11, list.get(i-1).getLoginIp());
					}					
					if(list.get(i-1).getLoginDate()==null){
						e.setCell(12, "   ");
					}
					else{
						e.setCell(12, list.get(i-1).getLoginDate());
					}					
					Integer state=list.get(i-1).getState();
					String stateValue=getState(state);
					e.setCell(13, stateValue);
					if(list.get(i-1).getExpirationTime()==null){
						e.setCell(14, "   ");
					}
					else{
						e.setCell(14, list.get(i-1).getExpirationTime());
					}
					
					
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}
	/**
	 * 省管接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param officeId2
	 * @param companyId2
	 * @param loginName
	 * @param password
	 * @param no
	 * @param name
	 * @param id
	 * @param email
	 * @param phone
	 * @param mobile
	 * @param userType
	 * @return
	 */
	public String remoteUser(String OPFlag, String TimeStamp, String hashCode,
			String Summary, String officeId2, String companyId2,
			String loginName, String password, String no, String name,
			String id, String email, String phone, String mobile,
			String userType) {
		String md5 = Md5Util.md5Encoder(TimeStamp+"cj2015");
		if(md5.equalsIgnoreCase(hashCode)){
			if(OPFlag.equals("0100") || OPFlag.equals("0102")){ // 默认批量导入数据
				User user = new User();
				user.setOutId(id);
				user.setRemarks(Summary);
				if(StringUtils.isNotBlank(officeId2)){
					Office oo = officeDao.findByOutId("2", officeId2);
					user.setOffice(oo);
				}
				if(StringUtils.isNotBlank(companyId2)){
					Office oo = officeDao.findByOutId("1", companyId2);
					user.setCompany(oo);
				}
				user.setLoginName(loginName);
				user.setPassword(password);
				user.setNo(no);
				user.setName(name);
				user.setEmail(email);
				user.setPhone(phone);
				user.setMobile(mobile);
				user.setUserType(userType);
				user.setRemarks("2"); //标示是否外来数据
				List<Role> roleList = Lists.newArrayList();
				Role role = new Role();
				role.setId("3ff2da785b864f1180060e2ee594daf7");
				roleList.add(role);
				user.setRoleList(roleList);
				userDao.saveOnly(user);
			}else if(OPFlag.equals("0103")){ //修改
				User user = new User();
				user.setOutId(id);
				user.setRemarks(Summary);
				Office office = new Office();
				office.setId(officeId2);
				user.setOffice(office);
				Office company = new Office();
				company.setId(companyId2);
				user.setCompany(company);
				user.setLoginName(loginName);
				user.setPassword(password);
				user.setNo(no);
				user.setName(name);
				user.setEmail(email);
				user.setPhone(phone);
				user.setMobile(mobile);
				user.setUserType(userType);
				user.setRemarks("2"); //标示是否外来数据
				userDao.save(user);
			}else if(OPFlag.equals("0101")){ //删除
				userDao.deleteById(id);
			}
			return "00000";
		}else{
			
			return "00003"; //业务未授权
		}
	}
	
	public String save(User user)
	{
		return this.userDao.saveRet(user);
	}
	
	public String getAllOfficeUserJson()
	{
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		
		//部门组织架构
		String deptHql="from Office where parent.id=null and delFlag="+Office.DEL_FLAG_NORMAL;
		List<Office> officeList= this.officeDao.find(deptHql);
		if(officeList.size()==1)
		{
			
			
			//部门根节点
			Office parentOffice = officeList.get(0);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", parentOffice.getId());
			map.put("text", parentOffice.getName());
			map.put("name", parentOffice.getName());
			map.put("iconSkin", "department");
			map.put("type", "department");
			map.put("pId", "0_0");
			map.put("enable", 1);
			map.put("open", 1);
			
			mapList.add(map);
			
			
			//子节点部门集合
			String childrenOfficeHql="from Office where parent.parentIds like '%"+parentOffice.getId()+"%' and delFlag="+Office.DEL_FLAG_NORMAL;
			List<Office> childrenOfficeList=this.officeDao.find(childrenOfficeHql);
			for(Office office:childrenOfficeList)
			{
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("id", office.getId());
				map1.put("text", office.getName());
				map1.put("name", office.getName());
				map1.put("iconSkin", "department");
				map1.put("type", "department");
				map1.put("pId", parentOffice.getId());
				map1.put("enable", 1);
				map1.put("open", 1);
				
				mapList.add(map1);
			}
			
			//查询根节点下所有的用户
			String childrenUserHql="from UserOffice where dept.parentIds like '%"+parentOffice.getId()+"%'";
			List<UserOffice> userOfficeList = this.userOfficeDao.find(childrenUserHql);
			
			for(UserOffice userOffice:userOfficeList)
			{
				User user=userOffice.getUser();//用户
				Office office=userOffice.getDept();//部门
				Dict dict=userOffice.getRole();//职位
				
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("id", user.getId());
				map2.put("text", user.getName());
				map2.put("name", user.getName());
				map2.put("phone", user.getPhone());
				map2.put("iconSkin", "user");
				map2.put("type", "department");
				map2.put("enable", 1);
				map2.put("department", office.getId());
				map2.put("position", dict.getId());
				
				mapList.add(map2);
			}
		}
		
		//职位组织架构
		String dictHql="from Dict t where t.type='role_type' and t.parentId = null";
		List<Dict> dictList = this.dictDao.find(dictHql);
		if(dictList.size()==1)
		{
			
			//职位根节点
			Dict dict = dictList.get(0);
			
			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("id", dict.getId());
			map3.put("text", dict.getLabel());
			map3.put("name", dict.getLabel());
			map3.put("iconSkin", "position");
			map3.put("type", "position");
			map3.put("pId", "0_1");
			map3.put("enable", 1);
			map3.put("open", 1);
			
			mapList.add(map3);
			
			String dictChildrenHql="from Dict t where t.type='role_type' and t.parentId ='"+dict.getId()+"'";
			List<Dict> dictChildrenList = this.dictDao.find(dictChildrenHql);
			for(Dict dict2:dictChildrenList)
			{
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("id", dict2.getId());
				map4.put("text", dict2.getLabel());
				map4.put("name", dict2.getLabel());
				map4.put("iconSkin", "position");
				map4.put("type", "position");
				map4.put("pId", dict.getId());
				map4.put("enable", 1);
				map4.put("open", 0);
				
				mapList.add(map4);
			}
		}
		
		JSONArray jsonArray= JSONArray.fromObject(mapList);
		System.out.println(jsonArray.toString());
		return jsonArray.toString();
	}
}

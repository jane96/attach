/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.modules.message.entity.NoticeSample;
import com.sccl.attech.modules.message.dao.NoticeSampleDao;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 通知记录Service
 * @author sccl
 * @version 2015-05-13
 */
@Component
@Transactional(readOnly = true)
public class NoticeSampleService extends BaseService {

	@Autowired
	private NoticeSampleDao noticeSampleDao;
	
	public NoticeSample get(String id) {
		return noticeSampleDao.get(id);
	}
	
	public Page<NoticeSample> find(Page<NoticeSample> page, NoticeSample noticeSample) {
		User currentUser = UserUtils.getUser();
		DetachedCriteria dc = noticeSampleDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(noticeSample.getContent())){
			dc.add(Restrictions.like("content", "%"+noticeSample.getContent()+"%"));
		}
		//dc.add(dataScopeFilter(currentUser, "office", "createBy"));//用户根据权限查看对应的通知发送记录
		dc.createAlias("createBy","createBy");
		dc.add(Restrictions.eq("createBy.id",currentUser.getId()));//用户只能查看自己的通知发送记录
		dc.add(Restrictions.eq(NoticeSample.FIELD_DEL_FLAG, NoticeSample.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return noticeSampleDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param noticeSample
	 * @return
	 */
	public List<NoticeSample> getList(NoticeSample noticeSample){
		DetachedCriteria dc = noticeSampleDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(noticeSample.getContent())){
			dc.add(Restrictions.like("content", "%"+noticeSample.getContent()+"%"));
		}
		dc.add(Restrictions.eq(NoticeSample.FIELD_DEL_FLAG, NoticeSample.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return noticeSampleDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(NoticeSample noticeSample) {
		noticeSampleDao.save(noticeSample);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		noticeSampleDao.deleteById(id);
	}
	

	//导出文件
	public String exportFile(NoticeSample noticeSample , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"noticeSample.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "通知样例");
			e.setTitleCell(2, "创建时间");	
			List<NoticeSample> list=getList(noticeSample);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getContent());
					e.setCell(2, list.get(i-1).getCreateDate());
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


}

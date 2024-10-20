package com.hml.mall.service.reward;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.ShareHandle;
import com.hml.mall.entity.reward.ShareTotal;
import com.hml.mall.iface.reward.IShareTotalService;
import com.hml.mall.mapper.reward.ShareHandleMapper;
import com.hml.mall.mapper.reward.ShareTotalMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */

@Service("shareTotalService")
public class ShareTotalServiceImpl extends ServiceImpl<ShareTotalMapper, ShareTotal> implements IShareTotalService {

	@Autowired
	private ShareTotalMapper shareTotalMapper;
	
	@Autowired
	private ShareHandleMapper shareHandleMapper;

    @Override
    public List< ShareTotal> list(ShareTotal model) {
       QueryWrapper< ShareTotal> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		LoginUser user = SecurityUtils.getLoginInfo();
		if(user.getType() > 0) {
			pageRequest.getParams().put("USERNO",user.getUserno());
		}
		 PageResult pageResult = MybatisPlusPageHelper.findPage(pageRequest, shareTotalMapper,"findPage");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 ShareTotal sum = shareTotalMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void countShareTotal(String fdate) throws Exception {
    	QueryWrapper<ShareTotal> qw = new QueryWrapper<ShareTotal>();
    	qw.eq("PDATE", fdate);
    	qw.eq("STATUS",1);
    	List<ShareTotal> list = shareTotalMapper.selectList(qw);
    	if(list.size() > 0) {
    		throw new Exception(fdate + "已办理，请勿重复计算");
    	}
    	qw = new QueryWrapper<ShareTotal>();
    	qw.eq("PDATE", fdate);
    	shareTotalMapper.delete(qw);
    	shareTotalMapper.saveTotal(fdate);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTotal(List<ShareTotal> list) throws Exception {
    	if(list.size() == 0) {
    		throw new Exception("请选择办理数据");
    	}
    	
    	for(ShareTotal item:list) {
    		QueryWrapper<ShareTotal> qw = new QueryWrapper<ShareTotal>();
    		qw.eq("PDATE", item.getPdate());
    		qw.eq("USERNO", item.getUserno());
    		
    		ShareTotal total = shareTotalMapper.selectOne(qw);
    		if(total==null) {
    			throw new Exception(item.getPdate() +"-" + item.getUserno()+"数据不存在");
    		}
    		if(total.getStatus()==1) {
    			throw new Exception(item.getPdate() +"-" + item.getUserno()+"已办理");
    		}
//    		设置已办理
    		UpdateWrapper<ShareTotal> uw = new UpdateWrapper<ShareTotal>();
    		uw.eq("PDATE", item.getPdate());
    		uw.eq("USERNO", item.getUserno());
    		uw.set("STATUS", 1);
    		shareTotalMapper.update(null, uw);
//    		插入办理数据
    		ShareHandle handle = new ShareHandle();
    		handle.setPdate(total.getPdate());
    		handle.setUserno(total.getUserno());
    		handle.setConsumn(total.getConsumn());
    		handle.setBonus(total.getBonus());
    		handle.setPstatus("0");
    		shareHandleMapper.insert(handle);
    	}
    }

}

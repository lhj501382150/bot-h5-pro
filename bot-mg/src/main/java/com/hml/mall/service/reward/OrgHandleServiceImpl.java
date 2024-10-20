package com.hml.mall.service.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgHandle;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.reward.IOrgHandleService;
import com.hml.mall.mapper.reward.OrgHandleMapper;
import com.hml.mall.mapper.user.UserMapper;
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

@Service("orgHandleService")
public class OrgHandleServiceImpl extends ServiceImpl<OrgHandleMapper, OrgHandle> implements IOrgHandleService {

	@Autowired
	private OrgHandleMapper orgHandleMapper;
	
	@Autowired
	private UserMapper userMapper;

    @Override
    public List< OrgHandle> list(OrgHandle model) {
       QueryWrapper< OrgHandle> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user.getType() > 0) {
			pageRequest.getParams().put("USERNO",user.getUserno());
		}
		PageResult pageResult =  MybatisPlusPageHelper.findPage(pageRequest, orgHandleMapper,"findPage");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 OrgHandle sum = orgHandleMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHandle(OrgHandle model) throws Exception {
    	
    	OrgHandle handle = orgHandleMapper.selectById(model.getWaterno());
    	if("1".equals(handle.getPstatus())) {
    		throw new Exception("该数据已审核");
    	}
    	handle.setPstatus("1");
    	handle.setChkuno(SecurityUtils.getUsername());
    	handle.setChktime(DateTimeUtils.getDateTime());
    	handle.setChkip(model.getChkip());
    	
    	orgHandleMapper.updateById(handle);
    	
//    	通知账本
    	User user = userMapper.selectById(handle.getOrgno());
    	ZbUtils.inMoney(handle.getOrgno(), user.getUsername(), handle.getCommbonus());
    	
    }
    
}

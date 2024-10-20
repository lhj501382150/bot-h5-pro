package com.hml.mall.service.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.ShareHandle;
import com.hml.mall.iface.reward.IShareHandleService;
import com.hml.mall.mapper.reward.ShareHandleMapper;
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

@Service("shareHandleService")
public class ShareHandleServiceImpl extends ServiceImpl<ShareHandleMapper, ShareHandle> implements IShareHandleService {

	@Autowired
	private ShareHandleMapper shareHandleMapper;

    @Override
    public List< ShareHandle> list(ShareHandle model) {
       QueryWrapper< ShareHandle> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
	
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user.getType() > 0) {
			pageRequest.getParams().put("USERNO",user.getUserno());
		}
		PageResult pageResult =  MybatisPlusPageHelper.findPage(pageRequest, shareHandleMapper,"findPage");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 ShareHandle sum = shareHandleMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHandle(ShareHandle model) throws Exception {
    	QueryWrapper<ShareHandle> qw = new QueryWrapper<ShareHandle>();
    	qw.eq("PDATE", model.getPdate());
    	qw.eq("USERNO", model.getUserno());
    	
    	ShareHandle handle = shareHandleMapper.selectOne(qw);
    	if("1".equals(handle.getPstatus())) {
    		throw new Exception("该数据已审核");
    	}
    	handle.setPstatus("1");
    	handle.setChkuno(SecurityUtils.getUsername());
    	handle.setChktime(DateTimeUtils.getDateTime());
    	handle.setChkip(model.getChkip());
    	
    	UpdateWrapper<ShareHandle> uw = new UpdateWrapper<ShareHandle>();
    	uw.eq("PDATE", model.getPdate());
    	uw.eq("USERNO", model.getUserno());
    	
    	shareHandleMapper.update(handle, uw);
    	
//    	通知账本
    	String money = handle.getBonus().toString();
    	String contno = handle.getPdate().replaceAll("-", "") + handle.getUserno();
    	ZbUtils.changeMoney(handle.getUserno(), "100", "102", money, contno, "分享奖励办理");
    	
    	
    }
}

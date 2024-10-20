package com.hml.mall.service.money;

import java.math.BigDecimal;
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
import com.hml.mall.entity.money.UsermoneyChangeApply;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.money.IUsermoneyChangeApplyService;
import com.hml.mall.mapper.money.UsermoneyChangeApplyMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-07-11
 */

@Service("usermoneyChangeApplyService")
public class UsermoneyChangeApplyServiceImpl extends ServiceImpl<UsermoneyChangeApplyMapper, UsermoneyChangeApply> implements IUsermoneyChangeApplyService {

	@Autowired
	private UsermoneyChangeApplyMapper usermoneyChangeApplyMapper;

	@Autowired
	private UserMapper userMapper;
    @Override
    public List< UsermoneyChangeApply> list(UsermoneyChangeApply model) {
       QueryWrapper< UsermoneyChangeApply> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType() > 0) {
//			机构/客户 查询 配置当前登陆信息
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
		}
		return MybatisPlusPageHelper.findPage(pageRequest, usermoneyChangeApplyMapper,"findPageByUser");
		
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(UsermoneyChangeApply model) throws Exception {
    	UsermoneyChangeApply apply = usermoneyChangeApplyMapper.selectById(model.getWartno());
    	if(apply.getStatus() != 0) {
    		throw new Exception("该数据已审核！");
    	}
    	apply.setChkip(model.getChkip());
    	apply.setChkuser(model.getChkuser());
    	apply.setChktime(model.getChktime());
    	apply.setStatus(1);
    	usermoneyChangeApplyMapper.updateById(apply);
    	
    	String userno = apply.getUserno();
    	User user = userMapper.selectById(userno);
    	if(user == null){
    		throw new Exception("客户不存在，请先录入客户");
    	}
    	String actno = apply.getAcctno();
    	String subno = apply.getSubno();
    	BigDecimal money = apply.getChmoney();
    	if("100".equals(subno)){
    		ZbUtils.inMoney(userno, user.getUsername(), money);
    	}else if("101".equals(subno)){
    		ZbUtils.outMoney(userno, user.getUsername(), money);
    	}else{
    		throw new Exception("未知科目类型");
    	}
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndAudit(UsermoneyChangeApply model) throws Exception {
    	usermoneyChangeApplyMapper.insert(model);
    	audit(model);
    }
}

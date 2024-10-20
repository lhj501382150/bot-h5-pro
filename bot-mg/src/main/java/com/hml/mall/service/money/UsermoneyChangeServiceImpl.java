package com.hml.mall.service.money;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.UsermoneyChange;
import com.hml.mall.iface.money.IUsermoneyChangeService;
import com.hml.mall.mapper.money.UsermoneyChangeMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-14
 */

@Service("usermoneyChangeService")
public class UsermoneyChangeServiceImpl extends ServiceImpl<UsermoneyChangeMapper, UsermoneyChange> implements IUsermoneyChangeService {

	@Autowired
	private UsermoneyChangeMapper usermoneyChangeMapper;

    @Override
    public List< UsermoneyChange> list(UsermoneyChange model) {
       QueryWrapper< UsermoneyChange> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType() > 0) {
//			分销商/客户 查询 配置当前登陆信息
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
		}
		PageResult result = MybatisPlusPageHelper.findPage(pageRequest, usermoneyChangeMapper,"findPageByUser");
		if(result.getContent() != null && result.getContent().size() > 0) {
			QueryWrapper<?> qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			UsermoneyChange sum = usermoneyChangeMapper.findSum(qw);
			result.setSum(sum);
		}
		return result;
		
		
	}

}

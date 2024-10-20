package com.hml.mall.service.money;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Usermoney;
import com.hml.mall.iface.money.IUsermoneyService;
import com.hml.mall.mapper.money.UsermoneyMapper;
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

@Service("usermoneyService")
public class UsermoneyServiceImpl extends ServiceImpl<UsermoneyMapper, Usermoney> implements IUsermoneyService {

	@Autowired
	private UsermoneyMapper usermoneyMapper;

    @Override
    public List< Usermoney> list(Usermoney model) {
       QueryWrapper< Usermoney> wrapper = new QueryWrapper();
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
		PageResult result = MybatisPlusPageHelper.findPage(pageRequest, usermoneyMapper,"findPageByUser");
		if(result.getContent() !=null && result.getContent().size() > 0){
			QueryWrapper<?> qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			Usermoney sum = usermoneyMapper.findSum(qw);
			result.setSum(sum);
		}
		return result;
	}

    @Override
    public PageResult findMonthPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType() > 0) {
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
		}
		return MybatisPlusPageHelper.findPage(pageRequest, usermoneyMapper,"findMonthPage");
		
    }
    
    @Override
    public List<Map<String, Object>> findChnageCount(Map<String, Object> paraMap) throws Exception {
    	
    	return usermoneyMapper.findChnageCount(paraMap);
    }
}

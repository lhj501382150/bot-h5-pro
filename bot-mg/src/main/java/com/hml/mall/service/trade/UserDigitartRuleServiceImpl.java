package com.hml.mall.service.trade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.UserDigitartRule;
import com.hml.mall.iface.trade.IUserDigitartRuleService;
import com.hml.mall.mapper.trade.UserDigitartRuleMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-18
 */

@Service("userDigitartRuleService")
public class UserDigitartRuleServiceImpl extends ServiceImpl<UserDigitartRuleMapper, UserDigitartRule> implements IUserDigitartRuleService {

	@Autowired
	private UserDigitartRuleMapper userDigitartRuleMapper;

    @Override
    public List< UserDigitartRule> list(UserDigitartRule model) {
       QueryWrapper< UserDigitartRule> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userDigitartRuleMapper,"findPage");
	}

}

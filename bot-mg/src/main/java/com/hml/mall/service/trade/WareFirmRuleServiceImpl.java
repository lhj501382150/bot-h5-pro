package com.hml.mall.service.trade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareFirmRule;
import com.hml.mall.iface.trade.IWareFirmRuleService;
import com.hml.mall.mapper.trade.WareFirmRuleMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-19
 */

@Service("wareFirmRuleService")
public class WareFirmRuleServiceImpl extends ServiceImpl<WareFirmRuleMapper, WareFirmRule> implements IWareFirmRuleService {

	@Autowired
	private WareFirmRuleMapper wareFirmRuleMapper;

    @Override
    public List< WareFirmRule> list(WareFirmRule model) {
       QueryWrapper< WareFirmRule> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareFirmRuleMapper);
	}

}

package com.hml.mall.service.game;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.HorseRule;
import com.hml.mall.iface.game.IHorseRuleService;
import com.hml.mall.mapper.game.HorseRuleMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2023-01-06
 */

@Service("horseRuleService")
public class HorseRuleServiceImpl extends ServiceImpl<HorseRuleMapper, HorseRule> implements IHorseRuleService {

	@Autowired
	private HorseRuleMapper horseRuleMapper;

    @Override
    public List< HorseRule> list(HorseRule model) {
       QueryWrapper< HorseRule> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, horseRuleMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLike(Map<String, Object> item) throws Exception {
    	QueryWrapper<HorseRule> qw = new QueryWrapper<>();
    	qw.eq("ACTNO", item.get("actno"));
    	
    	List<HorseRule> list = horseRuleMapper.selectList(qw);
    	if(list.size() <= 0){
    		throw new Exception("选择活动未设置规则，请重新选择");
    	}
    	Long actno = Long.parseLong(item.get("newno").toString());
    	for(HorseRule rule : list){
    		HorseRule model = rule.clone(actno);
    		horseRuleMapper.insert(model);
    	}
    }
}

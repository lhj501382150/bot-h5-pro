package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.HorseDigitart;
import com.hml.mall.iface.game.IHorseDigitartService;
import com.hml.mall.mapper.game.HorseDigitartMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2023-01-06
 */

@Service("horseDigitartService")
public class HorseDigitartServiceImpl extends ServiceImpl<HorseDigitartMapper, HorseDigitart> implements IHorseDigitartService {

	@Autowired
	private HorseDigitartMapper horseDigitartMapper;

    @Override
    public List< HorseDigitart> list(HorseDigitart model) {
       QueryWrapper< HorseDigitart> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, horseDigitartMapper,"findPage");
	}

}

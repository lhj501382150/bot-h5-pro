package com.hml.mall.service.spread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.Active;
import com.hml.mall.iface.spread.IActiveService;
import com.hml.mall.mapper.spread.ActiveMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-31
 */

@Service("activeService")
public class ActiveServiceImpl extends ServiceImpl<ActiveMapper, Active> implements IActiveService {

	@Autowired
	private ActiveMapper activeMapper;

    @Override
    public List< Active> list(Active model) {
       QueryWrapper< Active> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, activeMapper,"findPage");
	}

}

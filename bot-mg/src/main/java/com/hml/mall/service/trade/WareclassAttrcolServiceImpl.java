package com.hml.mall.service.trade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareclassAttrcol;
import com.hml.mall.iface.trade.IWareclassAttrcolService;
import com.hml.mall.mapper.trade.WareclassAttrcolMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */

@Service("wareclassAttrcolService")
public class WareclassAttrcolServiceImpl extends ServiceImpl<WareclassAttrcolMapper, WareclassAttrcol> implements IWareclassAttrcolService {

	@Autowired
	private WareclassAttrcolMapper wareclassAttrcolMapper;

    @Override
    public List< WareclassAttrcol> list(WareclassAttrcol model) {
       QueryWrapper< WareclassAttrcol> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareclassAttrcolMapper);
	}

}

package com.hml.mall.service.mt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.mt.Guest;
import com.hml.mall.iface.mt.IGuestService;
import com.hml.mall.mapper.mt.GuestMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-05-14
 */

@Service("guestService")
public class GuestServiceImpl extends ServiceImpl<GuestMapper, Guest> implements IGuestService {

	@Autowired
	private GuestMapper guestMapper;

    @Override
    public List< Guest> list(Guest model) {
       QueryWrapper< Guest> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, guestMapper,"findPage");
	}

}

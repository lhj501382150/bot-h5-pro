package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Dict;
import com.hml.mall.iface.sys.IDictService;
import com.hml.mall.mapper.sys.DictMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-07-10
 */

@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

	@Autowired
	private DictMapper dictMapper;

    @Override
    public List< Dict> list(Dict model) {
       QueryWrapper< Dict> wrapper = new QueryWrapper();
       if(!StringUtils.isBlank(model.getDictno())) {
    	   wrapper.eq("DICTNO",model.getDictno());
       }
       wrapper.orderByAsc("DICTNO");
       wrapper.orderByAsc("SEQ");
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, dictMapper);
	}

}

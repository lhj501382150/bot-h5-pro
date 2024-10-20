package com.hml.mall.service.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.ShareParas;
import com.hml.mall.iface.reward.IShareParasService;
import com.hml.mall.mapper.reward.ShareParasMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */

@Service("shareParasService")
public class ShareParasServiceImpl extends ServiceImpl<ShareParasMapper, ShareParas> implements IShareParasService {

	@Autowired
	private ShareParasMapper shareParasMapper;

    @Override
    public List< ShareParas> list(ShareParas model) {
       QueryWrapper< ShareParas> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		 PageResult pageResult = MybatisPlusPageHelper.findPage(pageRequest, shareParasMapper);
		 //List<ShareParas> list = (List<ShareParas>)pageResult.getContent();
		 
		 return pageResult;
	}

}

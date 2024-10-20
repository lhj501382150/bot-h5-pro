package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.ImageList;
import com.hml.mall.iface.sys.IImageListService;
import com.hml.mall.mapper.sys.ImageListMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-01-21
 */

@Service("imageListService")
public class ImageListServiceImpl extends ServiceImpl<ImageListMapper, ImageList> implements IImageListService {

	@Autowired
	private ImageListMapper imageListMapper;

    @Override
    public List< ImageList> list(ImageList model) {
       QueryWrapper< ImageList> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, imageListMapper);
	}

}

package com.hml.mall.service.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.Forum;
import com.hml.mall.iface.notice.IForumService;
import com.hml.mall.mapper.notice.ForumMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */

@Service("forumService")
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements IForumService {

	@Autowired
	private ForumMapper forumMapper;

    @Override
    public List< Forum> list(Forum model) {
       QueryWrapper< Forum> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, forumMapper);
	}

}

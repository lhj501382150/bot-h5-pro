package com.hml.mall.service.mt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.mt.Meeting;
import com.hml.mall.iface.mt.IMeetingService;
import com.hml.mall.mapper.mt.MeetingMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-05-14
 */

@Service("meetingService")
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

	@Autowired
	private MeetingMapper meetingMapper;

    @Override
    public List< Meeting> list(Meeting model) {
       QueryWrapper< Meeting> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, meetingMapper,"findPage");
	}

}

package com.hml.mall.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserFeedback;
import com.hml.mall.iface.user.IUserFeedbackService;
import com.hml.mall.mapper.user.UserFeedbackMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-07-17
 */

@Service("userFeedbackService")
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements IUserFeedbackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;

    @Override
    public List< UserFeedback> list(UserFeedback model) {
       QueryWrapper< UserFeedback> wrapper = new QueryWrapper();
       if(!StringUtils.isBlank(model.getUserno())) {
    	   wrapper.eq("userno", model.getUserno());
       }
       return userFeedbackMapper.findList(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 pageRequest.getParams().put("SEQ", 1);
		return MybatisPlusPageHelper.findPage(pageRequest, userFeedbackMapper,"findPage");
	}

}

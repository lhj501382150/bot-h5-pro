package com.hml.mall.service.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.req.REQ518;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserLandinfo;
import com.hml.mall.iface.user.IUserLandinfoService;
import com.hml.mall.mapper.user.UserLandinfoMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-12-27
 */

@Service("userLandinfoService")
public class UserLandinfoServiceImpl extends ServiceImpl<UserLandinfoMapper, UserLandinfo> implements IUserLandinfoService {

	@Autowired
	private UserLandinfoMapper userLandinfoMapper;

    @Override
    public List< UserLandinfo> list(UserLandinfo model) {
       QueryWrapper< UserLandinfo> wrapper = new QueryWrapper();
       wrapper.in("ATYPE", Arrays.asList(new String[] {"1","2"}));
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userLandinfoMapper,"findPage");
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(REQ518 req) throws Exception {
    	
    	ZbUtils.post(BackCoreConfig.USER_LANDINFO, req);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void termination(REQ518 req) throws Exception {
    	
    	ZbUtils.post(BackCoreConfig.USER_LANDINFO, req);
    }

}

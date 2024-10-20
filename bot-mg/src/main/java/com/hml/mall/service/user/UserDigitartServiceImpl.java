package com.hml.mall.service.user;

import java.util.List;
import java.util.Map;

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
import com.hml.mall.entity.user.UserDigitart;
import com.hml.mall.iface.user.IUserDigitartService;
import com.hml.mall.mapper.user.UserDigitartMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-09-22
 */

@Service("userDigitartService")
public class UserDigitartServiceImpl extends ServiceImpl<UserDigitartMapper, UserDigitart> implements IUserDigitartService {

	@Autowired
	private UserDigitartMapper userDigitartMapper;

    @Override
    public List< UserDigitart> list(UserDigitart model) {
       QueryWrapper< UserDigitart> wrapper = new QueryWrapper();
       if(!StringUtils.isBlank(model.getUserno())) {
    	   wrapper.eq("userno", model.getUserno());
       }
       if(!StringUtils.isBlank(model.getWareno())) {
    	   wrapper.eq("wareno", model.getWareno());
       }
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userDigitartMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void post(REQ518 req) throws Exception {
    	
    	ZbUtils.post(BackCoreConfig.USER_DIGITART, req);
    }
     
    @Override
    public int getMaxNo(int length, String pre) {
    	int no = 1;
    	length = length + pre.length();
    	Map<String, Object> data = userDigitartMapper.getMaxNo(length, pre);
    	if(data != null && !StringUtils.isBlank(data.get("CARDNO"))) {
    		String cardNo = data.get("CARDNO").toString();
    		cardNo = cardNo.replaceAll(pre, "");
    		no = Integer.parseInt(cardNo) + 1;
    	}
    	return no;
    }
}

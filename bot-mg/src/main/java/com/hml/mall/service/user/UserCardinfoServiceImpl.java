package com.hml.mall.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.heepay.service.IHeePayService;
import com.hml.mall.entity.user.UserCardinfo;
import com.hml.mall.iface.user.IUserCardinfoService;
import com.hml.mall.mapper.user.UserCardinfoMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-11-19
 */

@Service("userCardinfoService")
public class UserCardinfoServiceImpl extends ServiceImpl<UserCardinfoMapper, UserCardinfo> implements IUserCardinfoService {

	@Autowired
	private UserCardinfoMapper userCardinfoMapper;

	@Autowired
	private IHeePayService heePayService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auth(UserCardinfo model) throws Exception {
		model = userCardinfoMapper.selectById(model.getId());
		if(model == null){
			throw new Exception("查询数据不存在！");
		}
		List<Map<String,String>> list = new ArrayList<>();
		Map<String,String> json = new LinkedHashMap<String, String>();
		json.put("bank_card_no", model.getCardno());
		json.put("bank_account", model.getSname());
		json.put("id_card", model.getIdno());
		
		list.add(json);
		String str = JSONArray.toJSON(list).toString();
		heePayService.auth(str);
		
		model.setIsauth("Y");
		userCardinfoMapper.updateById(model);
	}
	
    @Override
    public List< UserCardinfo> list(UserCardinfo model) {
       QueryWrapper< UserCardinfo> wrapper = new QueryWrapper();
       if(!StringUtils.isBlank(model.getUserno())) {
    	   wrapper.eq("USERNO", model.getUserno());
       }
       if(!StringUtils.isBlank(model.getSertype())) {
    	   wrapper.eq("SERTYPE", model.getSertype());
       }
       List<UserCardinfo> list = super.list(wrapper);
       for(UserCardinfo item : list) {
    	   if(!StringUtils.isBlank(item.getCardno())) {
    		   String cardNo = item.getCardno();
    		   if(item.getCardno().length() > 4) {
    			   cardNo = cardNo.substring(cardNo.length() - 4);
    		   }
    		   String alias = item.getBankname() + "(" + cardNo + ")" ;
    		   item.setAlias(alias);
    	   }
       }
       return list;
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		pageRequest.getParams().put("userno", user.getUserno());
    	}
		return MybatisPlusPageHelper.findPage(pageRequest, userCardinfoMapper,"findPage");
	}

}

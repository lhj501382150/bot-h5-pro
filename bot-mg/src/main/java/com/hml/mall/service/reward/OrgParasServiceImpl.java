package com.hml.mall.service.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgParas;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.reward.IOrgParasService;
import com.hml.mall.mapper.reward.OrgParasMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */

@Service("orgParasService")
public class OrgParasServiceImpl extends ServiceImpl<OrgParasMapper, OrgParas> implements IOrgParasService {

	@Autowired
	private OrgParasMapper orgParasMapper;
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRelationMapper userRelationMapper;

    @Override
    public List< OrgParas> list(OrgParas model) {
       QueryWrapper< OrgParas> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, orgParasMapper,"findPage");
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrgPara(OrgParas entity) throws Exception {
    	String orgNo = entity.getOrgno();
    	if(StringUtils.isBlank(orgNo)){
    		throw new Exception("机构编号不能为空");
    	}
    	QueryWrapper<OrgParas> qw = new QueryWrapper<>();
    	qw.eq("ORGNO", entity.getOrgno());
    	if("0".equals(orgNo)){//层级校验
    		qw.eq("CLEVEL", entity.getClevel());
    	}else{//机构校验
    		User user = userMapper.selectById(entity.getOrgno());
    		if(user==null){
    			throw new Exception("机构编号不存在，请重新输入");
    		}
    		if(user.getOrgtype()!=1){
    			throw new Exception("输入用户不是机构，请重新输入");
    		}
//    		设置机构级别
    		UserRelation ur = userRelationMapper.selectById(user.getUserno());
    		entity.setClevel(ur.getClevel());
    	}
    	OrgParas para = orgParasMapper.selectOne(qw);
    	if(para != null){
    		if("0".equals(orgNo)){
    			throw new Exception("该机构参数已设置，请重新输入");
        	}else{
        		throw new Exception("该层级参数已设置，请重新输入");
        	}
    	}
    	
    	super.save(entity);
    }

}

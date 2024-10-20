package com.hml.mall.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserDigitart;
import com.hml.mall.entity.user.UserDigitartApply;
import com.hml.mall.iface.user.IUserDigitartApplyService;
import com.hml.mall.mapper.user.UserDigitartApplyMapper;
import com.hml.mall.mapper.user.UserDigitartMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-05-25
 */

@Service("userDigitartApplyService")
public class UserDigitartApplyServiceImpl extends ServiceImpl<UserDigitartApplyMapper, UserDigitartApply> implements IUserDigitartApplyService {

	@Autowired
	private UserDigitartApplyMapper userDigitartApplyMapper;

	@Autowired
	private UserDigitartMapper userDigitartMapper;
	
    @Override
    public List< UserDigitartApply> list(UserDigitartApply model) {
       QueryWrapper< UserDigitartApply> wrapper = new QueryWrapper();
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
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userDigitartApplyMapper,"findPage");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void audit(UserDigitartApply model) throws Exception {
		
		UserDigitartApply item = userDigitartApplyMapper.selectById(model.getArtid());
		if(!"1".equals(item.getReqstatus())){
			throw new Exception("该数据已审核或已驳回!");
		}
		if(StringUtils.isBlank(item.getWareno())){
			throw new Exception("请设置商品编号");
		}
		if(StringUtils.isBlank(item.getArttype())){
			throw new Exception("请设置资产类型");
		}
//		插入数字资产表
		UserDigitart digitart = new UserDigitart();
		digitart.setUserno(item.getUserno());
		digitart.setWareno(item.getWareno());
		digitart.setCardname(item.getCardname());
		digitart.setCardgrade(item.getCardgrade());
		digitart.setCardno(item.getCardno());
		digitart.setCardsex(item.getCardsex());
		digitart.setCardrace(item.getCardrace());
		digitart.setSpath(item.getSpath());
		digitart.setNum(item.getNum());
		digitart.setArttype(item.getArttype());
		digitart.setMem(item.getMem());
		userDigitartMapper.insert(digitart);
		
		item.setReqstatus("2");
		item.setOrgartid(digitart.getArtid());
		userDigitartApplyMapper.updateById(item);
		
		
		
		
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void refuse(UserDigitartApply model) throws Exception {
		UserDigitartApply item = userDigitartApplyMapper.selectById(model.getArtid());
		if(!"1".equals(item.getReqstatus())){
			throw new Exception("该数据已审核或已驳回!");
		}
		
		item.setReqstatus("3");
		userDigitartApplyMapper.updateById(item);
		
	}

}

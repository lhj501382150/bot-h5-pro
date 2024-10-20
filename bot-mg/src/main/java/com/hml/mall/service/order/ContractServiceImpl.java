package com.hml.mall.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.Contract;
import com.hml.mall.entity.order.Invoic;
import com.hml.mall.entity.order.Sendinfo;
import com.hml.mall.entity.reward.OrgHandle;
import com.hml.mall.iface.order.IContractService;
import com.hml.mall.mapper.order.ContractMapper;
import com.hml.mall.mapper.order.InvoicMapper;
import com.hml.mall.mapper.order.SendinfoMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-18
 */

@Service("contractService")
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

	@Autowired
	private ContractMapper contractMapper;
	
	@Autowired
	private SendinfoMapper sendinfoMapper;
	
	@Autowired
	private InvoicMapper invoicMapper;

    @Override
    public List< Contract> list(Contract model) {
       QueryWrapper< Contract> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType()==0) {
//			return MybatisPlusPageHelper.findPage(pageRequest, contractMapper,"findPage");
		}else {
//			当前登陆用户
			int clevel = user.getClevel();
			Object uno1 = pageRequest.getParam("uno1");
			Object uno2 = pageRequest.getParam("uno2");
			pageRequest.getParams().remove("uno1");
			pageRequest.getParams().remove("uno2");
			if(!StringUtils.isBlank(uno1)) {
				int c1 = clevel + 1;
				pageRequest.getParams().put("uno"+c1, uno1);
			}
			if(!StringUtils.isBlank(uno2)) {
				int c1 = clevel + 2;
				pageRequest.getParams().put("uno"+c1, uno2);
			}
//			机构/客户 查询 配置当前登陆信息
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
		}
		PageResult pageResult =  MybatisPlusPageHelper.findPage(pageRequest, contractMapper,"findPageByUser");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 Contract sum = contractMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
		
	}

	@Override
	public Sendinfo findSendInfo(Contract contract) {
		Sendinfo sendinfo = sendinfoMapper.selectById(contract.getContno());
		return sendinfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdateSend(Sendinfo model) {
		Sendinfo send = sendinfoMapper.selectById(model.getContno());
		if(send == null) {
			sendinfoMapper.insert(model);
		}else {
			sendinfoMapper.updateById(model);
		}
		return true;
	}

	@Override
	public Invoic findInvoice(Contract contract) {
		Invoic invoic = invoicMapper.selectById(contract.getContno());
		return invoic;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdateInvoic(Invoic model) {
		Invoic invoic = invoicMapper.selectById(model.getContno());
		if(invoic == null) {
			invoicMapper.insert(model);
		}else {
			invoicMapper.updateById(model);
		}
		return true;
	}
    
    

}

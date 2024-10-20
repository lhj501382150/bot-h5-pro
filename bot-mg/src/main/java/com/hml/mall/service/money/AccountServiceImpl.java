package com.hml.mall.service.money;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Account;
import com.hml.mall.iface.money.IAccountService;
import com.hml.mall.mapper.money.AccountMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-14
 */

@Service("accountService")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

	@Autowired
	private AccountMapper accountMapper;

    @Override
    public List< Account> list(Account model) {
       QueryWrapper< Account> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, accountMapper);
	}

}

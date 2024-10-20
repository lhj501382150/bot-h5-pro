package com.hml.mall.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Usermoney;
import com.hml.mall.entity.money.UsermoneyChange;
import com.hml.mall.entity.order.Order;
import com.hml.mall.iface.order.IOrderService;
import com.hml.mall.mapper.money.UsermoneyChangeMapper;
import com.hml.mall.mapper.money.UsermoneyMapper;
import com.hml.mall.mapper.order.OrderMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.HttpClientUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-18
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private UsermoneyMapper usermoneyMapper;
	
	@Autowired
	private UsermoneyChangeMapper usermoneyChangeMapper;

    @Override
    public List<Order> list(Order model) {
       QueryWrapper<Order> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType()<=0) {
			return MybatisPlusPageHelper.findPage(pageRequest, orderMapper,"findPage");
		}else {
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
			return MybatisPlusPageHelper.findPage(pageRequest, orderMapper,"findPageByUser");
		}
	}
    @Override
    public PageResult findCount(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
    	Map<String, Object> params = pageRequest.getParams();
    	int pageNum = pageRequest.getPageNum();
     	int size = pageRequest.getPageSize();
     	int pageIdx = (pageNum - 1) * size;
     	params.put("pageIndex", pageIdx);
     	params.put("pageSize", size);
     	if(user.getType() > 0) {
     		params.put(user.getQueryNo(), SecurityUtils.getUsername());
     	}
     	Map<String, Object> count = orderMapper.findCountSum(params);
     	List<Map<String, Object>> datas = orderMapper.findCount(params);
     	
     	PageResult result = new PageResult();
     	result.setContent(datas);
     	int total = Integer.parseInt(count.get("TOTAL").toString());
     	int totalPage = total/size +1;
     	result.setTotalPages(totalPage);
     	result.setPageNum(pageNum);
     	result.setPageSize(size);
     	result.setTotalSize(total);
     	result.setSum(count);
     	return result;
    }
    
    @Override
    public PageResult findFYCount(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
    	Map<String, Object> params = pageRequest.getParams();
    	int pageNum = pageRequest.getPageNum();
     	int size = pageRequest.getPageSize();
     	int pageIdx = (pageNum - 1) * size;
     	params.put("pageIndex", pageIdx);
     	params.put("pageSize", size);
     	if(user.getType() > 0) {
     		params.put(user.getQueryNo(), SecurityUtils.getUsername());
     	}
     	Map<String, Object> count = orderMapper.findFYCountSum(params);
     	List<Map<String, Object>> datas = orderMapper.findFYCount(params);
     	
     	PageResult result = new PageResult();
     	result.setContent(datas);
     	int total = Integer.parseInt(count.get("TOTAL").toString());
     	int totalPage = total/size +1;
     	result.setTotalPages(totalPage);
     	result.setPageNum(pageNum);
     	result.setPageSize(size);
     	result.setTotalSize(total);
     	result.setSum(count);
     	return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void syncHisData()throws Exception {
		String fdate = DateTimeUtils.getCurrentDate("yyyy-MM-dd");
		String date = DateTimeUtils.addDate(fdate, "yyyy-MM-dd", -14);
		String ordtime = date + " 06:50:00";
		log.info("同步14天数据：{} ~ {}",fdate,ordtime);
		orderMapper.syncHisOrder(ordtime);
		
		QueryWrapper<Order> qw = new QueryWrapper<Order>();
		qw.le("ORDTIME", ordtime);
		orderMapper.delete(qw);
		log.info("订单数据完成,开始备份资金:{}",date);
		usermoneyMapper.syncHisData(date);
		QueryWrapper<Usermoney> qw1 = new QueryWrapper<Usermoney>();
		qw1.le("FDATE", date);
		usermoneyMapper.delete(qw1);
		log.info("资金数据完成,开始备份变动明细:{}",date);
		usermoneyChangeMapper.syncHisData(date);
		QueryWrapper<UsermoneyChange> qw2 = new QueryWrapper<UsermoneyChange>();
		qw2.le("FDATE", date);
		usermoneyChangeMapper.delete(qw2);
		log.info("变动明细处理完成...");
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateById(Order entity) {
		Order item = orderMapper.selectById(entity.getOrderno());
		if("1".equals(entity.getStatus())) {
			item.setCantime(DateTimeUtils.getDateTime());
		}else {
			item.setCantime("");
		}
		item.setStatus(entity.getStatus());
		 boolean flag = super.updateById(item);
		 JSONObject json = new JSONObject();
		 json.put("orderNo", item.getOrderno());
		 json.put("mode", item.getMode());
		 json.put("status", item.getStatus());
		 
		 try {
				String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.EDIT_ORDER_STATUS, json.toJSONString(),null);
				json = JSONObject.parseObject(ret);
				if(!"0".equals(json.getString("iCode"))){
					throw new RuntimeException(json.getString("sMsg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		 return flag;
	}
}

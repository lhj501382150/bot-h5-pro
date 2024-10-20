package com.hml.mall.iface.trade;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareApply;
import com.hml.mall.entity.trade.WareAttrApply;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-05-18
*/


public interface IWareApplyService extends IService<WareApply> {

	/**
	   * 查询
	   *
	   * @param model
	   * @return
	   */
	   List<WareApply> list(WareApply model);

	    /**
	    * 分页查询
	    *
	    * @param model
	    * @param pageNum  第几页
	    * @param pageSize 每页数量
	    * @return
	    */
	   PageResult findPage(PageRequest pageRequest);
	   
	   void saveWare(WareApply ware)throws Exception;
	   
	   void updateWare(WareApply ware) throws Exception;
	   
	   void delWare(WareApply ware) throws Exception;
	   
	   List<WareAttrApply> findWareAttr(WareApply ware)throws Exception;
	   
	   void submitWareApply(WareApply ware)throws Exception;
	   
	   void auditWareApply(WareApply ware) throws Exception;

}

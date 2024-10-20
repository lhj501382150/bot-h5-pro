package com.hml.mall.mapper.trade;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hml.mall.entity.trade.WareApply;
import com.hml.mall.entity.trade.WareAttrApply;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-18
 */
public interface WareAttrApplyMapper extends BaseMapper<WareAttrApply> {
	List<WareAttrApply> selectWareAttr(WareApply ware);
	
	void saveWareAttr(WareApply ware)throws Exception;
	
	void updateWareAttr(WareApply ware)throws Exception;
}

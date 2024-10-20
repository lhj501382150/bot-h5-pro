package com.hml.mall.mapper.trade;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareAttr;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */
public interface WareAttrMapper extends BaseMapper<WareAttr> {
	List<WareAttr> selectWareAttr(Ware ware);
}

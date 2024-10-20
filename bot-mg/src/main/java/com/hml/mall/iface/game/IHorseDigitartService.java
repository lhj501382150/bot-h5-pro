package com.hml.mall.iface.game;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.HorseDigitart;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2023-01-06
*/


public interface IHorseDigitartService extends IService<HorseDigitart> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<HorseDigitart> list(HorseDigitart model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

}

package com.hml.mall.controller.money;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Usermoney;
import com.hml.mall.iface.money.IUsermoneyService;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-14
*/


@RestController
@RequestMapping("/usermoney" )
public class UsermoneyController {

    @Autowired
    private IUsermoneyService  usermoneyService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoney:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Usermoney model) {
        usermoneyService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoney:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Usermoney model) {
        usermoneyService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoney:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Usermoney model) {
        usermoneyService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @RequestMapping("/getById")
    public HttpResult getById(@RequestBody Usermoney model) {
    	QueryWrapper<Usermoney> qw = new QueryWrapper<Usermoney>();
    	qw.eq("USERNO", model.getUserno());
    	qw.eq("ACCTNO", model.getAcctno());
    	qw.eq("FDATE", DateTimeUtils.getCurrentDate("yyyy-MM-dd"));
    	Usermoney item = usermoneyService.getOne(qw);
        // todo 再包装一层
        return HttpResult.ok(item);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoney:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Usermoney model = new  Usermoney();
        List<Usermoney> list =  usermoneyService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoney:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = usermoneyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    /**
     * 列表查询（分页）
     *
     * @return
     */
     @PreAuthorize("hasAuthority('money:moneymonth:view')")
     @RequestMapping("/findMonthPage")
     public HttpResult findMonthPage(@RequestBody PageRequest pageRequest) {

         PageResult page = usermoneyService.findMonthPage(pageRequest);
         // todo 再包装一层
         return HttpResult.ok(page);
     }

     /**
      * 列表查询（非分页）
      *
      * @return
     * @throws Exception 
      */
      @PreAuthorize("hasAuthority('money:moneymonth:view')")
      @RequestMapping("/findChange")
      public HttpResult findChange(@RequestBody Map<String,Object> paraMap) throws Exception {
    	  String fdate = paraMap.get("fdate").toString();
      	  String bdate = fdate + "-01 00:00:00";
      	  String edate = "";
      	  if(fdate.equals(DateTimeUtils.getCurrentDate("yyyy-MM"))) {
      		  edate = DateTimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
      	  }else {
      		edate = DateTimeUtils.lastDayOfMonth(fdate, "yyyy-MM") + " 23:59:59";
      	  }
      	  paraMap.put("bdate",bdate);
      	  paraMap.put("edate",edate);
          List<Map<String,Object>> list =  usermoneyService.findChnageCount(paraMap);
          Map<String,Object> map = new HashedMap();
          map.put("bdate",bdate);
          map.put("edate",edate);
          map.put("data",list);
          // todo 再包装一层
          return HttpResult.ok(map);
      }
}

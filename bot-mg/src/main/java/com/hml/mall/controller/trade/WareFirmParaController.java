package com.hml.mall.controller.trade;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareFirmPara;
import com.hml.mall.entity.user.UserGivbox;
import com.hml.mall.iface.trade.IWareFirmParaService;
import com.hml.mall.iface.user.IUserGivboxService;
import com.hml.utils.PoiUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2022-07-18
*/


@RestController
@RequestMapping("/wareFirmPara" )
public class WareFirmParaController {

    @Autowired
    private IWareFirmParaService  wareFirmParaService;
    @Autowired
    private IUserGivboxService userGivboxService;


    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareFirmPara model) throws Exception {
        wareFirmParaService.savePara(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareFirmPara model) throws Exception {
        wareFirmParaService.updatePara(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareFirmPara model) throws Exception {
        wareFirmParaService.deletePara(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     WareFirmPara model = wareFirmParaService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }
    /**
     * 列表查询（非分页）
     *
     * @return
     * @throws Exception 
     */
     @RequestMapping("/para")
     public HttpResult para(@RequestBody WareFirmPara ware) throws Exception {
   	  List<Basepara> list = wareFirmParaService.findWareParas(ware);
         return HttpResult.ok(list);
     }

    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareFirmPara model = new  WareFirmPara();
        List<WareFirmPara> list =  wareFirmParaService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmPara:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareFirmParaService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    @PreAuthorize("hasAuthority('trade:wareFirmPara:import')")
    @RequestMapping("/importData")
    public HttpResult importData(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
	   	 String[][] str = PoiUtils.imporData(file);
	   	 List<UserGivbox> data = getData(str);
	   	  
	   	 userGivboxService.saveBatch(data);
        return HttpResult.ok();
    }
    
    private List<UserGivbox> getData(String[][] str)throws Exception{
    	List<UserGivbox> data = new ArrayList<UserGivbox>();
    	for(String[] para:str) {
    		if(para.length != 2) continue;
    		if(StringUtils.isBlank(para[0])
    			|| 	StringUtils.isBlank(para[1]) ) {
    			continue;
    		}
    		UserGivbox item = new UserGivbox();
    		 item.setUserno(para[0]);
    		 item.setNum(Integer.parseInt(para[1]));
    		data.add(item);
    	}
    	
    	return data;
    }
}

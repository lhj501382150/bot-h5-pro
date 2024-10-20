package com.hml.mall.controller.game;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
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
import com.hml.mall.entity.game.HorseDigitart;
import com.hml.mall.entity.sys.Dict;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.game.IHorseDigitartService;
import com.hml.mall.iface.sys.IDictService;
import com.hml.mall.iface.user.IUserService;
import com.hml.utils.PoiUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2023-01-06
*/


@RestController
@RequestMapping("/horseDigitart" )
public class HorseDigitartController {

    @Autowired
    private IHorseDigitartService  horseDigitartService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IDictService dictService;
    

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody HorseDigitart model) {
        horseDigitartService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody HorseDigitart model) {
        horseDigitartService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody HorseDigitart model) {
        horseDigitartService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     HorseDigitart model = horseDigitartService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        HorseDigitart model = new  HorseDigitart();
        List<HorseDigitart> list =  horseDigitartService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseDigitart:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = horseDigitartService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @PreAuthorize("hasAuthority('gm:horseDigitart:importData')")
    @RequestMapping("/importData")
    public HttpResult importData(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
	   	 String[][] str = PoiUtils.imporData(file);
	   	 List<HorseDigitart> data = getData(str);
	   	  
	   	 horseDigitartService.saveBatch(data);
        return HttpResult.ok();
    }
    
    private List<HorseDigitart> getData(String[][] str)throws Exception{
    	List<HorseDigitart> data = new ArrayList<HorseDigitart>();
//    	查询字典
    	QueryWrapper<Dict> qw =  new QueryWrapper<Dict>();
    	qw.eq("TABNAME", "TB_GM_HORSE_DIGITART");
    	List<Dict> dicts = dictService.list(qw);
    	Map<String,List<String>> dictMap = new HashedMap();
    	for(Dict dict:dicts) {
    		List<String> child = null;
    		if(dictMap.containsKey(dict.getFieldname())) {
    			child = dictMap.get(dict.getFieldname());
    		}else {
    			child = new ArrayList<String>();
    		}
    		child.add(dict.getSval());
    		dictMap.put(dict.getFieldname(),child);
    	}
    	for(String[] para:str) {
    		if(para.length != 9) continue;
    		if(StringUtils.isBlank(para[0])
    			|| 	StringUtils.isBlank(para[1]) ) {
    			continue;
    		}
    		HorseDigitart item = new HorseDigitart();
    		item.setHorseno(para[0]);
    		User user = userService.getById(para[1]);
    		if(user == null) {
    			throw new Exception("账号不存在！");
    		}
    		item.setUserno(para[1]);
//    		类型  血统  马种  体质  性别  颜色  配饰
    		checkAttr(para[2], "类型", dictMap.get("HTYPE"));
    		checkAttr(para[3], "血统", dictMap.get("ANCESTRY"));
    		checkAttr(para[4], "马种", dictMap.get("HBREED"));
    		checkAttr(para[5], "体质", dictMap.get("CONS"));
    		checkAttr(para[6], "性别", dictMap.get("SEX"));
    		checkAttr(para[7], "颜色", dictMap.get("COLOR"));
    		checkAttr(para[8], "配饰", dictMap.get("ORNAM"));
    		
    		item.setHtype(para[2]);
    		item.setAncestry(para[3]);
    		item.setHbreed(para[4]);
    		item.setCons(para[5]);
    		item.setSex(para[6]);
    		item.setColor(para[7]);
    		item.setOrnam(para[8]);
    		data.add(item);
    	}
    	
    	return data;
    }

    private void checkAttr(String val,String info,List<String> dicts)throws Exception{
    	if(StringUtils.isBlank(val)) {
    		throw new Exception(info + "属性值不能为空！");
    	}
    	if(!dicts.contains(val)) {
    		throw new Exception(info + "属性值不存在，请核查以后再操作！");
    	}
    }
    
}

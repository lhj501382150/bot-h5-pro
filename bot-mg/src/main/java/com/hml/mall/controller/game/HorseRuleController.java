package com.hml.mall.controller.game;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.HorseRule;
import com.hml.mall.entity.sys.Dict;
import com.hml.mall.iface.game.IHorseRuleService;
import com.hml.mall.iface.sys.IDictService;


/**
*  前端控制器
* @author hml
* @since 2023-01-06
*/


@RestController
@RequestMapping("/horseRule" )
public class HorseRuleController {

    @Autowired
    private IHorseRuleService  horseRuleService;

    @Autowired
    private IDictService dictService;
    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody HorseRule model) {
    	QueryWrapper<HorseRule> qw = new QueryWrapper<>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("RULENO", model.getRuleno());
    	int count = horseRuleService.count(qw);
    	if(count > 0){
    		return HttpResult.error("该活动规则已设置，请重新选择！");
    	}
        horseRuleService.save(model);
        return HttpResult.ok();
    }
    
    /**
     * 保存
     *
     * @param model
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('gm:horseRule:addlike')")
     @RequestMapping("/saveLike")
     public HttpResult saveLike(@RequestBody Map<String,Object> item) throws Exception {
         horseRuleService.saveLike(item);
         return HttpResult.ok();
     }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody HorseRule model) {
    	UpdateWrapper<HorseRule> uw = new UpdateWrapper<>();
    	uw.eq("ACTNO", model.getActno());
    	uw.eq("RULENO", model.getRuleno());
        horseRuleService.update(model, uw);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody HorseRule model) {
    	QueryWrapper<HorseRule> qw = new QueryWrapper<>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("RULENO", model.getRuleno());
        horseRuleService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     HorseRule model = horseRuleService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        HorseRule model = new  HorseRule();
        List<HorseRule> list =  horseRuleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:horseRule:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = horseRuleService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    @RequestMapping("/dictTree")
    public HttpResult findDictTree(@RequestBody Dict model){
    	QueryWrapper<Dict> qw =  new QueryWrapper<Dict>();
    	qw.eq("TABNAME", "TB_GM_HORSE_DIGITART");
    	List<Dict> dicts = dictService.list(qw);
    	List<Map<String,Object>> dictTree = new ArrayList<Map<String,Object>>();
    	String lastFiled = "";
    	for(Dict item : dicts){
    		if(lastFiled.equals(item.getFieldname())) continue;
    		 String value = item.getFieldname();
    		 String label = item.getDictinfo();
    		 Map<String,Object> map = new HashedMap();
    		 map.put("value", value);
    		 map.put("label", label);
    		 dictTree.add(map);
    		 lastFiled = value;
    	}
    	for(Map<String,Object> item : dictTree){
    		List<Map<String,Object>> child = new ArrayList<>();
    		String field = item.get("value").toString();
    		for(Dict dict:dicts){
    			if(field.equals(dict.getFieldname())){
    				Map<String,Object> map = new HashedMap();
    	    		 map.put("value", dict.getSval());
    	    		 map.put("label", dict.getSshow());
    	    		 child.add(map);
    			}
    		}
    		item.put("children", child);
    	}
    	
    	return HttpResult.ok(dictTree);
    }
}

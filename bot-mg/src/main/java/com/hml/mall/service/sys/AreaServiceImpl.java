package com.hml.mall.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.mall.entity.sys.Area;
import com.hml.mall.iface.sys.IAreaService;
import com.hml.mall.mapper.sys.AreaMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("areaService")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {


    @Override
    public List< Area> list(Area model) {
       QueryWrapper< Area> wrapper = new QueryWrapper();
       List<Area> list = super.list(wrapper);
       List<Area> pList = new ArrayList<Area>();
       List<Area> cList = new ArrayList<Area>();
       List<Area> tList = new ArrayList<Area>();
       for(Area area : list) {
    	   switch (area.getLevel()) {
    	   	case 1:
				pList.add(area);
				break;
    		case 2:
    			cList.add(area);
    			break;
    		case 3:
    			tList.add(area);
    			break;
			default:
				break;
		}
       }
       for(Area area:cList) {
    	   area = findChildren(area, tList);
       }
       for(Area area:pList) {
    	   area = findChildren(area, cList);
       }
       return pList; 
    }

    @Override
    public IPage< Area> pageList( Area model, Integer pageNum, Integer pageSize) {

       QueryWrapper< Area> wrapper = new QueryWrapper(model);
       return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    
    private Area findChildren(Area parent,List<Area> list){
    	List<Area> children = parent.getChildren();
    	if(children == null) {
    		children = new ArrayList<Area>();
    	}
    	for(Area area:list) {
    		if(area.getParentid() - parent.getCode()==0) {
    			children.add(area);
    		}
    	}
    	parent.setChildren(children);
    	return parent;
    }

}

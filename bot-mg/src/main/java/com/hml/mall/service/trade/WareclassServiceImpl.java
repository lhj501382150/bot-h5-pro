package com.hml.mall.service.trade;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Wareclass;
import com.hml.mall.entity.trade.WareclassAttrcol;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.trade.IWareclassService;
import com.hml.mall.mapper.trade.WareclassAttrcolMapper;
import com.hml.mall.mapper.trade.WareclassMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */

@Service("wareclassService")
public class WareclassServiceImpl extends ServiceImpl<WareclassMapper, Wareclass> implements IWareclassService {

	@Autowired
	private WareclassMapper wareclassMapper;
	
	@Autowired
	private WareclassAttrcolMapper wareclassAttrcolMapper;

    @Override
    public List< Wareclass> list(Wareclass model) {
       QueryWrapper< Wareclass> wrapper = new QueryWrapper();
       if(model!=null) {
    	   if(!StringUtils.isBlank(model.getCno())) {
    		   wrapper.eq("CNO", model.getCno());
    	   }
    	   if(model.getClevel() != null) {
    		   wrapper.eq("CLEVEL", model.getClevel());
    	   }
    	   if(!StringUtils.isBlank(model.getSysblkno())) {
    		   wrapper.eq("SYSBLKNO", model.getSysblkno());
    	   }
       }
       wrapper.orderByAsc("CLEVEL");
       wrapper.orderByAsc("CNO");
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareclassMapper);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean saveWareclass(Wareclass entity) throws Exception {
    	entity = setWareClassCleveL(entity);
		wareclassMapper.insert(entity);
		
		saveAttr(entity.getCno(),entity.getAttrcols());
		return true;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateWareClass(Wareclass entity) throws Exception {
    	entity = setWareClassCleveL(entity);
		wareclassMapper.updateById(entity);
		deleteAttr(entity.getCno());
		
		saveAttr(entity.getCno(),entity.getAttrcols());
		return true;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
    	wareclassMapper.deleteById(id);
    	
    	deleteAttr(id);
    	return true;
    }
    
    /*
     * 删除商品分类属性信息
     */
    private void deleteAttr(Serializable id) {
    	QueryWrapper<WareclassAttrcol> qw = new QueryWrapper<WareclassAttrcol>();
    	qw.eq("CNO", id);
    	wareclassAttrcolMapper.delete(qw);
    }
    
    private void saveAttr(String cno,List<WareclassAttrcol> attrcols) {
		if(attrcols !=null && attrcols.size() > 0) {
			for(WareclassAttrcol attr: attrcols) {
				attr.setCno(cno);
				wareclassAttrcolMapper.insert(attr);
			}
		}
    }
//  保存层级关系
    private Wareclass setWareClassCleveL(Wareclass wc) throws Exception {
    	Integer clevel = wc.getClevel();
    	if(clevel > 1) {
    		if(StringUtils.isBlank(wc.getParentno())) {
    			throw new RuntimeException("未设置上级信息");
    		}
    		Wareclass parent = wareclassMapper.selectById(wc.getParentno());
    		if(parent == null) {
    			throw new RuntimeException("上级信息不存在");
    		}
    		parent.setCno(wc.getCno());
    		parent.setCname(wc.getCname());
    		parent.setClevel(wc.getClevel());
    		parent.setParentno(wc.getParentno());
    		parent.setAttrcols(wc.getAttrcols());
    		parent.setFilepath(wc.getFilepath());
    		parent.setSeq(wc.getSeq());
    		parent.setMem(wc.getMem());
    		wc = setClevelVal(wc.getClevel(), wc.getCno(), parent);
    	}else if(clevel == 1){
    		wc = setClevelVal(wc.getClevel(), wc.getCno(), wc);
    	}else {
    		throw new Exception("商品级别设置不正确");
    	}
    	return wc;
    }
    
//  为曾级赋值
  private Wareclass setClevelVal(int level,String cno,Wareclass wc)throws Exception{
	  if(level > 14 || level < 0) throw new RuntimeException("暂不支持此层级");
  	String fieldName = "cno" + level;
  	Field field = wc.getClass().getDeclaredField(fieldName);
  	 field.setAccessible(true);
  	 field.set(wc, cno);
  	 return wc;
  }
}

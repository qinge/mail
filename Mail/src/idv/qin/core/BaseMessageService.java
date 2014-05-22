package idv.qin.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装常用的对数据的处理( 算了 以后再做吧 本地分页)
 * @author qinge
 *
 */
public abstract class BaseMessageService<T> {
	
	public List<T> getPagingData(File dir , int start , int end){
		List<T> list = new ArrayList<T>();
		return list;
	}
	
	

}

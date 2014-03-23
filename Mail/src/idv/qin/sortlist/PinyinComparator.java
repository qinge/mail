package idv.qin.sortlist;

import idv.qin.domain.ContactsBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<ContactsBean> {

	@Override
	public int compare(ContactsBean bean_1, ContactsBean bean_2) {
		//这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
		if(bean_2.sortLetters.equals("#")){
			return -1;
		}else if(bean_1.sortLetters.equals("#")){
			return 1;
		}else{
			return bean_1.sortLetters.compareTo(bean_2.sortLetters);
		}
	}

}

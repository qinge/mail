package idv.qin.utils;

import idv.qin.domain.MailMessageBean;

import java.util.Comparator;

/**
 * 对收件箱中的邮件进行时间排序
 * @author qinge
 *
 */
public class CustomComparator implements Comparator<MailMessageBean> {

	@Override
	public int compare(MailMessageBean lhs, MailMessageBean rhs) {
		return rhs.mailHead.sendDate.compareTo(lhs.mailHead.sendDate);
	}


}

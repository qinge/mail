package idv.qin.domain;

import java.io.Serializable;

/**
 * 保持用户信息
 * @author qin
 *
 */
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String password;
	public boolean isChoosed; // 是否是选择的
}

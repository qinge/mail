package idv.qin.doamin;

import java.io.Serializable;

/**
 * @author qinge
 *
 */
public class AttachBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String path;
	public String name;
	public ExtraType extraType;
	
	@Override
	public String toString() {
		return "AttachBean [path=" + path + ", name=" + name + ", extraType="
				+ extraType + "]";
	}
	
}

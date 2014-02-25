package idv.qin.doamin;

/** 联系人实体类  */
public class Contacts {

	/** 由数据库自动设置保存的时候可以不用赋值 */
	public Integer id;
	public String name;
	public String mail_address;
	
	@Override
	public String toString() {
		return "Contacts [id=" + id + ", name=" + name + ", mail_address="
				+ mail_address + "]";
	}
	
	
}

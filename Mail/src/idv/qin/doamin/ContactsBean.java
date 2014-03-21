package idv.qin.doamin;

/** 联系人实体类  */
public class ContactsBean {

	/** 由数据库自动设置保存的时候可以不用赋值 */
	public Integer id;
	
	public String name;
	public String mail_address;
	/** 用于排序的关键字段*/
	public String sortLetters;
	
	@Override
	public String toString() {
		return "ContactsBean [id=" + id + ", name=" + name + ", mail_address="
				+ mail_address + ", sortLetters=" + sortLetters + "]";
	}
	
	
	
}
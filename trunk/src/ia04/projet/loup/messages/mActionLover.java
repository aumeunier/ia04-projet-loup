package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mActionLover extends mMessage {
	private String lover1;
	private String lover2;
	private Roles lover1Role;
	private Roles lover2Role;
	
	public Roles getLover1Role() {
		return lover1Role;
	}

	public void setLover1Role(Roles lover1Role) {
		this.lover1Role = lover1Role;
	}

	public Roles getLover2Role() {
		return lover2Role;
	}

	public void setLover2Role(Roles lover2Role) {
		this.lover2Role = lover2Role;
	}

	public String getLover1() {
		return lover1;
	}

	public void setLover1(String lover1) {
		this.lover1 = lover1;
	}

	public String getLover2() {
		return lover2;
	}

	public void setLover2(String lover2) {
		this.lover2 = lover2;
	}

	public mActionLover(){
		super();
	}
}

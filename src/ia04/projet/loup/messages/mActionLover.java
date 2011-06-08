package ia04.projet.loup.messages;

public class mActionLover extends mMessage {
	private String lover1;
	private String lover2;
	
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

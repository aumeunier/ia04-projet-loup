package ia04.projet.loup.roles;

public class ConfidenceLevel{
	private int level;
	public static int ILOVEHIM = 30;
	public static int VOTEFORME = -10;
	public static int VOTEFORMYROLE = -5;
	public static int VOTEFOROPPONENT = +5;
	public static int FRIENDWANTSTOEATHIM = -1;
	public static int ISWEREWOLF = -30;
	public static int ISVILLAGER = 30;
	
	
	public ConfidenceLevel(){
		level = -1;
	}
	
	public ConfidenceLevel(int baseLvl, int badFacing){
		level = baseLvl+badFacing;
	}
	
	public void update(int value){
		level += value;
	}

	public int getLevel() {
		return level;
	}
	
	public void setLevel(int l) {
		level = l;
	}
}

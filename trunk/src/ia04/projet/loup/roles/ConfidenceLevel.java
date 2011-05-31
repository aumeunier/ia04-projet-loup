package ia04.projet.loup.roles;

public class ConfidenceLevel{
	private int level;
	public static int VOTEFORME = -10;
	public static int VOTEFORMYROLE = -5;
	public static int VOTEFOROPPONENT = +5;
	
	public ConfidenceLevel(){
		level = 50;
	}
	
	public void update(int value){
		level += value;
	}
}

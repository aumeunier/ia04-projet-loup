package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;

import java.util.Random;

public class ConfidenceLevel{
	private int level;
	public static int ILOVEHIM = 30;
	public static int VOTEFORME = -10;
	public static int VOTEFORMYROLE = -5;
	public static int VOTEFOROPPONENT = +5;
	public static int FRIENDWANTSTOEATHIM = -1;
	
	public ConfidenceLevel(int badFacing){
		level = 45+badFacing;
	}
	
	public void update(int value){
		level += value;
	}

	public int getLevel() {
		return level;
	}

}

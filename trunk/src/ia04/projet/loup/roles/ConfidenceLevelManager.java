package ia04.projet.loup.roles;

import java.util.HashMap;

public class ConfidenceLevelManager {

	/** Map of the players with the corresponding confidence level */
	protected HashMap<String, ConfidenceLevel> confidenceLevelMap = new HashMap<String, ConfidenceLevel>();
	
	public void put(String name, ConfidenceLevel confidenceLevel){
		this.confidenceLevelMap.put(name, confidenceLevel);
	}
	
	public void remove(String name){
		this.confidenceLevelMap.remove(name);
	}
	
	public void update(String name, int value){
		this.confidenceLevelMap.get(name).update(value);
	}
	
	public int getLevel(String name) {
		return this.confidenceLevelMap.get(name).getLevel();
	}
	
}

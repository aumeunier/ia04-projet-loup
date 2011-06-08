package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mActionClairvoyant extends mMessage {
	private String chosenPlayer;
	private Roles role;
	public String getChosenPlayer() {
		return chosenPlayer;
	}
	public void setChosenPlayer(String chosenPlayer) {
		this.chosenPlayer = chosenPlayer;
	}
	public Roles getRole() {
		return role;
	}
	public void setRole(Roles role) {
		this.role = role;
	}
}

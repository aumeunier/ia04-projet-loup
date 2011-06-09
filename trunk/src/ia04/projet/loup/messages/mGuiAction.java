package ia04.projet.loup.messages;

public class mGuiAction extends mMessage{
	
	private String choice;

	public static mGuiAction parseJson(String jsonString){
		return (mGuiAction)mMessage.parseJson(jsonString, mGuiAction.class);
	}
	
	/**
	 * @param choice the choice to set
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * @return the choice
	 */
	public String getChoice() {
		return choice;
	}
}

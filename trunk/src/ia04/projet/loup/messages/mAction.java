package ia04.projet.loup.messages;

public class mAction extends mMessage {

	private Integer numberOfActionPerformed = 0;
	
	public mAction() {
	}

	public static mAction parseJson(String json) {
		return (mAction) mMessage.parseJson(json, mAction.class);
	}

	/**
	 * @param numberOfActionPerformed the numberOfActionPerformed to set
	 */
	public void setNumberOfActionPerformed(Integer numberOfActionPerformed) {
		this.numberOfActionPerformed = numberOfActionPerformed;
	}

	/**
	 * @return the numberOfActionPerformed
	 */
	public Integer getNumberOfActionPerformed() {
		return numberOfActionPerformed;
	}
}

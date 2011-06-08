package ia04.projet.loup.messages;

public class mTimeElapsed extends mMessage {
	private String typeTimeElapsed;

	public void setTypeTimeElapsed(String _type){
		this.typeTimeElapsed = _type;
	}

	public String getTypeTimeElapsed(){
		return typeTimeElapsed;
	}
	
	public static mTimeElapsed parseJson(String jsonString){
		return (mTimeElapsed)mMessage.parseJson(jsonString, mTimeElapsed.class);
	}
}

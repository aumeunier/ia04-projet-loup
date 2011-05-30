package ia04.projet.loup.messages;

public class mAction extends mMessage {

	public mAction() {
	}

	public static mAction parseJson(String json) {
		return (mAction) mMessage.parseJson(json, mAction.class);
	}
}

package ia04.projet.loup.messages;

public interface IMessage {
	public String toJson();
	public String toString();
	public String jsonToString(String jsonString);
}

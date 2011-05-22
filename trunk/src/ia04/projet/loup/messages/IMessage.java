package ia04.projet.loup.messages;

public interface IMessage {
	/**
	 * Transform the message to a message that can be sent
	 * @return A JSON string representing the message content
	 */
	public String toJson();
	
	/**
	 * Transform the message to a human-understandable form
	 * @return A string human-readable representation of the message
	 */
	public String toString();
	
	/**
	 * Transform the JSON string given to a human-understandable string.
	 * @param jsonString The JSON string to transform
	 * @return A string that you and me can read easily
	 */
	public String jsonToString(String jsonString);
}

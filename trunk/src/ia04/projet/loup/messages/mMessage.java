package ia04.projet.loup.messages;

import java.io.StringWriter;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This class provides the basic methods to serialize and deserialize an mMessage instance
 * @author paul
 */
public abstract class mMessage implements IMessage{
	
	/**
	 * Transform the Json String into a mMessage instance
	 * @param jsonString
	 * @param aClass specify a class which must extend mMessage
	 * @return mMessage
	 */
	public static mMessage parseJson(String jsonString, Class<?> aClass){
		
		ObjectMapper m = new ObjectMapper();
		mMessage r = null;
	
		try
		{
			r = (mMessage)m.readValue(jsonString, aClass);
		}
		catch(Exception e)
		{
			//Debugger.println("mMessage.parseJson - error : " + e);
			return null;
		}
		
		return r;
	}

	
	/**
	 * Transform the message to a message that can be sent
	 * @return A JSON string representing the message content
	 */
	@Override
	public String toJson() {
		StringWriter sw = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		
		try
		{
			m.writeValue(sw, this);
			return sw.toString();
		}
		catch(Exception e)
		{
			System.out.println("mMessage.toJson() - error : " + e);
			return null;
		}
	}
	
	/**
	 * Transform the message to a human-understandable form
	 * @return A string human-readable representation of the message
	 */
	public String toString(){
		return null;
	}

	/**
	 * Transform the JSON string given to a human-understandable string.
	 * @param jsonString The JSON string to transform
	 * @return A string that you and me can read easily
	 */
	@Override
	public String jsonToString(String jsonString) {
		return null;
	}
}

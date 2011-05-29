package ia04.projet.loup;

public class Debugger {
	
	private static boolean on = true;
	
	public static void println(String str){
		if(on){
			System.out.println(str);
		}
	}

	/**
	 * @param on the on to set
	 */
	public static void setOn(boolean on) {
		Debugger.on = on;
	}

	/**
	 * @return the on
	 */
	public static boolean isOn() {
		return on;
	}
	
}

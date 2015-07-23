package chatletcreator;

public class Token {

	public static String token="";
	public static String userId="";
	public static String rooms="";
	public static String room[]=new String[20];
	public static String authEmail="";
	public static String authPassword="";
	
	public static String getAuthEmail() {
		return authEmail;
	}
	public static void setAuthEmail(String authEmail) {
		Token.authEmail = authEmail;
	}
	public static String getAuthPass() {
		return authPassword;
	}
	public static void setAuthPass(String authPass) {
		Token.authPassword = authPass;
	}
	public static String[] getRoom() {
		return room;
	}
	public static void setRoom(String[] room) {
		Token.room = room;
	}
	public static String getRooms() {
		return rooms;
	}
	public static void setRooms(String rooms) {
		Token.rooms = rooms;
	}
	public static String getToken() {
		return token;
	}
	public static void setToken(String token) {
		Token.token = token;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		Token.userId = userId;
	}
	
}

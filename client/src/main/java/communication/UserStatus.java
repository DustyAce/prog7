package communication;

public class UserStatus {
    private static boolean loggedIn = false;
    private static String username;
    private static String password;

    public static void setCredentials(String user, String pass){
        loggedIn = true;
        username=user;
        password=pass;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }
}

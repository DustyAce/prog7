package communication;

public class UserStatus {
    private static boolean loggedIn = false;
    private static String username;
    private static String password;
    private static int serverPort = 1430;

    public static void setCredentials(String user, String pass){
        loggedIn = true;
        username=user;
        password=pass;
    }

    public static void setServerPort(int serverPort) {
        UserStatus.serverPort = serverPort;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void resetServerPort() {
        serverPort=1430;
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

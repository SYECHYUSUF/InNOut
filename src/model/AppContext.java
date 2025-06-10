package model;

public class AppContext {
    private static Object currentUser;

    public static Object getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Object user) {
        currentUser = user;
    }

    public static boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    public static boolean isPengguna() {
        return currentUser instanceof Pengguna;
    }
}


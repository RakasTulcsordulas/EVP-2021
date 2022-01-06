package com.app.evp2021.services;
/**
 * Class that manages the user session.
 */
public final class UserSession {
    private static UserSession instance;

    private static String employee;
    private static boolean loggedIn;
    /**
     * Private constructor.
     * @param employee Employee name.
     * @param loggedIn Logged in status.
     */
    private UserSession(String employee, boolean loggedIn) {
        this.employee = employee;
        this.loggedIn = loggedIn;
    }

    /**
     * Update user session.
     * @param employee Employee name.
     * @param loggedIn Logged in status.
     */
    public static UserSession setSession(String employee, boolean loggedIn) {
        instance = new UserSession(employee, loggedIn);
        return instance;
    }

    public static UserSession getSession() {
        return instance;
    }

    public String getEmployee() {
        return employee;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public static void logout() {
        employee = null;
        loggedIn = false;
    }

}

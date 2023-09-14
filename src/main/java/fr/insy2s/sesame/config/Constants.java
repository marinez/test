package fr.insy2s.sesame.config;

/**
 * Applications constants.
 *
 * @author Fethi Benseddik
 */
public final class Constants {


    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SUPER_MANAGER = "ROLE_SUPER_MANAGER";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_COMMERCIAL = "ROLE_COMMERCIAL";
    public static final String ADMIN = "ADMIN";
    public static final String SUPER_MANAGER = "SUPER_MANAGER";
    public static final String MANAGER = "MANAGER";
    public static final String COMMERCIAL = "COMMERCIAL";
    public static final String ANONYMOUS = "anonymous";
    public static final int MAX_FAILED_ATTEMPTS = 3;

    public static final int MAX_LENGTH_UUID = 36;
    public static final int LENGTH_ACTIVATION_KEY = 20;
    public static final int YEAR_MINIMUM = 18;
    public static final int LENGTH_PASSWORD = 20;
    private Constants() {
    }

}

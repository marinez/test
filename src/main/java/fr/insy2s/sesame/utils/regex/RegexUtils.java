package fr.insy2s.sesame.utils.regex;

/**
 * class containing all the regex used in the application
 *
 * @author Marine Zimmer
 */
public class RegexUtils {
    public static final String REGEX_ALPHABETIC_NAME = "^[A-Za-zÀ-ÖØ-öø-ÿ '-]*$";
    public static final String REGEX_PHONE = "^([0-9]{2} ){4}[0-9]{2}$|^\\+[0-9]{2} [0-9]( [0-9]{2}){4}$";
    public static final String REGEX_ALPHANUMERIC_NAME = "^[0-9A-Za-zÀ-ÖØ-öø-ÿ '-]*$";
    public static final String REGEX_SIREN = "^[0-9]{9}$";
    public static final String REGEX_ZIP_CODE = "^[0-9]{5}$";

    private RegexUtils() {
    }
}
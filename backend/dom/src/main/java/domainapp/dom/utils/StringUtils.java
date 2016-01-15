package domainapp.dom.utils;

public final class StringUtils {

    private StringUtils() {
    }

    public static String wildcardToCaseInsensitiveRegex(final String pattern) {
        if(pattern == null) {
            return null;
        }
        return "(?i)".concat(wildcardToRegex(pattern));
    }

    public static String wildcardToRegex(final String pattern) {
        if(pattern == null) {
            return null;
        }
        return pattern.replace("*", ".*").replace("?", ".");
    }

}

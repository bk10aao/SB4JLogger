package sb4jLogger;

public class UniversalMasker {

    public static String obfuscate(Object value) {
        return obfuscate(value, Integer.MAX_VALUE);
    }

    public static String obfuscate(Object value, int characters) {
        if(value == null)
            return "null";
        if (characters < 0)
            throw new IllegalArgumentException("Characters to obfuscate cannot be negative: " + characters);
        String inputString =  value.toString();
        int obfuscateCount = Math.min(characters, inputString.length());
        return "*".repeat(obfuscateCount) + inputString.substring(obfuscateCount);
    }
}

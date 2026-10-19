package sb4jLogger;

public class Obfuscator {

    public String obfuscate(Object value) {
        if(value == null)
            return "null";
        return "*".repeat(value.toString().length());
    }
}

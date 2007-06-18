package net.unicon.issueScraper;

public class RegexStringInterceptor implements IStringInterceptor {
    
    private String name;
    private String pattern;
    private String replacement;

	public String intercept(String input) {
        return input.replaceAll(pattern, replacement);
    }
 
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
}

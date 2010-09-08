package net.unicon.issueScraper;

public interface IStringInterceptor {
    public String getName();
    public String intercept(String input);
}

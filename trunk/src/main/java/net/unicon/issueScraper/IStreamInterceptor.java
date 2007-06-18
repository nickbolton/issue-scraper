package net.unicon.issueScraper;

import java.io.InputStream;

public interface IStreamInterceptor {
    public String getName();
    public InputStream intercept(InputStream is);
}

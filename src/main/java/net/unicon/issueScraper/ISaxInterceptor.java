package net.unicon.issueScraper;

import org.xml.sax.ContentHandler;

public interface ISaxInterceptor {
    public String getName();
    public void intercept(ContentHandler handler);
}

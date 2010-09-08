package net.unicon.issueScraper;

import org.dom4j.Document;

public interface IDomInterceptor {
    public String getName();
    public void intercept(Document doc);
}

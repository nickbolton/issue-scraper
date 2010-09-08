package net.unicon.issueScraper;

import java.util.List;

public class IssuePropertyParser implements IIssuePropertyParser {
    
    private String xPath;
    private List<IStringInterceptor> interceptors;

    public List<IStringInterceptor> getInterceptors() {
        return interceptors;
    }

    public String getXPath() {
        return xPath;
    }

    public void setInterceptors(List<IStringInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void setXPath(String path) {
        xPath = path;
    }

}

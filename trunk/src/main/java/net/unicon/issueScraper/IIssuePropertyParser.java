package net.unicon.issueScraper;

import java.util.List;

public interface IIssuePropertyParser {
    public String getXPath();
    public List<IStringInterceptor> getInterceptors();
}

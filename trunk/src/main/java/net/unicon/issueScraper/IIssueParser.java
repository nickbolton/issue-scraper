package net.unicon.issueScraper;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

public interface IIssueParser {
    public Map<String, IIssue> parseResult(Document doc) throws Exception;
    public String getPaginationUrlXPath();
    public String getIssuePageLinkPrefix();
    public List<IStringInterceptor> getPaginationUrlInterceptors();
}

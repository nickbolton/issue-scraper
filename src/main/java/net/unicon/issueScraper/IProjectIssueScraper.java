package net.unicon.issueScraper;

import java.util.List;

public interface IProjectIssueScraper {
    public String getProject();
    public boolean isUrlFromThisProject(String url);
    public String parseIssueIdFromUrl(String url);
    public List<IIssue> fetchNamedQuery(String queryName);
    public List<IIssue> fetchNamedQuery(String queryName, boolean forceQuery);
    public IIssue getIssue(String id);
    public IIssue getIssue(String id, boolean forceQuery);
    public void stop();
}

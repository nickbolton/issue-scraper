package net.unicon.issueScraper;

import java.util.List;

public interface IIssueTrackerManager {
    
    public boolean doesUrlBelongToProject(String project, String url);
    public IIssue getIssue(String url);
    public IIssue getIssue(String url, boolean forceQuery);
    public IIssue getIssue(String project, String issueId);
    public IIssue getIssue(String project, String issueId, boolean forceQuery);
    public List<IIssue> getIssuesFromNamedQuery(String project, String queryName);
    public List<IIssue> getIssuesFromNamedQuery(String project, String queryName, boolean forceQuery);
}

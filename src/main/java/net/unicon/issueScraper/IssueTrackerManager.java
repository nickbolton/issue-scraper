package net.unicon.issueScraper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IssueTrackerManager implements IIssueTrackerManager {
    
    private Log log = LogFactory.getLog(getClass());
    private Map<String, IProjectIssueScraper> scraperMap;

    public IIssue getIssue(String url) {
        return getIssue(url, false);
    }
    
    public IIssue getIssue(String url, boolean forceQuery) {
        
        if (log.isDebugEnabled()) {
            log.debug("Getting issue for url: " + url);
        }
        Iterator<String> itr = scraperMap.keySet().iterator();
        IIssue issue = null;
        
        while (issue == null && itr.hasNext()) {
            String project = itr.next();
            IProjectIssueScraper scraper = scraperMap.get(project);
            if (scraper.isUrlFromThisProject(url)) {
                String issueId = scraper.parseIssueIdFromUrl(url);
                if (issueId != null) {
	                issue = scraper.getIssue(issueId, forceQuery);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Issue for url: " + url + " - " + issue);
        }
        return issue;
    }

    public IIssue getIssue(String project, String issueId) {
        return getIssue(project, issueId, false);
    }
    
    public IIssue getIssue(String project, String issueId, boolean forceQuery) {
        if (log.isDebugEnabled()) {
            log.debug("Getting issue for project/issueId: " + project + "/" + issueId);
        }
        
        IProjectIssueScraper scraper = scraperMap.get(project);
        IIssue issue = null;
        
        if (scraper != null) {
            issue = scraper.getIssue(issueId, forceQuery);
        } else {
            log.error("No scraper found for project: " + project);
        }
        if (log.isDebugEnabled()) {
            log.debug("Issue for project/issueId: " + project + "/" + issueId + " - " + issue);
        }
        return issue;
    }

    public Map<String, IProjectIssueScraper> getScraperMap() {
        return scraperMap;
    }

    public void setScraperMap(Map<String, IProjectIssueScraper> scraperMap) {
        this.scraperMap = scraperMap;
    }

    public List<IIssue> getIssuesFromNamedQuery(String project, String queryName) {
        return getIssuesFromNamedQuery(project, queryName, false);
    }
    
    public List<IIssue> getIssuesFromNamedQuery(String project, String queryName, boolean forceQuery) {
        List<IIssue> issues = null;
        IProjectIssueScraper scraper = scraperMap.get(project);
        if (scraper != null) {
            issues = scraper.fetchNamedQuery(queryName, forceQuery);
        } else {
            issues = Collections.emptyList();
        }
        return issues;
    }

}

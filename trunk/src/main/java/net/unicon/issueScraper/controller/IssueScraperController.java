package net.unicon.issueScraper.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Element;
import net.unicon.issueScraper.IIssue;
import net.unicon.issueScraper.IIssueTrackerManager;
import net.unicon.issueScraper.cache.CacheUtility;
import net.unicon.issueScraper.cache.CacheWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.web.servlet.ModelAndView;

public class IssueScraperController extends AbstractCacheController {
    
    private Log log = LogFactory.getLog(getClass());
    private IIssueTrackerManager issueTrackerManager;
        
    public IssueScraperController() {
        super();
    }

    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, DocumentException {
        
        String url = req.getParameter("url");
        
        Document issueDoc = null;
        
        if (url != null) {
            issueDoc = getIssue(url);
            if (issueDoc != null) {
                sendIssue(resp.getWriter(), issueDoc);
                return null;
            }
        }
        
        String project = req.getParameter("project");
        String issueId = req.getParameter("issueId");
        if (project != null && issueId != null) {
            issueDoc = getIssue(project, issueId);
            if (issueDoc != null) {
                sendIssue(resp.getWriter(), issueDoc);
                return null;
            }
        }
        
        log.error("No issue found for: url="+url + ", project="+project + ", issueId=" + issueId);
        
        return null;
    }
    
    private void sendIssue(PrintWriter out, Document doc) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter( out, format );
        writer.write(doc);
    }
    
    private Document getIssue(String project, String issueId) throws IOException, DocumentException {
        if (log.isDebugEnabled()) {
            log.debug("Getting issue for project="+project+", issueId="+issueId);
        }
        Document issueDoc = null;
        CacheUtility cacheUtility = getCacheUtility();
        
        try {
            CacheWrapper cacheWrapper = cacheUtility.getCacheWrapper();
            Element element = cacheWrapper.get(issueId);
            
            
            if (element == null) {
                if (log.isDebugEnabled()) {
                    log.debug("cache miss for issueId: " + issueId);
                }
                IIssue issue = issueTrackerManager.getIssue(project, issueId);
                if (issue != null) {
                    issueDoc = issue.toDocument();
                    element = new Element(issueId, issue);
                    cacheWrapper.put(element);
                    element = new Element(issue.getIssueUrl(), issue);
                    cacheWrapper.put(element);
                    cacheUtility.storePreviousVersion(issueId, issueDoc);
                    if (log.isDebugEnabled()) {
                        if (cacheWrapper.get(issueId) == null) {
                            log.debug("element not cached for key: " + issueId);
                        }
                        if (cacheWrapper.get(issue.getIssueUrl()) == null) {
                            log.debug("element not cached for key: " + issue.getIssueUrl());
                        }
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("cache hit for issueId: " + issueId);
                }
                IIssue issue = (IIssue)element.getValue();
                if (issue != null) {
                    issueDoc = issue.toDocument();
                }
            }
        } catch (Exception e) {
            log.error("Failed scraping for project: " + project + ", issueId: " + issueId, e);
        }
        
        try {
            if (issueDoc == null) {
                issueDoc = cacheUtility.getPreviousVersion(issueId);
            }
        } catch (DocumentException de) {
            log.error("Failed retrieving previously cached version for project: " + project + ", issueId: " + issueId, de);
        }

        
        return issueDoc;
        
    }
    
    private Document getIssue(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Getting issue for url="+url);
        }
        Document issueDoc = null;
        CacheUtility cacheUtility = getCacheUtility();
        try {
            CacheWrapper cacheWrapper = cacheUtility.getCacheWrapper();
            Element element = cacheWrapper.get(url);
            
            
            if (element == null) {
                if (log.isDebugEnabled()) {
                    log.debug("cache miss for url: " + url);
                }
                IIssue issue = issueTrackerManager.getIssue(url);
                if (issue != null) {
                    issueDoc = issue.toDocument();
                    element = new Element(url, issue);
                    cacheWrapper.put(element);
                    element = new Element(issue.getId(), issue);
                    cacheWrapper.put(element);
                    cacheUtility.storePreviousVersion(url, issueDoc);
                    if (log.isDebugEnabled()) {
                        if (cacheWrapper.get(url) == null) {
                            log.debug("element not cached for key: " + url);
                        }
                        if (cacheWrapper.get(issue.getId()) == null) {
                            log.debug("element not cached for key: " + issue.getId());
                        }
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("cache hit for url: " + url);
                }
                IIssue issue = (IIssue)element.getValue();
                if (issue != null) {
                    issueDoc = issue.toDocument();
                }
            }
        } catch (Exception e) {
            log.error("Failed scraping for issue: " + url, e);
        }
        
        try {
            if (issueDoc == null) {
                issueDoc = cacheUtility.getPreviousVersion(url);
            }
        } catch (DocumentException de) {
            log.error("Failed retrieving previously cached version for: " + url, de);
        }
        
        return issueDoc;
        
    }

    public IIssueTrackerManager getIssueTrackerManager() {
        return issueTrackerManager;
    }

    public void setIssueTrackerManager(IIssueTrackerManager issueTrackerManager) {
        this.issueTrackerManager = issueTrackerManager;
    }
    
}

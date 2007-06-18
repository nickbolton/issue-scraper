package net.unicon.issueScraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.InitializingBean;

public class DefaultProjectIssueScraper implements IProjectIssueScraper, InitializingBean  {
    
    private static final String emptyString = "";
    
    private String project;
    private Map<String, IIssue> issueCache;
    private Map<String, List<IIssue>> queryCache;
    private Map<String, URL> namedQueries;
    
    private boolean startWorker;
    private int refreshPeriod;
    
    private String issueIdToken;
    private String issueLookupUrl;
    
    private Pattern issueUrlPattern;
    private String issueUrlExpression;
    
    private Pattern issueIdPattern;
    private String issueIdExpression;
    
    private List<IStreamInterceptor> streamInterceptors;
    private List<IStringInterceptor> stringInterceptors;
    private List<IDomInterceptor> domInterceptors;
    
    private IIssueParser issueParser;
    private IIssueParser queryParser;
    
    private Log log = LogFactory.getLog(getClass());
    
    private Thread workerThread;
    private IssueRefresherWorker worker;
    
    public DefaultProjectIssueScraper() {
        issueCache = new SmartCache(300);
    }
    
    public void afterPropertiesSet() throws Exception {
        if (startWorker) {
            worker = new IssueRefresherWorker();
            workerThread = new Thread(worker);
            workerThread.start();
        }
    }
    
    public void stop() {
        if (worker != null) {
            worker.setRunning(false);
        }
    }

    public List<IIssue> getIssues() {
        return Collections.unmodifiableList(new ArrayList<IIssue>(issueCache.values()));
    }
    
    public IIssue getIssue(String id) {
        return getIssue(id, false);
    }
    
    public IIssue getIssue(String id, boolean forceQuery) {
        IIssue issue = null;
        
        if (!forceQuery) {
            issue = issueCache.get(id);
            if (log.isDebugEnabled()) {
                if (issue != null) {
                    log.debug("Issue (" + project + ", " + id + ") CACHE HIT.");
                } else {
                    log.debug("Issue (" + project + ", " + id + ") CACHE MISS.");
                }
            }
        }
        
        if (issue == null) {
            String issueUrl = issueLookupUrl.replaceAll(issueIdToken, id);
            if (log.isDebugEnabled()) {
                log.debug("issueUrl: " + issueUrl);
            }
            fetchIssues(issueUrl);
            issue = issueCache.get(id);
                    
            if (issue == null) {
                log.warn("Attempted to fetch issue(" +
                    project + ", " + issueUrl + ", " + id+ ") returned no issue.");
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("returing issue for id("+id+"): " + issue);
        }
        return issue;
    }

    public String parseIssueIdFromUrl(String url) {
        Matcher m = issueIdPattern.matcher(url);
        if (m.find() && m.groupCount()==1) {
            return m.group(1);
        }
        return null;
    }
    
    public boolean isUrlFromThisProject(String url) {
        Matcher m1 = issueUrlPattern.matcher(url);
        Matcher m2 = issueIdPattern.matcher(url);
        return m1.find() && m2.find();
    }
    
    public List<IIssue> fetchNamedQuery(String queryName) {
        return fetchNamedQuery(queryName, false);
    }
    
    public List<IIssue> fetchNamedQuery(String queryName, boolean forceQuery) {
        if (queryName == null || emptyString.equals(queryName.trim())) {
            throw new RuntimeException("queryName must be specified.");
        }
        URL url = namedQueries.get(queryName);
        if (url == null) return Collections.EMPTY_LIST;
        
        List<IIssue> issues = null;
        
        if (!forceQuery) {
	        issues = queryCache.get(queryName);
	        if (log.isDebugEnabled()) {
                if (issues != null) {
                    log.debug("Issue query (" + project + ", " + queryName + ") CACHE HIT.");
                } else {
                    log.debug("Issue query (" + project + ", " + queryName + ") CACHE MISS.");
                }
            }

        }
        
        if (issues == null) {
            issues = fetchIssues(url.toString());
            queryCache.put(queryName, issues);
        }
        
        return issues;
    }

    protected List<IIssue> fetchIssues(String url) {
        List<IIssue> issues = new ArrayList<IIssue>();
        fetchIssues(url, issues);
        return issues;
    }
    
    private void fetchIssues(String url, List<IIssue> issues) {
        
        if (log.isDebugEnabled()) {
            log.debug("fetching issues at url: " + url);
        }
        
        HttpClient client = new HttpClient();
        GetMethod method = null;
        
        try {
            method = new GetMethod(url);
            method.setFollowRedirects(true);
            
            int rc = client.executeMethod(method);
            if (rc == 200) {
                long t = System.currentTimeMillis();
                String content = interceptStream(method.getResponseBodyAsStream());
                if (log.isDebugEnabled()) {
                    log.debug("stream interceptors took " + (System.currentTimeMillis()-t) + " ms");
                    log.debug("content after stream interceptors: " + content);
                }
                t = System.currentTimeMillis();
                content = interceptString(content);
                if (log.isDebugEnabled()) {
                    log.debug("string interceptors took " + (System.currentTimeMillis()-t) + " ms");
                    log.debug("content after string interceptors: " + content);
                }
                SAXReader reader = new SAXReader();
                t = System.currentTimeMillis();
                Document document = reader.read(new StringReader(content));
                if (log.isDebugEnabled()) {
                    log.debug("parsing content into dom took " + (System.currentTimeMillis()-t) + " ms");
                }
                t = System.currentTimeMillis();
                interceptDom(document);
                if (log.isDebugEnabled()) {
                    log.debug("dom interceptors took " + (System.currentTimeMillis()-t) + " ms");
                    log.debug("content after dom interceptors: ");
                    dumpElement(document.getRootElement());
                }
                t = System.currentTimeMillis();
                Map<String, IIssue> m = issueParser.parseResult(document);
                if (log.isDebugEnabled()) {
                    log.debug("issue parsing took " + (System.currentTimeMillis()-t) + " ms");
                }
                issueCache.putAll(m);
                issues.addAll(m.values());
                String nextUrl = getNextUrl(document);
                if (nextUrl != null) {
                    fetchIssues(nextUrl, issues);
                }
            } else {
                throw new RuntimeException("HTTP response not ok retrieving issue query. RC: " + rc + " for url: " + url);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed retrieving issue query for url: " + url, e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
    
    private void dumpElement(Element el) {
        if (!log.isDebugEnabled()) return;

        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter( sw, format );
        try {
            writer.write( el );
            log.debug("element:\n"+sw.toString());
        } catch (IOException e) {
            log.error("Failed to dump element.", e);
        }
    }
    
    protected String getNextUrl(Document document) {
        String nextUrl = null;
        
        if (issueParser.getPaginationUrlXPath() != null) {
            Element root = document.getRootElement();
            Node node = root.selectSingleNode(issueParser.getPaginationUrlXPath());
            if (node != null) {
                String value = node.getText();
                if (issueParser.getIssuePageLinkPrefix() != null) {
                    nextUrl = issueParser.getIssuePageLinkPrefix() + value;
                } else {
                    nextUrl = value;
                }
                if (issueParser.getPaginationUrlInterceptors() != null) {
                    Iterator<IStringInterceptor> itr = issueParser.getPaginationUrlInterceptors().iterator();
                    while (itr.hasNext()) {
                        nextUrl = itr.next().intercept(nextUrl);
                    }
                }
            } 
        }
        
        
        return nextUrl;
    }
    
    protected String interceptStream(InputStream is) throws IOException {
        InputStream modifiedStream = is;
        if (streamInterceptors != null) {
            Iterator<IStreamInterceptor> itr = streamInterceptors.iterator();
            while (itr.hasNext()) {
                IStreamInterceptor interceptor = itr.next();
                long t = System.currentTimeMillis();
                modifiedStream = interceptor.intercept(modifiedStream);
                if (log.isDebugEnabled()) {
                    log.debug("Stream interceptor " + interceptor.getName() + " took " + (System.currentTimeMillis()-t) + " ms");
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        byte[] buf = new byte[1024];
        int numRead;
        while ( (numRead = modifiedStream.read(buf, 0, 1024)) >= 0) {
            sb.append(new String(buf, 0, numRead));
        }
        return sb.toString();
    }
    
    protected String interceptString(String content) {
        String modifiedContent = content;
        
        if (stringInterceptors != null) {
            Iterator<IStringInterceptor> itr = stringInterceptors.iterator();
            while (itr.hasNext()) {
                IStringInterceptor interceptor = itr.next();
                long t = System.currentTimeMillis();
                modifiedContent = interceptor.intercept(modifiedContent);
                if (log.isDebugEnabled()) {
                    log.debug("String interceptor " + interceptor.getName() + " took " + (System.currentTimeMillis()-t) + " ms");
                }
            }
        }
        return modifiedContent;
    }
    
    protected void interceptDom(Document document) {
        if (domInterceptors != null) {
            Iterator<IDomInterceptor> itr = domInterceptors.iterator();
            while (itr.hasNext()) {
                IDomInterceptor interceptor = itr.next();
                long t = System.currentTimeMillis();
                interceptor.intercept(document);
                if (log.isDebugEnabled()) {
                    log.debug("Dom interceptor " + interceptor.getName() + " took " + (System.currentTimeMillis()-t) + " ms");
                }
            }
        }
    }
    
    public Map<String, URL> getNamedQueries() {
        return namedQueries;
    }

    public void setNamedQueries(Map<String, URL> namedQueries) {
        this.namedQueries = namedQueries;
    }

    public IIssueParser getIssueParser() {
        return issueParser;
    }

    public void setIssueParser(IIssueParser issueParser) {
        this.issueParser = issueParser;
    }
    
    public IIssueParser getQueryParser() {
        return queryParser;
    }

    public void setQueryParser(IIssueParser queryParser) {
        this.queryParser = queryParser;
    }

    public List getDomInterceptors() {
        return domInterceptors;
    }

    public void setDomInterceptors(List<IDomInterceptor> domInterceptors) {
        this.domInterceptors = domInterceptors;
    }

    public List getStringInterceptors() {
        return stringInterceptors;
    }

    public void setStringInterceptors(List<IStringInterceptor> stringInterceptors) {
        this.stringInterceptors = stringInterceptors;
    }

    public List<IStreamInterceptor> getStreamInterceptors() {
        return streamInterceptors;
    }

    public void setStreamInterceptors(List<IStreamInterceptor> streamInterceptors) {
        this.streamInterceptors = streamInterceptors;
    }
    
    public int getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(int refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }
    
    public String getIssueUrlExpression() {
        return issueUrlExpression;
    }

    public void setIssueUrlExpression(String issueUrlExpression) {
        this.issueUrlExpression = issueUrlExpression;
        this.issueUrlPattern = Pattern.compile(issueUrlExpression);
    }

    public String getIssueLookupUrl() {
        return issueLookupUrl;
    }

    public void setIssueLookupUrl(String issueLookupUrl) {
        this.issueLookupUrl = issueLookupUrl;
    }

    public String getIssueIdToken() {
        return issueIdToken;
    }

    public void setIssueIdToken(String issueIdToken) {
        this.issueIdToken = issueIdToken;
    }

	public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIssueIdExpression() {
        return issueIdExpression;
    }

    public void setIssueIdExpression(String issueIdExpression) {
        this.issueIdExpression = issueIdExpression;
        this.issueIdPattern = Pattern.compile(issueIdExpression);
    }
    
    protected class IssueRefresherWorker implements Runnable {
        
        private boolean running = true;
        
        public void run() {
            while (running) {
                Iterator<String> namedQueryItr = namedQueries.keySet().iterator();
                while (namedQueryItr.hasNext()) {
                    fetchNamedQuery(namedQueryItr.next());
                }
                try {
                    Thread.sleep(getRefreshPeriod()*1000);
                } catch (InterruptedException e) {
                    log.warn("Refresh Thread was interupted.");
                    running = false;
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
        
    }

    public Map<String, IIssue> getIssueCache() {
        return issueCache;
    }

    public void setIssueCache(Map<String, IIssue> cache) {
        this.issueCache = Collections.synchronizedMap(cache);
    }

    public Map<String, List<IIssue>> getQueryCache() {
        return queryCache;
    }

    public void setQueryCache(Map<String, List<IIssue>> queryCache) {
        this.queryCache = Collections.synchronizedMap(queryCache);
    }

    public boolean isStartWorker() {
        return startWorker;
    }

    public void setStartWorker(boolean startWorker) {
        this.startWorker = startWorker;
    }

}

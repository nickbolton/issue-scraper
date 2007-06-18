package net.unicon.issueScraper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DefaultIssueParser implements IIssueParser {
    
    private static final String issueUrlName = "issueUrl";
    private static final String statusName = "status";
    
    private Log log = LogFactory.getLog(getClass());
    private String issuesXPath;
    private String paginationUrlXPath;
    private Map<String, IIssuePropertyParser> issueXPathMap;
    private List<IStringInterceptor> paginationUrlInterceptors;
    private Set<String> closedStatusSet;
    private String issuePageLinkPrefix;
    
    private void logElementPaths(Element e) {
        if (!log.isDebugEnabled()) return;
        List elements = e.elements();
        for (int i=0; i<elements.size(); i++) {
            Element child = (Element)elements.get(i);
            log.debug("<"+child.getName()+">: " + child.getPath());
            if ("a".equals(child.getName().toLowerCase())) {
                Attribute href = child.attribute("href");
                if (href != null) {
                    log.debug("\thref: " + href.getText() + " - path: " + href.getPath());
                }
            }
            logElementPaths(child);
        }
    }
    
    private void dumpElement(Element el) throws IOException {
        if (!log.isDebugEnabled()) return;
        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter( sw, format );
        writer.write( el );
        
        log.debug("element:\n"+sw.toString());
    }

    public Map<String, IIssue> parseResult(Document document) throws Exception {
        Element root = document.getRootElement();
        
        //dumpElement(root);
        //logElementPaths(root);
        
        List issueList = null;
        
        if (issuesXPath != null) {
            issueList = root.selectNodes(issuesXPath);
        } else {
            issueList = new ArrayList(1);
            issueList.add(root);
        }
        
        Map<String, IIssue> issues = new HashMap<String, IIssue>(issueList.size());
        
        Object[] args = new Object[1];
        Class[] params = { String.class };
        Iterator itr = issueList.iterator();
        while (itr.hasNext()) {
            Element issueEl = (Element) itr.next();
            if (log.isDebugEnabled()) {
	            log.debug("processing issue element:");
            }
            //dumpElement(issueEl);
            IIssue issue = new Issue();
            
            if (issueXPathMap != null) {
	            Iterator<String> nameItr = issueXPathMap.keySet().iterator();
	            while (nameItr.hasNext()) {
	                String name = nameItr.next();
                    IIssuePropertyParser propertyParser = issueXPathMap.get(name);
	                String xpath = propertyParser.getXPath();
	                if (xpath != null) {
	                    Node node = issueEl.selectSingleNode(xpath);
	                    if (node != null) {
			                String value = node.getText().trim();
                            
                            if (propertyParser.getInterceptors() != null) {
                                Iterator<IStringInterceptor> interceptorItr = propertyParser.getInterceptors().iterator();
                                while (interceptorItr.hasNext()) {
                                    RegexStringInterceptor si = (RegexStringInterceptor)interceptorItr.next();
                                    if (log.isDebugEnabled()) {
                                        log.debug("interceptor target: " + si.getPattern() + " - value: " + value);
                                    }
                                    value = si.intercept(value);
                                    if ("description".equals(name)) {
                                        System.out.println("value: " + value);
                                    }
                                }
                            }
                            
			                if (issuePageLinkPrefix != null && issueUrlName.equals(name)) {
				                args[0] = issuePageLinkPrefix + value;
			                } else {
				                args[0] = value;
			                }
                            
			                try {
			                    Method getter = issue.getClass().getDeclaredMethod("set"+name.substring(0, 1).toUpperCase()+name.substring(1), params);
			                    getter.invoke(issue, args);
			                } catch (NoSuchMethodException e) {
			                    throw new RuntimeException("Issue getter not defined: " + name, e);
			                }
                            
                            if (statusName.equals(name)) {
	                            issue.setClosed(closedStatusSet != null && closedStatusSet.contains(value));
                            }
                                
	                    } else {
	                        if (log.isWarnEnabled()) {
	                            log.warn("Bad element xpath: " + xpath);
	                        }
	                    }
	                }
	            }
            }
            
            issues.put(issue.getId(), issue);
        }
        
        return issues;
    }
    
    public String getIssuesXPath() {
        return issuesXPath;
    }

    public void setIssuesXPath(String issuesXPath) {
        this.issuesXPath = issuesXPath;
    }

    public Map<String, IIssuePropertyParser> getIssueXPathMap() {
        return issueXPathMap;
    }

    public void setIssueXPathMap(Map<String, IIssuePropertyParser> issueElementsXPathMap) {
        this.issueXPathMap = issueElementsXPathMap;
    }

    public String getPaginationUrlXPath() {
        return paginationUrlXPath;
    }

    public void setPaginationUrlXPath(String paginationUrl) {
        this.paginationUrlXPath = paginationUrl;
    }

    public List<IStringInterceptor> getPaginationUrlInterceptors() {
        return paginationUrlInterceptors;
    }

    public void setPaginationUrlInterceptors(
        List<IStringInterceptor> paginationUrlInterceptors) {
        this.paginationUrlInterceptors = paginationUrlInterceptors;
    }

    public Set<String> getClosedStatusSet() {
        return closedStatusSet;
    }

    public void setClosedStatusSet(Set<String> closedStatusSet) {
        this.closedStatusSet = closedStatusSet;
    }

    public String getIssuePageLinkPrefix() {
        return issuePageLinkPrefix;
    }

    public void setIssuePageLinkPrefix(String issuePageLinkPrefix) {
        this.issuePageLinkPrefix = issuePageLinkPrefix;
    }

}

package net.unicon.issueScraper;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class Issue implements IIssue {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String project;
    private String assignee;
    private String description;
    private String environment;
    private String issueUrl;
    private String priority;
    private String reporter;
    private String resolution;
    private String status;
    private String summary;
    private String type;
    private boolean closed;
    private String issueScraperUrl;
    private String proxiedIssueScraperUrl;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('(').append(id).append(',');
        sb.append(summary).append(',');
        sb.append(description).append(',');
        sb.append(assignee).append(',');
        sb.append(environment).append(',');
        sb.append(issueUrl).append(',');
        sb.append(priority).append(',');
        sb.append(reporter).append(',');
        sb.append(resolution).append(',');
        sb.append(status).append(',');
        sb.append(type).append(',');
        sb.append(closed).append(')');
        return sb.toString();
    }

    public String getAssignee() {
        return assignee;
    }

    public String getDescription() {
        return description;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getId() {
        return id;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public String getPriority() {
        return priority;
    }

    public String getReporter() {
        return reporter;
    }

    public String getResolution() {
        return resolution;
    }

    public String getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public String getType() {
        return type;
    }

    public void setAssignee(String s) {
        this.assignee = s;
    }

    public void setDescription(String s) {
        this.description = s;
    }

    public void setEnvironment(String s) {
        this.environment = s;
    }

    public void setId(String s) {
        this.id = s;
    }

    public void setIssueUrl(String url) {
        this.issueUrl = url;
    }

    public void setPriority(String s) {
        this.priority = s;
    }

    public void setReporter(String s) {
        this.reporter = s;
    }

    public void setResolution(String s) {
        this.resolution = s;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public void setSummary(String s) {
        this.summary = s;
    }

    public void setType(String s) {
        this.type = s;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean b) {
        this.closed = b;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setIssueScraperUrls(String proxiedUrl, String issueScraperUrl) {
        this.issueScraperUrl = issueScraperUrl;
        proxiedIssueScraperUrl = proxiedUrl;
    }

    public String getIssueScaperUrl(boolean useProxy) {
        if (useProxy && proxiedIssueScraperUrl != null) {
            return proxiedIssueScraperUrl;
        } else {
            return issueScraperUrl;
        }
    }

    private void addElement(Element el, String name, String value) {
        if (value != null) {
            el.addElement(name).setText(value);
        }
    }

    public Document toDocument() {
        Document document = DocumentHelper.createDocument();
        Element issue = document.addElement("issue");
        addElement(issue, "id", id);
        addElement(issue, "project", project);
        addElement(issue, "assignee", assignee);
        addElement(issue, "environment", environment);
        addElement(issue, "issueUrl", issueUrl);
        addElement(issue, "priority", priority);
        addElement(issue, "reporter", reporter);
        addElement(issue, "resolution", resolution);
        addElement(issue, "status", status);
        addElement(issue, "type", type);
        addElement(issue, "closed", new Boolean(closed).toString());
        addElement(issue, "summary", summary);
        addElement(issue, "description", description);
        return document;
    }
    
    public IIssue fromDocument(Document doc) {
        if (doc == null) return null;
        Element root = doc.getRootElement();
        if (!"issue".equals(root.getName())) return null;
        
        IIssue issue = new Issue();
        issue.setId(root.elementText("id"));
        issue.setProject(root.elementText("project"));
        issue.setAssignee(root.elementText("assignee"));
        issue.setEnvironment(root.elementText("environment"));
        issue.setIssueUrl(root.elementText("issueUrl"));
        issue.setPriority(root.elementText("priority"));
        issue.setReporter(root.elementText("reporter"));
        issue.setResolution(root.elementText("resolution"));
        issue.setStatus(root.elementText("status"));
        issue.setType(root.elementText("type"));
        issue.setClosed(new Boolean(root.elementText("closed")));
        issue.setSummary(root.elementText("summary"));
        issue.setDescription(root.elementText("description"));
        return issue;
    }
}

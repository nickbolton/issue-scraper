package net.unicon.issueScraper;


public class Issue implements IIssue {
    
    private String id;
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
}

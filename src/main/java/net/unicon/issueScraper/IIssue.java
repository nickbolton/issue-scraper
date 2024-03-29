package net.unicon.issueScraper;

import java.io.Serializable;

import org.dom4j.Document;

public interface IIssue extends Serializable {

    public String getId();
    public void setId(String s);
    public String getProject();
    public void setProject(String s);
    public String getSummary();
    public void setSummary(String s);
    public String getIssueUrl();
    public void setIssueUrl(String s);
    public String getDescription();
    public void setDescription(String s);
    public String getEnvironment();
    public void setEnvironment(String s);
    public String getType();
    public void setType(String s);
    public String getPriority();
    public void setPriority(String s);
    public String getStatus();
    public void setStatus(String s);
    public String getResolution();
    public void setResolution(String s);
    public String getAssignee();
    public void setAssignee(String s);
    public String getReporter();
    public void setReporter(String s);
    public void setClosed(boolean b);
    public boolean isClosed();
    public String getIssueScaperUrl(boolean flag);
    public void setIssueScraperUrls(String s, String s1);
    public Document toDocument();
    public IIssue fromDocument(Document doc);
}

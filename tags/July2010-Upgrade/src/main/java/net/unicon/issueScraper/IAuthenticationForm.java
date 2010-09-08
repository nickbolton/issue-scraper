package net.unicon.issueScraper;

import java.util.Map;

public interface IAuthenticationForm {
    public HttpMethodType getMethod();
    public void setMethod(HttpMethodType method);
    public String getUrl();
    public void setUrl(String s);
    public Map<String, String> getParameters();
    public void setParameters(Map<String, String> m);
}

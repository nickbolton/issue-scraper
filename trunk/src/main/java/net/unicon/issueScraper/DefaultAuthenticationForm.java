package net.unicon.issueScraper;

import java.util.HashMap;
import java.util.Map;

public class DefaultAuthenticationForm implements IAuthenticationForm {
    
    private String url;
    private HttpMethodType method;
    private Map<String, String> parameters = new HashMap<String, String>();

    public HttpMethodType getMethod() {
        return method;
    }

    public void setMethod(HttpMethodType method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setParameters(Map<String, String> m) {
        this.parameters = m;
    }

    public void setUrl(String s) {
        this.url = s;
    }

}

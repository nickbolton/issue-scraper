package net.unicon.issueScraper.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ToggleIgnoreExternalSitesController implements Controller {
    
    private static final String IGNORE_EXTERNAL_SITES_KEY = "ignoreExternalSites";
    
    private Map<String, String> configuration = new HashMap<String, String>();
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        boolean ignoreExternalSites = true;
        if (configuration.get(IGNORE_EXTERNAL_SITES_KEY) != null) {
            ignoreExternalSites = !new Boolean(configuration.get(IGNORE_EXTERNAL_SITES_KEY));
        }
        
        String newValue = Boolean.toString(ignoreExternalSites).toString();
        
        configuration.put(IGNORE_EXTERNAL_SITES_KEY, newValue);
        
        Map<String, String> model = new HashMap<String, String>();
        model.put("ignoreExternalSites", newValue);
        return new ModelAndView("toggle_ignore_external_sites", "model", model);
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
}

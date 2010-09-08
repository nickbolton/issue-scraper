package net.unicon.issueScraper.controller;

import net.unicon.issueScraper.cache.CacheUtility;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.mvc.Controller;

public abstract class AbstractCacheController implements Controller, InitializingBean {

    private CacheUtility cacheUtility;

    public CacheUtility getCacheUtility() {
        return cacheUtility;
    }

    public void setCacheUtility(CacheUtility cacheUtility) {
        this.cacheUtility = cacheUtility;
    }

    public void afterPropertiesSet() throws Exception {
        if (cacheUtility == null) {
            throw new RuntimeException("cacheUtility was not set.");
        }
    }
    
}

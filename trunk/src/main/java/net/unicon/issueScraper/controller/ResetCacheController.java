package net.unicon.issueScraper.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.unicon.issueScraper.cache.CacheUtility;
import net.unicon.issueScraper.cache.CacheWrapper;

import org.springframework.web.servlet.ModelAndView;

public class ResetCacheController extends AbstractCacheController {
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CacheUtility cacheUtility = getCacheUtility();
        CacheWrapper cacheWrapper = cacheUtility.getCacheWrapper();
        cacheWrapper.removeAll();
        return new ModelAndView("WEB-INF/jsp/reset_cache.jsp");
    }

}

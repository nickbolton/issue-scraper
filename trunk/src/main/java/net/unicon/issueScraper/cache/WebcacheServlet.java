package net.unicon.issueScraper.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebcacheServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private CacheManager manager = new CacheManager();
    private Cache cache;
    private HttpClient httpClient;
    private Log log = LogFactory.getLog(getClass());
    private File persistentFileCacheDir;
    private String fileSeparator;
  

    public WebcacheServlet() {
        cache = manager.getCache("xmlCache");
        httpClient = new HttpClient();
        fileSeparator = System.getProperty("file.separator");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        String persistentFileCachePath = config.getInitParameter("persistentFileCachePath"); 
        if (persistentFileCachePath == null) {
            if (log.isWarnEnabled()) {
                log.warn("persistentFileCachePath init-param is not set in web descriptor.");
            }
        }
        
        persistentFileCacheDir = new File(persistentFileCachePath);
        if (!persistentFileCacheDir.exists()) {
            throw new ServletException("Directory '" + persistentFileCachePath + "' does not exist.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        
        String resetDirective = req.getParameter("resetCache");
        if (resetDirective != null && new Boolean(resetDirective)) {
            cache.removeAll();
            resp.getWriter().println("<html><body><p>Removed all cache entries.</p></body></html>");
            return;
        }
        
        String url = req.getParameter("url");
        Element element = cache.get(url);
        if (element == null) {
            if (log.isDebugEnabled()) {
                log.debug("cache miss for url: " + url);
            }
            String content = fetchContent(url);
            if (content != null) {
                element = new Element(url, content);
                cache.put(element);
                storePreviousVersion(url, content);
            } else {
                content = getPreviousVersion(url);
                if (content != null) {
                    element = new Element(url, content);
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("cache hit for url: " + url);
            }
        }
        
        if (element != null) {
            resp.getWriter().print(element.getValue());
        } else {
            resp.setStatus(404);
        }
    }
    
    private void storePreviousVersion(String url, String content) {
        PrintWriter writer = null;
        try {
            File filePath = getFilePath(url);
            
            if (log.isDebugEnabled()) {
                log.debug("Storing previous version for url="+url+" to '"+filePath.getAbsolutePath()+"'.");
            }
            
            writer = new PrintWriter(new FileWriter(filePath));
            writer.println(content);
        } catch (IOException e) {
            log.error("Failed storing the previous version of url: " + url, e);
        } finally {
            writer.close();
        }
    }
    
    private File getFilePath(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            log.error("Malformed url: " + urlStr, e);
            return null;
        }
        
        StringBuffer relativePath = new StringBuffer(url.getHost()).append(fileSeparator);
        relativePath.append(url.getPort()).append(url.getPath());
        File f = new File(persistentFileCacheDir, relativePath.toString());
        if (log.isDebugEnabled()) {
            log.debug("Persistent file path for url="+urlStr + " is '" + f.getAbsolutePath() + "'.");
        }
        return f;
    }

    private String fetchContent(String url) throws HttpException, IOException {
        HttpMethod method = null;
        
        if (log.isDebugEnabled()) {
            log.debug("Fetching content for url: " + url);
        }
        
        //execute the method
        String responseBody = null;
        try{
            method = new GetMethod(url);
            method.setFollowRedirects(true);
            httpClient.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
        } catch (HttpException he) {
            log.error("Http error connecting to '" + url + "'", he);
            throw he;
        } catch (IOException ioe){
            log.error("Unable to connect to '" + url + "'", ioe);
            throw ioe;
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Content for url: " + url + " is:\n"+responseBody);
        }
        return responseBody;
    }
    
    private String getPreviousVersion(String url) {
        File filePath = null;
        try {
            filePath = getFilePath(url);
            if (log.isDebugEnabled()) {
                log.debug("Getting previous version for url="+url);
            }
            StringBuffer content = new StringBuffer();
            FileReader reader = new FileReader(filePath);
            char[] buf = new char[1024];
            int numRead;
            
            while ((numRead = reader.read(buf, 0, 1024))>=0) {
                content.append(new String(buf, 0, numRead));
            }
            if (log.isDebugEnabled()) {
                log.debug("Previous version content for url="+url+" is:\n"+content);
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("Previous version not found for url="+url, e);
            }
        } catch (IOException e) {
            log.error("Failed reading file="+(filePath != null ? filePath.getAbsolutePath() : "NULL"), e);
        }
        
        return null;
    }
}

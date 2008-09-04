package net.unicon.issueScraper.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.InitializingBean;

public class CacheUtility implements InitializingBean {
    
    private HttpClient httpClient;
    private CacheWrapper cacheWrapper;
    private String persistentFileCachePath;
    private String fileSeparator;
    private Log log = LogFactory.getLog(getClass());
    
    public CacheUtility() {
        httpClient = new HttpClient();
        fileSeparator = System.getProperty("file.separator");
    }
    
    public void storePreviousVersion(String key, Document content) throws IOException {
        XMLWriter writer = null;
        try {
            key = key.toUpperCase();
            File file = getFile(key);
            
            if (log.isDebugEnabled()) {
                log.debug("Storing previous version for key="+key+" to '"+file.getAbsolutePath()+"'.");
            }
            
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(new FileWriter(file), format );
            writer.write( content );
        } catch (IOException e) {
            log.error("Failed storing the previous version of key: " + key, e);
        } finally {
            if (writer != null) { writer.close();}
        }
    }
    
    protected File getFilePath(String key) {
        if (!key.startsWith("http")) {
            return new File(persistentFileCachePath, key);
        }
        URL url = null;
        try {
            url = new URL(key);
        } catch (MalformedURLException e) {
            log.error("Malformed url: " + key, e);
            return null;
        }
        
        StringBuffer relativePath = new StringBuffer(url.getHost()).append(fileSeparator);
        if (url.getPort() > 0) {
            relativePath.append(url.getPort());
        }
        relativePath.append(url.getPath());
        File f = new File(persistentFileCachePath, relativePath.toString());
        if (log.isDebugEnabled()) {
            log.debug("Persistent file path for key="+key + " is '" + f.getAbsolutePath() + "'.");
        }
        return f;
    }
    
    public File getFile(String key) {
        File dir = getFilePath(key);
        dir.mkdirs();
        return new File(dir, "cache");
    }

    public Document getPreviousVersion(String key) throws DocumentException {
        File file = null;
        try {
            key = key.toUpperCase();
            file = getFile(key);
            if (!file.exists()) {
                if (log.isDebugEnabled()) {
                    log.debug("No previous version exists for key="+key);
                }
                return null;
            }
            if (log.isDebugEnabled()) {
                log.debug("Getting previous version for key="+key);
            }
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            if (log.isDebugEnabled()) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                StringWriter sw = new StringWriter();
                XMLWriter writer = new XMLWriter( sw, format );
                writer.write( document );
                log.debug("Previous version content for key="+key+" is:\n"+sw.toString());
            }
            return document;
        } catch (FileNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("Previous version not found for key="+key, e);
            }
        } catch (IOException e) {
            log.error("Failed reading file="+(file != null ? file.getAbsolutePath() : "NULL"), e);
        }
        
        return null;
    }


    public void afterPropertiesSet() throws Exception {
        if (persistentFileCachePath != null && !new File(persistentFileCachePath).exists()) {
            throw new ServletException("Directory '" + persistentFileCachePath + "' does not exist.");
        }
        if (cacheWrapper == null) {
            throw new RuntimeException("cacheWrapper not set");
        }
    }
    
    public String getPersistentFileCachePath() {
        return persistentFileCachePath;
    }

    public void setPersistentFileCachePath(String persistentFileCachePath) {
        if (persistentFileCachePath == null) {
            if (log.isWarnEnabled()) {
                log.warn("persistentFileCachePath init-param is not set in web descriptor.");
            }
        } else {
            this.persistentFileCachePath = persistentFileCachePath;
        }
    }

    public CacheWrapper getCacheWrapper() {
        return cacheWrapper;
    }

    public void setCacheWrapper(CacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

}

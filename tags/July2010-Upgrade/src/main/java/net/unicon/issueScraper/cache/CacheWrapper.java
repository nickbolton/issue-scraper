package net.unicon.issueScraper.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.Status;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.event.RegisteredEventListeners;
import net.sf.ehcache.exceptionhandler.CacheExceptionHandler;
import net.sf.ehcache.extension.CacheExtension;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;


public class CacheWrapper implements Ehcache, InitializingBean {

    private Cache cache;
    private CacheConfiguration cacheConfiguration;
    private String cacheName;
    private String evictionPolicy;
    private Log log = LogFactory.getLog(getClass());
    
    public String getCacheName() {
        return cacheName;
    }
    
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
    
    public void bootstrap() {
        cache.bootstrap();
    }
    
    public final long calculateInMemorySize() throws IllegalStateException,
        CacheException {
        return cache.calculateInMemorySize();
    }
    
    public final Object clone() throws CloneNotSupportedException {
        return cache.clone();
    }
    
    public void dispose() throws IllegalStateException {
        cache.dispose();
    }
    
    public boolean equals(Object object) {
        return cache.equals(object);
    }
    
    public void evictExpiredElements() {
        cache.evictExpiredElements();
    }
    
    public final void flush() throws IllegalStateException, CacheException {
        cache.flush();
    }
    
    public final Element get(Object key) throws IllegalStateException,
        CacheException {
        return cache.get(key);
    }
    
    public final Element get(Serializable key) throws IllegalStateException,
        CacheException {
        return cache.get(key);
    }
    
    public BootstrapCacheLoader getBootstrapCacheLoader() {
        return cache.getBootstrapCacheLoader();
    }
    
    public final RegisteredEventListeners getCacheEventNotificationService() {
        return cache.getCacheEventNotificationService();
    }
    
    public final CacheManager getCacheManager() {
        return cache.getCacheManager();
    }
    
    public final long getDiskExpiryThreadIntervalSeconds() {
        return cache.getDiskExpiryThreadIntervalSeconds();
    }
    
    public final int getDiskStoreHitCount() {
        return cache.getDiskStoreHitCount();
    }
    
    public final int getDiskStoreSize() throws IllegalStateException {
        return cache.getDiskStoreSize();
    }
    
    public final String getGuid() {
        return cache.getGuid();
    }
    
    public final int getHitCount() {
        return cache.getHitCount();
    }
    
    public final List getKeys() throws IllegalStateException, CacheException {
        return cache.getKeys();
    }
    
    public final List getKeysNoDuplicateCheck() throws IllegalStateException {
        return cache.getKeysNoDuplicateCheck();
    }
    
    public final List getKeysWithExpiryCheck() throws IllegalStateException,
        CacheException {
        return cache.getKeysWithExpiryCheck();
    }
    
    public final int getMaxElementsInMemory() {
        return cache.getMaxElementsInMemory();
    }
    
    public int getMaxElementsOnDisk() {
        return cache.getMaxElementsOnDisk();
    }
    
    public final MemoryStoreEvictionPolicy getMemoryStoreEvictionPolicy() {
        return cache.getMemoryStoreEvictionPolicy();
    }
    
    public final int getMemoryStoreHitCount() {
        return cache.getMemoryStoreHitCount();
    }
    
    public final long getMemoryStoreSize() throws IllegalStateException {
        return cache.getMemoryStoreSize();
    }
    
    public final int getMissCountExpired() {
        return cache.getMissCountExpired();
    }
    
    public final int getMissCountNotFound() {
        return cache.getMissCountNotFound();
    }
    
    public final String getName() {
        return cache.getName();
    }
    
    public final Element getQuiet(Object key) throws IllegalStateException,
        CacheException {
        return cache.getQuiet(key);
    }
    
    public final Element getQuiet(Serializable key)
        throws IllegalStateException, CacheException {
        return cache.getQuiet(key);
    }
    
    public final int getSize() throws IllegalStateException, CacheException {
        return cache.getSize();
    }
    
    public Statistics getStatistics() throws IllegalStateException {
        return cache.getStatistics();
    }
    
    public int getStatisticsAccuracy() {
        return cache.getStatisticsAccuracy();
    }
    
    public final Status getStatus() {
        return cache.getStatus();
    }
    
    public final long getTimeToIdleSeconds() {
        return cache.getTimeToIdleSeconds();
    }
    
    public final long getTimeToLiveSeconds() {
        return cache.getTimeToLiveSeconds();
    }
    
    public int hashCode() {
        return cache.hashCode();
    }
    
    public void initialise() {
        cache.initialise();
    }
    
    public final boolean isDiskPersistent() {
        return cache.isDiskPersistent();
    }
    
    public final boolean isElementInMemory(Object key) {
        return cache.isElementInMemory(key);
    }
    
    public final boolean isElementInMemory(Serializable key) {
        return cache.isElementInMemory(key);
    }
    
    public final boolean isElementOnDisk(Object key) {
        return cache.isElementOnDisk(key);
    }
    
    public final boolean isElementOnDisk(Serializable key) {
        return cache.isElementOnDisk(key);
    }
    
    public final boolean isEternal() {
        return cache.isEternal();
    }
    
    public final boolean isExpired(Element element)
        throws IllegalStateException, NullPointerException {
        return cache.isExpired(element);
    }
    
    public boolean isKeyInCache(Object key) {
        return cache.isKeyInCache(key);
    }
    
    public final boolean isOverflowToDisk() {
        return cache.isOverflowToDisk();
    }
    
    public boolean isValueInCache(Object arg0) {
        return cache.isValueInCache(arg0);
    }
    
    public final void put(Element element, boolean doNotNotifyCacheReplicators)
        throws IllegalArgumentException, IllegalStateException, CacheException {
        cache.put(element, doNotNotifyCacheReplicators);
    }
    
    public final void put(Element element) throws IllegalArgumentException,
        IllegalStateException, CacheException {
        cache.put(element);
    }
    
    public final void putQuiet(Element element)
        throws IllegalArgumentException, IllegalStateException, CacheException {
        cache.putQuiet(element);
    }
    
    public final boolean remove(Object key, boolean doNotNotifyCacheReplicators)
        throws IllegalStateException {
        return cache.remove(key, doNotNotifyCacheReplicators);
    }
    
    public final boolean remove(Object key) throws IllegalStateException {
        return cache.remove(key);
    }
    
    public final boolean remove(Serializable key,
        boolean doNotNotifyCacheReplicators) throws IllegalStateException {
        return cache.remove(key, doNotNotifyCacheReplicators);
    }
    
    public final boolean remove(Serializable key) throws IllegalStateException {
        return cache.remove(key);
    }
    
    public void removeAll() throws IllegalStateException, CacheException {
        cache.removeAll();
    }
    
    public void removeAll(boolean doNotNotifyCacheReplicators)
        throws IllegalStateException, CacheException {
        cache.removeAll(doNotNotifyCacheReplicators);
    }
    
    public final boolean removeQuiet(Object key) throws IllegalStateException {
        return cache.removeQuiet(key);
    }
    
    public final boolean removeQuiet(Serializable key)
        throws IllegalStateException {
        return cache.removeQuiet(key);
    }
    
    public void setBootstrapCacheLoader(
        BootstrapCacheLoader bootstrapCacheLoader) throws CacheException {
        cache.setBootstrapCacheLoader(bootstrapCacheLoader);
    }
    
    public void setCacheManager(CacheManager cacheManager) {
        cache.setCacheManager(cacheManager);
    }
    
    public void setDiskStorePath(String diskStorePath) throws CacheException {
        cache.setDiskStorePath(diskStorePath);
    }
    
    public final void setName(String name) throws IllegalArgumentException {
        cache.setName(name);
    }
    
    public void setStatisticsAccuracy(int statisticsAccuracy) {
        cache.setStatisticsAccuracy(statisticsAccuracy);
    }
    
    public final String toString() {
        return cache.toString();
    }

    public void afterPropertiesSet() throws Exception {
        if (cacheName == null) {
            throw new RuntimeException("cacheName property not set.");
        }
        if (cacheConfiguration == null) {
            throw new RuntimeException("cacheConfiguration property not set.");
        }
        if (evictionPolicy == null) {
            if (log.isInfoEnabled()) {
                log.info("memoryStoreEvictionPolicy not set. Setting to LRU");
            }
            evictionPolicy = "LRU";
        }
        Configuration configuration = new Configuration();
        cacheConfiguration.setMemoryStoreEvictionPolicy(evictionPolicy);
        configuration.addCache(cacheConfiguration);
        configuration.setDefaultCacheConfiguration(cacheConfiguration);
        CacheManager cacheManager = new CacheManager(configuration);
        cache = cacheManager.getCache("default");
        
        if (cache == null) {
            throw new RuntimeException("Failed to get cache '" + cacheName + "'.");
        }
    }

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    public void setCacheConfiguration(CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }
    
    public void setEvictionPolicy(String s) {
        evictionPolicy = s;
    }

    public void clearStatistics() {
        cache.clearStatistics();
    }

    public Map getAllWithLoader(Collection arg0, Object arg1)
        throws CacheException {
        return cache.getAllWithLoader(arg0, arg1);
    }

    public float getAverageGetTime() {
        return cache.getAverageGetTime();
    }

    public CacheExceptionHandler getCacheExceptionHandler() {
        return cache.getCacheExceptionHandler();
    }

    public CacheLoader getCacheLoader() {
        return cache.getCacheLoader();
    }

    public Element getWithLoader(Object arg0, CacheLoader arg1, Object arg2)
        throws CacheException {
        return cache.getWithLoader(arg0, arg1, arg2);
    }

    public boolean isDisabled() {
        return cache.isDisabled();
    }

    public void load(Object key) throws CacheException {
        cache.load(key);
    }

    public void loadAll(Collection keys, Object argument) throws CacheException {
        cache.loadAll(keys, argument);
    }

    public void registerCacheExtension(CacheExtension cacheExtension) {
        cache.registerCacheExtension(cacheExtension);
    }

    public void setCacheExceptionHandler(
        CacheExceptionHandler cacheExceptionHandler) {
        cache.setCacheExceptionHandler(cacheExceptionHandler);
    }

    public void setCacheLoader(CacheLoader cacheLoader) {
        cache.setCacheLoader(cacheLoader);
    }

    public void setDisabled(boolean disabled) {
        cache.setDisabled(disabled);
    }

    public void unregisterCacheExtension(CacheExtension cacheExtension) {
        cache.unregisterCacheExtension(cacheExtension);
    }
    
}

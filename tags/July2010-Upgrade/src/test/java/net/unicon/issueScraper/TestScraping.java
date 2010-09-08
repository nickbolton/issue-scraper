package net.unicon.issueScraper;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class TestScraping extends TestCase {
    
    private BeanFactory beanFactory;
    private IIssueTrackerManager manager;

    protected void setUp() throws Exception {
        super.setUp();
        
		XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("issue-scraper-test-beans.xml"));
		StaticApplicationContext staticApplicationContext = new StaticApplicationContext();
		for (int i=0; i<xmlBeanFactory.getBeanDefinitionNames().length; i++) {
		    String beanName = xmlBeanFactory.getBeanDefinitionNames()[i];
		    BeanDefinition beanDefinition = xmlBeanFactory.getBeanDefinition(beanName);
    		staticApplicationContext.registerBeanDefinition(beanName, beanDefinition);
		}
		beanFactory = staticApplicationContext;
        manager = (IIssueTrackerManager)beanFactory.getBean("issueTrackerManager", IIssueTrackerManager.class);
    }
    
    public void testJiraUrlQuery() throws Throwable {
        assertNotNull(manager.getIssue("http://www.ja-sig.org/issues/browse/UP-1", true));
    }

    public void testJiraIDQuery() throws Throwable {
        assertNotNull(manager.getIssue("uPortal", "UP-1", true));
    }

    public void testJiraOpenQuery() throws Throwable {
        List<IIssue> issues = manager.getIssuesFromNamedQuery("uPortal", "open");
        assertNotNull(issues);
        assertTrue(issues.size() > 0);
        System.out.println("uPortal open query returned " + issues.size() + " results");
    }

    public void testJiraRecentQuery() throws Throwable {
        List<IIssue> issues = manager.getIssuesFromNamedQuery("uPortal", "recent");
        assertNotNull(issues);
        assertTrue(issues.size() > 0);
        System.out.println("uPortal recent query returned " + issues.size() + " results");
    }

}

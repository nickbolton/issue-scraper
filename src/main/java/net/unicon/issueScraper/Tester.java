package net.unicon.issueScraper;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class Tester extends TestCase {

    public void testIssueScraper() throws Throwable {
        
        ClassPathResource beanFactoryResource = new ClassPathResource("/net/unicon/issueScraper/test-issue-scraper-beans.xml");
        XmlBeanFactory beanFactory = new XmlBeanFactory(beanFactoryResource);
        IIssueTrackerManager itm = (IIssueTrackerManager)beanFactory.getBean("issueTrackerManager", IIssueTrackerManager.class);
        
        IIssue issue = itm.getIssue("http://www.ja-sig.org/issues/browse/GAP-34");
        System.out.println("ZZZ issue: " + issue);
    }

}

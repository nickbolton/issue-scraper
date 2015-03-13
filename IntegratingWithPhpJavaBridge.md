This page covers integrating with the php-java bridge.

### Deploy IssueScraper and Dependencies ###
You need to make all the jar files available to php when invoking the java\_require method. PHP looks for these in /usr/share/java by default. Untar the issue-scraper-dependencies.tar tar file in /usr/share/java or in the appropriate directory.  Then copy the issueScraper.jar library in the same directory.

### Configure PHP ###
This assumes you've installed the java php bridge (see http://php-java-bridge.sourceforge.net/pjb/).
Replace the paths below with their appropriate values.

php.ini:
```
extension = java.so
[java]

java.java_home  = /usr/local/j2sdk
java.java       = /usr/local/j2sdk/bin/java
java.classpath  = /usr/local/php/extensions/JavaBridge.jar:/usr/share/java/issueScraper
java.log_level  = 4
java.log_file   = /home/unicon/drupal/issueScraper/php-java-bridge.log
```

### Usage Example ###
```
function retrieveIssue($project, $issueId) {
    $classPathResource = new Java("org.springframework.core.io.ClassPathResource", "issue-scraper-beans.xml");
    $beanFactory = new Java("org.springframework.beans.factory.xml.XmlBeanFactory", $classPathResource); 
    $issueTrackerManagerClass = new JavaClass("net.unicon.issueScraper.IIssueTrackerManager");
    $issueTrackerManager = $beanFactory->getBean("issueTrackerManager", $issueTrackerManagerClass);
    try {
        return $issueTrackerManager->getIssue($project, $issueId);
    } catch (JavaException $e) {
        return NULL;
    }
}
```

# How to setup development environment #

Perform the following steps:

  * Install [Java JDK 6](http://java.sun.com/javase/downloads/)

  * Install the latest version of [Maven2](http://maven.apache.org/download.html)

  * Add SCM properties to your local maven settings ({USER\_HOME}/.m2/settings.xml)

```
<settings>

  <profiles>
    <profile>
      <id>spelstegen</id>
      <properties>
        <spelstegen-scm-username>{YOUR_USERNAME}</spelstegen-scm-username>
        <spelstegen-scm-password>{YOUR_PASSWORD}</spelstegen-scm-password>
      </properties>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>spelstegen</activeProfile>
  </activeProfiles>

</settings>
```

Replace {YOUR\_USERNAME} and {YOUR\_PASSWORD} with the ones you see when you are logged in at http://code.google.com/p/spelstegen/source/checkout

  * Optionally install a [Subversion client](http://subversion.tigris.org/getting.html) to be able to run mvn:scm from command line

  * Optionally install [TortoiseSVN](http://tortoisesvn.net/downloads). TortoiseSVN integrates subversion very nicely with the Windows Explorer.

  * Install [Eclipse IDE for Java EE Developers (3.5 - Galileo)](http://www.eclipse.org/downloads/)

  * Configure Eclipse to start from installed JDK. Add the following lines to eclipse.ini, i.e:
```
256m
-vm
C:\Program Files\Java\jdk1.6.0_12\bin\javaw.exe
-vmargs
-Dosgi.requiredJavaVersion=1.5
-Xms40m
-Xmx512m
```

  * Set text file encoding in eclipse to UTF-8 (Under Windows->Preferences->General->Workspace)

  * Install Subclipse. Update site: http://subclipse.tigris.org/update_1.4.x/

  * Install Subversive SVN Connectors. Update site: http://community.polarion.com/projects/subversive/download/eclipse/2.0/galileo-site/

  * Install M2Eclipse (Integrates maven with Eclipse). Update site: http://m2eclipse.sonatype.org/update/
> Select the following components to install:
    * Maven Integration	(All components)
    * Maven Optional Components
      * Maven issue tracking configurator for Mylyn 3.x (Optional)
      * Maven SCM handler for Subclipse (Optional)
      * Maven SCM handler for Team/CVS (Optional)
      * Maven SCM Integration (Optional)

  * Install Subversive Integrations (Select only integration for M2Eclipse). Update site: http://community.polarion.com/projects/subversive/download/integrations/galileo-site/

  * Install Google Plugin for Eclipse. Update site: http://dl.google.com/eclipse/plugin/3.5

  * Use your favourite subversion client tool (like TortoiseSVN) to check out the source code from the repository:
```
https://spelstegen.googlecode.com/svn/trunk/
```

  * Opend a command prompt and navigate to the directory where you checked out the code. Then enter:
```
mvn clean install eclipse:eclipse gwt:eclipse
```

> This will build the code and generate an eclipse project files as well as a launch configuration.

  * If you want to run the application with MySQL (deprecated) perform the following three steps:
    1. Install [MySQL](http://dev.mysql.com/downloads/mysql/5.0.html#downloads)
      * Create a new schema: _spelstegen_
      * Create a user named _stegusr_ with password _stegpw_ and add all permissions to this user for the _spelstegen_ schema.
    1. To create the tables needed for the application in mysql execute one of the following scripts located in the db folder from the command prompt:
      * mysql -u stegusr -p < create\_db.sql
      * mysql -u stegusr -p < create\_db\_with\_test\_data.sql  (to add som sample test data)
    1. Download [MySQL Connector/J 5.1](http://dev.mysql.com/downloads/connector/j/5.1.html) and add mysql-connector-java-5.1.X-bin.jar to jre/lib/etc folder of your JDK.

  * Open Eclipse and import the project. Your eclipse workspace should look something like this:

> ![http://spelstegen.googlecode.com/svn/wiki/eclipse_screenshot.png](http://spelstegen.googlecode.com/svn/wiki/eclipse_screenshot.png)

  * To run the project - right-click on the **com.appspot.spelstegen.Spelstegen.launch** file and select **Run as->com.appspot.spelstegen.Spelstegen**

# BTS

Web-based bug tracker

## Feature Summary

* Advanced search capabilities. Searches can be saved for later.
* Issues
  * Can be commented on
  * Can contain screenshots & attachments
  * Store a history of what has happened to the issue
* User and group management
* Customizable Email integration
* PDF reports
* Comprehensive logging
* Built-in database backups
* Auditing
* System monitoring

## Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Gradle
* Tomcat 8

### Installing

1. Clone project
2. Build with Gradle

## Deployment (WIP)

1. Build war file with Gradle
2. Copy war file to tomcat webapps directory
3. Place DB driver jar file for H2 or Postgres in tomcat/lib (both should be in the war)
4. Set up tomcat's server.xml (there is a server-sample.xml in project source code)
5. Update DB connection info in bts.properties once war file is expanded
6. To add Trigram extension run: CREATE EXTENSION pg_trgm;

## Built With (partial listing)

* [EOI](https://github.com/ehicks05/eoi) - Object relational mapper
* [Reflections](https://github.com/ronmamo/reflections) - Annotation scanning. Used to build URL 
  to method handler mappings (AKA Routes)
* [diffpatch](https://github.com/sksamuel/google-diff-match-patch) - Text diffing. Used to 
  create fancy-looking emails when the text of an issue or a comment is updated.
* [Cron4J](http://www.sauronsoftware.it/projects/cron4j/index.php) - Job Scheduler
* [Apache Commons Email](https://commons.apache.org/proper/commons-email/) - API for sending mail
* [Apache PDFBox](https://pdfbox.apache.org/) - PDF creation
* [Bulma](https://bulma.io/) - CSS framework

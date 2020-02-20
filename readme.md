# BTS

Web-based bug tracker

## Feature Summary

* Advanced search capabilities. 
  * Searches can be saved for later.
  * Saved searches can be added to your dashboard.
* Issues
  * Can be commented on
  * Can contain screenshots & attachments
  * Store a history of what has happened to the issue
* User and group management
* Customizable Email integration
  * Flexible email subscription management
* PDF reports
* Comprehensive logging
* Built-in database backups
* Auditing

##2020 Todo:
- security, lock down access
- do we need a modify subscription screen?
- why do temporal indexes like 'created_on' in Issue.kt not work?
- Consider react

## Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Java

### Installing

1. Clone project
2. Build with Gradle

## Deployment (WIP)

1. Edit DB connection info in application.properties
2. Build war file with Gradle
3. run with java -jar

## Built With (partial listing)

* [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
* [diffpatch](https://github.com/sksamuel/google-diff-match-patch) - Text diffing. Used to 
  create fancy-looking emails when the text of an issue or a comment is updated.
* [Cron4J](http://www.sauronsoftware.it/projects/cron4j/index.php) - Job Scheduler
* [Apache PDFBox](https://pdfbox.apache.org/) - PDF creation
* [Bulma](https://bulma.io/) - CSS framework

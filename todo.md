## TODO

- [ ] ~~Review correctness of Shiro~~
- [x] Add chat functionality (for fun)
- [x] Consider spring boot or dropwizard
- [x] all dates to localdatetimes
- [ ] Look for reusable chunks of 'view' and create .tag files for them.
- [ ] Look into adding npm + bulma + maybe even vuejs
- [ ] Consider porting to Kotlin
- [ ] Consider storing images in filesystem (or S3)
    - A huge amount of base64-encoded text gets embedded in the html every time we display an img from the db.
    - pros: performance
    - cons: complicates backups

##2020 Rewrite:

- Replace bespoke backend 'framework' with Spring Boot.
    - replace custom routing logic and annotations with spring mvc.
        - update all controllers to use the spring annotations.
    - replace my homegrown orm/data access layer with spring data jpa.
        - rework all beans with jpa annotations and proper relationship mappings.
        - make use of repositories for simple queries.
        - use criteria api for complex queries.
    - convert beans to kotlin data classes to reduce noisy boilerplate.
    - replace custom auditing logic with hibernate envers.
    - replace shiro security with spring security.
    - change how images are sent to the browser:
        - from being sent as base64 text embedded in the html.
        - to using Spring's ResponseEntity to send the image as a separate request.
    - rewrite email html generation from manual java strings (what a mess), to making use of a templateEngine. 
    
##2020 Todo:
- security, lock down access
- do we need a modify subscription screen?
- can we reasonably recreate the 'response time' metric in the footer?
    - it should clock the time from request hitting the server, to leaving the server
- why do temporal indexes like 'created_on' in Issue.kt not work?
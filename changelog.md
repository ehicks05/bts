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
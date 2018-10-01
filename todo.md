# TODO

* Review correctness of Shiro
* Add chat functionality (for fun)
* Look for reusable chunks of 'view' and create .tag files for them.
* Look into adding npm + bulma + maybe even vuejs
* Consider spring boot or dropwizard
* Consider porting to Kotlin
* Consider storing images in filesystem (or S3)
    * A huge amount of base64-encoded text gets embedded in the html every
      time we display an img from the db.
    * pros: performance
    * cons: complicates backups
* all dates to localdatetimes
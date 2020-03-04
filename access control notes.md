who should be able to do what?
Admins can do anything. otherwise:

* issue:
  * create: any user
  * read: group membership
  * update (direct fields): creator
  * delete: creator (maybe no one)
  * special cases:
    * attachments:
      * create: any user (#see note)
      * read: any user
      * remove: creator
    * comments:
      * create: any user
      * read: any user (consider comments with custom visibility)
      * update: author
      * remove: author
    * watchers:
      * create: todo
      * read: anyone
      * add: anyone can add themselves
      * remove: anyone can remove themselves
* avatars:
  * create (upload): todo
  * read: you can view your own avatar, or public avatars
  * change selection: anyone
  * remove: your own 
* user:
  * read profile: group access
  * update (only some fields): themselves
* role:
  * admin only
* group:
  * read: group membership
* issue event:
  * read: same as issue access (group membership)
* btsSystem:
  * some fields are display to every logged in user (theme)
  * some fields are admin-only.
  * maybe this bean should be split up.
* DBFile:
  * todo
             
global read-only access for:
projects, issue types, statuses, severities      
      
users can only CRUD objects they own:      
IssueForm, Subscription

note: this assumes we already accounted for issue level security todo confirm this
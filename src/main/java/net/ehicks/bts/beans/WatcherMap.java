//package net.ehicks.bts.beans;
//
//import net.ehicks.eoi.EOI;
//import net.ehicks.eoi.Index;
//import net.ehicks.eoi.Indexes;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "watcher_maps")
//@Indexes({
//        @Index(sql = "CREATE INDEX IDX_WATCHER_MAP_ISSUE_ID ON WATCHER_MAPS(issue_id);")
//})
//public class WatcherMap implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false, unique = false)
//    private Long userId;
//
//    @Column(nullable = false, unique = false)
//    private Long issueId;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof WatcherMap)) return false;
//        WatcherMap that = (WatcherMap) obj;
//        return this.id.equals(that.getId());
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return 17 * 37 * id.intValue();
//    }
//
//    public String toString()
//    {
//        return this.getClass().getSimpleName() + ":" + id.toString();
//    }
//
//    public static List<WatcherMap> getAll()
//    {
//        return EOI.executeQuery("select * from watcher_maps");
//    }
//
//    public static List<WatcherMap> getByUserId(Long userId)
//    {
//        return EOI.executeQuery("select * from watcher_maps where user_id=?", Arrays.asList(userId));
//    }
//
//    public static List<WatcherMap> getByIssueId(Long issueId)
//    {
//        return EOI.executeQuery("select * from watcher_maps where issue_id=?", Arrays.asList(issueId));
//    }
//
//    public static WatcherMap getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from watcher_maps where id=?", Arrays.asList(id));
//    }
//
//    public static List<User> getWatchersForIssue(Long issueId)
//    {
//        List<User> watchers = new ArrayList<>();
//        List<WatcherMap> watcherMaps = WatcherMap.getByIssueId(issueId);
//        for (WatcherMap watcherMap : watcherMaps)
//            watchers.add(User.getByUserId(watcherMap.getUserId()));
//        return watchers;
//    }
//
//    public User getWatcher()
//    {
//        return User.getByUserId(userId);
//    }
//
//    // -------- Getters / Setters ----------
//
//    public Long getId()
//    {
//        return id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public Long getUserId()
//    {
//        return userId;
//    }
//
//    public void setUserId(Long userId)
//    {
//        this.userId = userId;
//    }
//
//    public Long getIssueId()
//    {
//        return issueId;
//    }
//
//    public void setIssueId(Long issueId)
//    {
//        this.issueId = issueId;
//    }
//}

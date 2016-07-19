package com.hicks.beans;

import com.sun.org.apache.bcel.internal.generic.NEW;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "watcher_maps")
public class WatcherMap implements Serializable
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName="ROLE_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;

    @Column(name = "issue_id", nullable = false, unique = false)
    private Long issueId;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof WatcherMap)) return false;
        WatcherMap that = (WatcherMap) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ":" + id.toString();
    }

    public static List<WatcherMap> getAll()
    {
        return EOI.executeQuery("select * from watcher_maps");
    }

    public static List<WatcherMap> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from watcher_maps where user_id=?", Arrays.asList(userId));
    }

    public static List<WatcherMap> getByIssueId(Long issueId)
    {
        return EOI.executeQuery("select * from watcher_maps where issue_id=?", Arrays.asList(issueId));
    }

    public static WatcherMap getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from watcher_maps where id=?", Arrays.asList(id));
    }

    public static List<User> getWatchersForIssue(Long issueId)
    {
        List<User> watchers = new ArrayList<>();
        List<WatcherMap> watcherMaps = WatcherMap.getByIssueId(issueId);
        for (WatcherMap watcherMap : watcherMaps)
            watchers.add(User.getByUserId(watcherMap.getUserId()));
        return watchers;
    }

    // -------- Getters / Setters ----------

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getIssueId()
    {
        return issueId;
    }

    public void setIssueId(Long issueId)
    {
        this.issueId = issueId;
    }
}

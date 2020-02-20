//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.Named;
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Entity
//@Table(name = "subscriptions")
//public class Subscription implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    private Long userId;
//    private String groupIds = "";
//    private String issueTypeIds = "";
//    private String projectIds = "";
//    private String assigneeUserIds = "";
//    private String reporterUserIds = "";
//    private String severityIds = "";
//    private String statusIds = "";
//
//    public void updateFields(Long userId, String statusIds, String severityIds, String projectIds, String groupIds, String assigneeUserIds)
//    {
//        this.userId = userId;
//        this.statusIds = statusIds;
//        this.severityIds = severityIds;
//        this.projectIds = projectIds;
//        this.groupIds = groupIds;
//        this.assigneeUserIds = assigneeUserIds;
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Subscription)) return false;
//        Subscription that = (Subscription) obj;
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
//        String thisId = id == null ? "" : id.toString();
//        return this.getClass().getSimpleName() + ":" + thisId;
//    }
//
//    // --------
//
//    public static List<Subscription> getAll()
//    {
//        return EOI.executeQuery("select * from subscriptions");
//    }
//
//    public static Subscription getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from subscriptions where id=?", Arrays.asList(id));
//    }
//
//    public static List<Subscription> getByUserId(Long userId)
//    {
//        return EOI.executeQuery("select * from subscriptions where user_id=?", Arrays.asList(userId));
//    }
//
//    public List<String> getProjectIdsAsList()
//    {
//        if (projectIds == null || projectIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(projectIds.split(",")));
//    }
//
//    public List<String> getGroupIdsAsList()
//    {
//        if (groupIds == null || groupIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(groupIds.split(",")));
//    }
//
//    public List<String> getSeverityIdsAsList()
//    {
//        if (severityIds == null || severityIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(severityIds.split(",")));
//    }
//
//    public List<String> getStatusIdsAsList()
//    {
//        if (statusIds == null || statusIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(statusIds.split(",")));
//    }
//
//    public List<String> getAssigneeUserIdsAsList()
//    {
//        if (assigneeUserIds == null || assigneeUserIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(assigneeUserIds.split(",")));
//    }
//
//    public List<String> getReporterUserIdsAsList()
//    {
//        if (reporterUserIds == null || reporterUserIds.length() == 0)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(reporterUserIds.split(",")));
//    }
//
//    private String buildClause(String description, List<Named> items, String clauseText) {
//        if (items.size() == 0)
//            return description;
//
//        if (description.length() > 0)
//            description += " AND ";
//        description += clauseText;
//        String clause = "";
//        for (Named item : items)
//        {
//            if (clause.length() > 0)
//            {
//                boolean isLast = items.indexOf(item) == items.size() - 1;
//                boolean isTwo = items.size() == 2;
//                if (isLast)
//                {
//                    if (!isTwo)
//                        clause += ",";
//                    clause += " or ";
//                }
//                else
//                    clause += ", ";
//            }
//            clause += item.getName();
//        }
//        description += clause;
//        return description;
//    }
//
//    public String getDescription()
//    {
//        String description = "";
//
//        List<Named> items = new ArrayList<>();
//        description = buildClause(description, items, " were reported by "); // reporters
//        description = buildClause(description, items, " are assigned to "); // assignees
//        description = buildClause(description, items, " are in group "); // groups
//        description = buildClause(description, items, " are in project "); // projects
//        description = buildClause(description, items, " have a severity of "); // severities
//        description = buildClause(description, items, " have a status of "); // statuses
//
//        return "Subscribe to issues that " + description + ".";
//    }
//
//    // -------- Getters / Setters ----------
//
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
//    public String getGroupIds()
//    {
//        return groupIds;
//    }
//
//    public void setGroupIds(String groupIds)
//    {
//        this.groupIds = groupIds;
//    }
//
//    public String getIssueTypeIds()
//    {
//        return issueTypeIds;
//    }
//
//    public void setIssueTypeIds(String issueTypeIds)
//    {
//        this.issueTypeIds = issueTypeIds;
//    }
//
//    public String getProjectIds()
//    {
//        return projectIds;
//    }
//
//    public void setProjectIds(String projectIds)
//    {
//        this.projectIds = projectIds;
//    }
//
//    public String getAssigneeUserIds()
//    {
//        return assigneeUserIds;
//    }
//
//    public void setAssigneeUserIds(String assigneeUserIds)
//    {
//        this.assigneeUserIds = assigneeUserIds;
//    }
//
//    public String getReporterUserIds()
//    {
//        return reporterUserIds;
//    }
//
//    public void setReporterUserIds(String reporterUserIds)
//    {
//        this.reporterUserIds = reporterUserIds;
//    }
//
//    public String getSeverityIds()
//    {
//        return severityIds;
//    }
//
//    public void setSeverityIds(String severityIds)
//    {
//        this.severityIds = severityIds;
//    }
//
//    public String getStatusIds()
//    {
//        return statusIds;
//    }
//
//    public void setStatusIds(String statusIds)
//    {
//        this.statusIds = statusIds;
//    }
//}

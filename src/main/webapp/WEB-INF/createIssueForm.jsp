<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 3/3/2020
  Time: 12:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! static Logger log = LoggerFactory.getLogger("createIssueForm"); %>

<% long start = System.nanoTime(); %>
<% log.debug("      start frmCreateIssue"); %>
<form id="frmCreateIssue" name="frmCreateIssue" method="post" action="${pageContext.request.contextPath}/issue/create">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <% long fieldStart = System.nanoTime(); %>
    <t:text        id="createIssueTitle" label="Title" required="true" />
    <% long fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:text        id="createIssueDescription" label="Description" required="true" />
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:basicSelect id="createIssueProject" label="Project" items="${projects}" required="true" horizontal="true"/>
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:basicSelect id="createIssueGroup" label="Group" items="${groups}" required="true" horizontal="true"/>
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:basicSelect id="createIssueIssueType" label="Issue Type" items="${issueTypes}" required="true" horizontal="true"/>
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:basicSelect id="createIssueSeverity" label="Severity" items="${severities}" required="true" horizontal="true"/>
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>

    <% fieldStart = System.nanoTime(); %>
    <t:basicSelect id="createIssueStatus" label="Status" items="${statuses}" required="true" horizontal="true"/>
    <% fieldDuration = (System.nanoTime() - fieldStart) / 1_000_000; %>
    <% log.debug("        fieldTime " + fieldDuration + " ms"); %>
</form>
<% long duration = (System.nanoTime() - start) / 1_000_000; %>
<% log.debug("      end frmCreateIssue " + duration + " ms"); %>

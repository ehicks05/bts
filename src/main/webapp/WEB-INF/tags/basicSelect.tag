<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>

<%@tag description="Text with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" required="false" %>
<%@attribute name="items" fragment="false" type="java.util.List<net.ehicks.bts.ISelectTagSupport>" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="horizontal" fragment="false" %>
<%@attribute name="required" fragment="false" %>

<c:set var="items" value="${ct:parseItemsForSelect(items)}" />

<c:if test="${horizontal}">
    <div class="field is-horizontal">
        <div class="field-label is-normal">
            <label class="label">${label}</label>
        </div>
        <div class="field-body">
            <div class="field">
                <div class="control">
                    <div class="select">
                        <input type="hidden" id="${id}prev" value="${value}" />
                        <select id="${id}" name="${id}" <c:if test="${required}">required</c:if> onblur="blurHandler(this.id)">
                            <c:if test="${!required}">
                                <option value=""></option>
                            </c:if>
                            <c:forEach var="item" items="${items}">
                                <c:set var="selected" value="${item.value eq value ? 'selected' : ''}" />
                                <option value="${item.value}" ${selected}>${item.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${!horizontal}">
    <div class="field">
        <label class="label">${label}</label>
        <div class="control">
            <div class="select">
                <input type="hidden" id="${id}prev" value="${value}" />
                <select id="${id}" name="${id}" <c:if test="${required}">required</c:if> onblur="blurHandler(this.id)">
                    <c:if test="${!required}">
                        <option value=""></option>
                    </c:if>
                    <c:forEach var="item" items="${items}">
                        <c:set var="selected" value="${item.value eq value ? 'selected' : ''}" />
                        <option value="${item.value}" ${selected}>${item.text}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
</c:if>
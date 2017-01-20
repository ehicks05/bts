<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" type="net.ehicks.bts.beans.User" scope="request"/>
<jsp:useBean id="avatars" type="java.util.List<net.ehicks.bts.beans.DBFile>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function formatAvatar(avatarOption)
        {
            if (!avatarOption.id)
            {
                return avatarOption.text;
            }

            var avatarId = avatarOption.id;
            var src = document.getElementById('avatar' + avatarId).src;
            var $avatar = $('<span><img style="padding-bottom:5px; height:22px;" src="' + src + '" class="img-flag" /> ' + avatarOption.text +  '</span>');
            return $avatar;
        }
    </script>
</head>
<body>

<div style="display: none;">
    <c:forEach var="avatar" items="${avatars}">
        <img id="avatar${avatar.id}" src="${avatar.base64}"/>
    </c:forEach>
</div>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Modify User ${user.logonId}</h5></div>

        <form id="frmUser" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&tab3=modify&action=modify&userId=${user.id}">
            <div style="padding: 10px;">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="userId" name="userId" type="text" value="${user.id}" readonly/>
                    <label class="mdl-textfield__label" for="userId">User Id:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="logonId" name="logonId" type="text" value="${user.logonId}"/>
                    <label class="mdl-textfield__label" for="logonId">Logon Id:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="firstName" name="firstName" type="text" value="${user.firstName}"/>
                    <label class="mdl-textfield__label" for="firstName">First Name:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="lastName" name="lastName" type="text" value="${user.lastName}"/>
                    <label class="mdl-textfield__label" for="lastName">Last Name:</label>
                </div>
                <br>
                <div>
                    <label class="" for="avatarId">Avatar:</label>
                    <t:select id="avatarId" items="${avatars}" value="${user.avatarId}" formatFunction="formatAvatar"/>
                </div>
                <br>
                <div>
                    <label class="" for="groups">Groups:</label>
                    <t:multiSelect id="groups" items="${groups}" selectedValues="${user.allGroupIds}" placeHolder="None"/>
                </div>
            </div>

            <div class="mdl-card__actions">
                <input id="saveUserButton" type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>
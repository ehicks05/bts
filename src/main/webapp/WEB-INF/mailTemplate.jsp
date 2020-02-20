<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<body>
<table style="width: 100%;background-color: #eee;">
    <tr><td>
            <table style="width: 500px;margin:auto;background-color: white">
                <tr>
                    <td style="padding: 10px;"><h1><a href="${emailContext}/issue/form&issueId=${issue.id}">
                            ${issue.project.prefix}-${issue.id} ${issue.title}</a></h1></td>
                </tr>
            <tr>
                    <td style="padding: 10px;">
                        <h3>
                                <img style="width: 32px;" src="${emailContext}/avatar/${user.avatar.id}.png" />
                                ${user.username} ${EmailAction.getById(actionId).getVerb()}.
                            </h3>
                        <p>${description}</p>
                    </td>
                </tr>
        </table>
    </td></tr>
</table>

</body>
</html>

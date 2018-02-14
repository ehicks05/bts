<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>
    <script>
        var socket = new WebSocket("ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/chat");
        socket.onmessage = onMessage;

        $(function () {
            $('#fldContents').on('keypress', function (e) {
                if ($('#fldContents').val() && e.keyCode === 13)
                {
                    formSubmit();
                }
            });
        });

        function onMessage(event) {
            var message = JSON.parse(event.data);
            if (message.action === "addMessage") {
                printMessage(message);
            }
            if (message.action === "addRoomMember") {
                printRoomMember(message);
            }
            if (message.action === "updateRoomMember") {
                updateRoomMember(message);
            }
            if (message.action === "addPerson") {
                printPerson(message);
            }
            if (message.action === "updatePerson") {
                updatePerson(message);
            }
            if (message.action === "addRoom") {
                printRoom(message);
            }
        }

        function addMessage(contents) {
            var messageAction = {
                action: "addMessage",
                contents: contents
            };
            socket.send(JSON.stringify(messageAction));
        }
        function changeRoomMessage(newRoom) {
            var messageAction = {
                action: "changeRoom",
                newRoom: newRoom
            };
            socket.send(JSON.stringify(messageAction));
        }
        function changeToPrivateRoomMessage(otherUserId) {
            var messageAction = {
                action: "changeToPrivateRoom",
                otherUserId: otherUserId
            };
            socket.send(JSON.stringify(messageAction));
        }

        function printMessage(message) {
            var messages = document.getElementById("messages");

            var autoScroll = (messages.scrollHeight - messages.scrollTop) === messages.clientHeight;

            var messageArticle = document.createElement("article");
            messageArticle.classList.add('media');
            messageArticle.setAttribute("id", message.id);
            messages.appendChild(messageArticle);

            var figure = document.createElement('figure');
            figure.classList.add('media-left');
            messageArticle.appendChild(figure);

            var para = document.createElement('p');
            para.classList.add('image', 'is-32x32');
            figure.appendChild(para);

            var img = document.createElement('img');
            img.setAttribute('src', message.avatarBase64);
            para.appendChild(img);

            var mediaContentDiv = document.createElement('div');
            mediaContentDiv.classList.add('media-content');
            messageArticle.appendChild(mediaContentDiv);

            var contentDiv = document.createElement('div');
            contentDiv.classList.add('content');
            mediaContentDiv.appendChild(contentDiv);

            var plainDiv = document.createElement('div');
            contentDiv.appendChild(plainDiv);

            var strong = document.createElement('strong');
            strong.innerHTML = message.author;
            plainDiv.appendChild(strong);

            var dateSpan = document.createElement('span');
            dateSpan.innerHTML = ' ' + message.timestamp;
            plainDiv.appendChild(dateSpan);

            var breakEl = document.createElement('br');
            plainDiv.appendChild(breakEl);

            var contentsSpan = document.createElement('span');
            contentsSpan.innerHTML = message.contents;
            plainDiv.appendChild(contentsSpan);

            if (autoScroll)
            {
                document.getElementById("messages").scrollTo(0, document.getElementById("messages").scrollHeight);
            }
        }

        function printRoomMember(roomMember) {
            var roomMembers = document.getElementById("roomMembers");

            var messageArticle = document.createElement("article");
            messageArticle.classList.add('media');
            messageArticle.setAttribute("id", 'roomMemberArticle' + roomMember.id);
            roomMembers.appendChild(messageArticle);

            var figure = document.createElement('figure');
            figure.classList.add('media-left');
            messageArticle.appendChild(figure);

            var para = document.createElement('p');
            para.classList.add('image', 'is-32x32');
            figure.appendChild(para);

            var img = document.createElement('img');
            img.setAttribute("id", 'roomMemberImg' + roomMember.id);
            img.setAttribute('src', roomMember.avatarBase64);
            para.appendChild(img);

            var mediaContentDiv = document.createElement('div');
            mediaContentDiv.classList.add('media-content');
            messageArticle.appendChild(mediaContentDiv);

            var contentDiv = document.createElement('div');
            contentDiv.classList.add('content');
            mediaContentDiv.appendChild(contentDiv);

            var plainDiv = document.createElement('div');
            contentDiv.appendChild(plainDiv);

            var strong = document.createElement('strong');
            strong.setAttribute("id", 'roomMemberName' + roomMember.id);
            strong.innerHTML = roomMember.name;
            plainDiv.appendChild(strong);

            var mediaRightDiv = document.createElement('div');
            mediaRightDiv.classList.add('media-right');
            messageArticle.appendChild(mediaRightDiv);

            var statusSpan = document.createElement('span');
            statusSpan.setAttribute("id", 'roomMemberStatus' + roomMember.id);
            statusSpan.classList.add('tag', roomMember.statusClass);
            // statusSpan.innerHTML = '  ';
            mediaRightDiv.appendChild(statusSpan);
        }

        function updateRoomMember(roomMember) {
            if (!document.getElementById('roomMemberImg' + roomMember.id))
                return;

            var img = document.getElementById('roomMemberImg' + roomMember.id);
            img.setAttribute('src', roomMember.avatarBase64);

            var strong = document.getElementById('roomMemberName' + roomMember.id);
            strong.innerHTML = roomMember.name;

            var statusSpan = document.getElementById('roomMemberStatus' + roomMember.id);
            statusSpan.className = 'tag ' + roomMember.statusClass;
        }

        function printPerson(person) {
            var people = document.getElementById("people");

            var personDiv = document.createElement("div");
            personDiv.classList.add('button', 'is-fullwidth');
            personDiv.setAttribute("id", 'person' + person.id);
            personDiv.addEventListener("click", changeToPrivateRoom);
            people.appendChild(personDiv);

            var statusSpan = document.createElement('span');
            statusSpan.classList.add('icon');
            personDiv.appendChild(statusSpan);

            var icon = document.createElement('i');
            icon.setAttribute("id", 'personStatus' + person.id);
            icon.classList.add('fa-circle', 'has-text-primary', person.statusIcon);
            statusSpan.appendChild(icon);

            var nameSpan = document.createElement('span');
            nameSpan.setAttribute("id", 'personName' + person.id);
            nameSpan.innerHTML = person.name;
            personDiv.appendChild(nameSpan);
        }

        function updatePerson(person) {
            var strong = document.getElementById('personName' + person.id);
            strong.innerHTML = person.name;

            var icon = document.getElementById('personStatus' + person.id);
            // icon.className = 'fa-circle has-text-primary ' + person.statusIcon;
            icon.setAttribute("data-prefix", person.statusIcon);
        }

        function printRoom(room) {
            var rooms = document.getElementById("roomList");

            var roomDiv = document.createElement("div");
            roomDiv.classList.add('button', 'is-fullwidth');
            roomDiv.onclick = changeRoom;
            roomDiv.setAttribute("id", 'room' + room.id);
            roomDiv.innerHTML = room.name;
            rooms.appendChild(roomDiv);
        }

        function formSubmit() {
            var contents = document.querySelector('#addMessageForm #fldContents').value;
            document.querySelector('#addMessageForm #fldContents').value = '';
            addMessage(contents);
        }
        function changeRoom(e) {
            var newRoom = e.target.id.replace('room', '');

            var messages = document.getElementById("messages");
            while (messages.firstChild) {
                messages.removeChild(messages.firstChild);
            }

            var roomMembers = document.getElementById("roomMembers");
            while (roomMembers.firstChild) {
                roomMembers.removeChild(roomMembers.firstChild);
            }

            $('#people .is-info').removeClass('is-info');
            $('#roomList .is-info').removeClass('is-info');
            $('#room' + newRoom).addClass('is-info');
            changeRoomMessage(newRoom);
        }

        function changeToPrivateRoom(e) {
            var otherUserId = e.currentTarget.id.replace('person', '');

            // nuke messages
            var messages = document.getElementById("messages");
            while (messages.firstChild) {
                messages.removeChild(messages.firstChild);
            }

            // nuke room members
            var roomMembers = document.getElementById("roomMembers");
            while (roomMembers.firstChild) {
                roomMembers.removeChild(roomMembers.firstChild);
            }

            $('#people .is-info').removeClass('is-info');
            $('#roomList .is-info').removeClass('is-info');
            $('#person' + otherUserId).addClass('is-info');
            changeToPrivateRoomMessage(otherUserId);
        }
    </script>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Chat
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-fifth">
                <h2 class="subtitle">Rooms</h2>
                <div id="roomList" class="has-text-centered">

                </div>
                <br>
                <h2 class="subtitle">People</h2>
                <div id="people" class="has-text-centered">

                </div>
            </div>
            <div class="column is-three-fifths">
                <h2 class="subtitle">Messages</h2>
                <div id="messages" style="height:500px; overflow-y: auto;">

                </div>

                <div id="addMessageForm" style="margin-top:5px;">

                    <div class="field has-addons">
                        <div class="control is-expanded">
                            <input class="input" type="text" id="fldContents" name="fldContents" value="" />
                        </div>
                        <div class="control">
                            <a class="button is-primary" onclick="formSubmit();">
                                Submit
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="column is-one-fifth">
                <h2 class="subtitle">Room Members</h2>
                <div id="roomMembers">

                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>
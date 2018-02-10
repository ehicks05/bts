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

            var roomMemberDiv = document.createElement("div");
            roomMemberDiv.setAttribute("id", roomMember.id);
            roomMembers.appendChild(roomMemberDiv);

            var roomMemberName = document.createElement("span");
            roomMemberName.innerHTML = roomMember.name;
            roomMemberDiv.appendChild(roomMemberName);
        }

        function printRoom(room) {
            var rooms = document.getElementById("roomList");

            var roomDiv = document.createElement("div");
            roomDiv.classList.add('button');
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

            $('#roomList .is-primary').removeClass('is-primary');
            $('#room' + newRoom).addClass('is-primary');
            changeRoomMessage(newRoom);
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
                <div class="box">
                    <h2 class="subtitle">Rooms</h2>
                    <hr>
                    <div id="roomList" class="has-text-centered">

                    </div>
                </div>
            </div>
            <div class="column is-three-fifths">
                <div class="box">
                    <h2 class="subtitle">Messages</h2>
                    <hr>
                    <div id="messages" style="max-height:300px; overflow-y: auto;">

                    </div>
                    <hr>

                    <div id="addMessageForm">
                        <t:text id="fldContents" horizontal="false"/>
                        <button class="button is-primary" onclick="formSubmit();">Submit</button>
                    </div>
                </div>
            </div>
            <div class="column is-one-fifth">
                <div class="box">
                    <h2 class="subtitle">People</h2>
                    <hr>
                    <div id="roomMembers">

                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>
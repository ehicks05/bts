* security:
	* does a room exist inside of a group?
		* yes, unless we need cross-group rooms...
		* maybe rooms have a security setting
			* site-wide room
			* group-level security, where u pick group(s) that can access the room
			* invite-only room
	* access to a room 
	* access to a room means you can see everything related to that room 
* todo: where do we apply security to filter what the client can see?
    * sendMessageToClient?
* system init
	* load rooms into memory
	* load room membership into memory
	* load messages into memory
		* maybe just N most recent
* client enters chat page
	* client initiates connect websocket
	* add to list of live sessions
	* server send rooms to client
	* server send room membership to client
	* server send messages to client
* at this point, any changes to the chat tables should be broadcast to our client
* client changes room
	* delete client-side rooms, memberships, and messages
	* client sends changeRoom request
	* server send room membership to client
	* server send messages to client
* client adds a message
	* client sends addMessage request
	* server send new message to client
* client creates a room
	* client sends createRoom request
	* server create room and save to db
	* server send new room to client

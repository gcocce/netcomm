# Intro

This repo has a basic client and server for a multi-client game board, the client open a basic window and allows to start a chat window. The server provides communications among clients.

This project has network communications functionality already solved applying multi-threading and client functionality is ready to be extended.

The client is a MVC which has GameModel, GameView and GameControler classes.

![Client](https://preview.ibb.co/f5wcfz/netcomm_client.png)

# Running

In order to get started the server needs a parameter: the port number where it is going to listen for connexions.

After running the client in order to start a connexion it needs both the host's address and the port number.

![Client](https://preview.ibb.co/mrbM7e/netcomm_client_connect.png)
 
Once the client is connected a chat windows can be opened.



# Extending Client Functionality

In order to extend client's functionality two things needs to be done: add a new type of packet with the game info to be exchanged and the functionality into the GameControler and maybe GameView in order to process that packet.

The class common.Packet has an enum with the different types of packets. Each packet has a header and a payload, the header is used to encode the type of packet, the payload may contain any data defined by the programmer. 

For every new Packet it is necessary to:

* Add a new type to Tipo enum existing in Packet class.
* Extends Packet with new class.
* Add functionality to PacketManager to serialize and deserialize the new Packet.
* Add functionality to GameController method OnPacketReceived to process the new type of Packet.

```
		if(p.getType()==Packet.Tipo.CHAT){
			ChatPacket cp=(ChatPacket)p;
			Message m=new Message(cp.getUser(), cp.getMessage());
			gameModel.addMessage(m);
		}
```


# Example:

For an example consider how ChatPacket class is used.


# License:

[MIT](https://opensource.org/licenses/MIT)



# Lab 2 - Implementation of a protocol stack

## Table of contents
  
  * [Task Description](#task-description)
      * [Server](#server) 
      * [Client](#client)
      * [Packets](#packets)
      * [Encryption](#encryption-algorithm)
  * [File List](#file-list)
  * [Implementation](#implementation)
  * [How to use](#how-to-use)
  
## Task Description
In this project we should implemented a protocol stack, namely a transport protocol based on UDP, a session-level security protocol inspired by SSL/TLS, and an application-level protocol.
You must present this project as a client and a server, both using a library that contains the protocol logic.
The library must be made up of 3 modules, for each level of the protocol stack, with a well-defined API and that adheres to the layered architecture.
For transport and session level protocols the BSD Sockets API is a recommended source of inspiration, while for the application-level protocol something that resembles an HTTP client API is a recommended source of inspiration.

## File List
- HttpServerApplication.java
- UDPClient.java
- UDPServer.java
- Packet.java
- AES.java

## Implementation 

I have the following features implemented in my project :
* Client
* Server and HttpServer
* Simulation routing of network packets
* Encryption algorithm using AES – Advanced Encryption Standard
* Multicasting

-------------------------

I have tried to implement program that enables reliable data transfer between a server and a client over the UDP protocol using DatagramChannel.
Also I have tried to simulate application-level protocol something that resembles an HTTP client API using "GET" and "POST" requests.
This HTTP client API have been based on file manager example between client ans server.

---------------------


#### UDP Client & UDP Server
For establishing connection between UDP Client & UDP Server I created two classes UDPClient and UDPServer and using DatagramChannel established the connection between them two.
Java NIO Datagram is used as channel which can send and receive UDP packets over a connection less protocol. DataGram channel can be open by calling its one of the static method named as open() which can also take IP address as parameter so that it can be used for multi casting.

This example opens a DatagramChannel which can receive packets on UDP port 8080:

```
int port = 8080;
...
server.listenAndServe( port, directory);
...
try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            ...
```

send(ByteBuffer src, SocketAddress target) − This method is used to send datagram via this channel.
```
channel.send(resp.toBuffer(), router);
```

 The receive( ) method reads a packet. Besides the special purpose receive( ) method, DatagramChannel has the usual three read( ) methods.
```
 SocketAddress router = channel.receive(buf);
```
 #### Packet transmission

Packet represents a simulated network packet. As we don't have unsigned types in Java, we can achieve this by using a larger type.

The following code creates a builder from the current packet It's used to create another packet by re-using some parts of the current packet.


```
 public Builder toBuilder(){
        return new Builder()
                .setType(type)
                .setSequenceNumber(sequenceNumber)
                .setPeerAddress(peerAddress)
                .setPortNumber(peerPort)
                .setPayload(payload);
```

fromBuffer creates a packet from the given ByteBuffer in BigEndian.

```
public static Packet fromBuffer(ByteBuffer buf) throws IOException {
    if (buf.limit() < MIN_LEN || buf.limit() > MAX_LEN) {
        throw new IOException("Invalid length");
    }

    Builder builder = new Builder();

    builder.setType(Byte.toUnsignedInt(buf.get()));
    builder.setSequenceNumber(Integer.toUnsignedLong(buf.getInt()));

    byte[] host = new byte[]{buf.get(), buf.get(), buf.get(), buf.get()};
    builder.setPeerAddress(Inet4Address.getByAddress(host));
    builder.setPortNumber(Short.toUnsignedInt(buf.getShort()));

    byte[] payload = new byte[buf.remaining()];
    buf.get(payload);
    builder.setPayload(payload);

    return builder.create();
}
```

fromBytes creates a packet from the given array of bytes:

```
public static Packet fromBytes(byte[] bytes) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(MAX_LEN).order(ByteOrder.BIG_ENDIAN);
    buf.put(bytes);
    buf.flip();
    return fromBuffer(buf);
}
```
 Therefore, the main idea of my project is to show how Http client api working with UDP Server, I have preformed methods which analyse http requests and implement file managing:
 * POST file with text 
 * GET file with text
 * UPDATE(Post) existed file with text(post includes update methods too)
 
 **HTTP parser**
 It has very simple logic in order to simulate basic operations of HTTP API requests.
 
 Client sends request
 
 like string
 ("post -h headesEx. -d body1 test.txt")  which is converted into -->  (POST test.txt HTTP\1.0\headesEx.-d\\body1) 
 
 and server sends response in the following form(using postResponse( ) and getResponse( ) methods):
 
 (HTTP\1.0 201 Created\headesEx.-d\Content-length: 5\Content-Type: text\html\\body1)
 
```
                    method = splitClientPayload[0];
                    url = splitClientPayload[1];
                    requestHeader = splitClientPayload2[2];

                    if (method.equals("get") || method.equals("GET")) {
                        if (url.equals("/") & url.length() == 1) {
                            getFileResponse(directory);
                        } else {
                            getResponse(directory, url);
                        }
                    } else {
                        if (splitClientPayload2.length > 3) {
                            requestEntityBody = splitClientPayload2[4];
                        }
                        postResponse(directory, url, requestEntityBody);
                    }

                    serverResponse = responseVersion + " " + statusCode + " " + phrase + "\\" + responseHeader + "\\" + "\\" + responseEntityBody;

                    System.out.println("SERVER: Sending this message to client: " + serverResponse);
```
Also there are implemented methods for "3 way handshake" in order to make reliable UDP protocol to guarantee packet transmission.

--------------------

#### Client

Get request
It accepts client request as a parameter. Written request is split by spaces and analyzed on header presence. 

In order to send this payload to server and establish the connection, three-way handshake operation is performed:
SYN message is sent to the server side from the client throught eh open channel.
Server is set to non-blocking mode to receive SYN message during the specified timeout on the client side.
If there is no response within timeout "No response after timeout" - is printed.

Server code:
```
                    // CONNECTION REQUEST
                if (packet.getType() == 1) {
                    System.out.println("SERVER: Sending back syn ack");
                    Packet resp = packet.toBuilder()
                            .setType(2)
Client code:
```
                                  String msg = "SYN";            // Creating packet
                                  Packet p = new Packet.Builder()
                                          .setType(1)
                                          .setSequenceNumber(sequenceNumber)
                                          .setPortNumber(serverAddress.getPort())
                                          .setPeerAddress(serverAddress.getAddress())
                                          .setPayload(msg.getBytes())
                                          .create();
                                  channel.send(p.toBuffer(), routerAddr);
                                  System.out.println("CLIENT: Sending SYN request to the router address: " + routerAddr);
```
Otherwise, we receive a packet sent from the server with SYN-ACK message. After that we are sending and ACK message to the server, which means that we have established connection with the server.

**Also, what is important, we check whether sequence number remained the same.**

Only if it remained the same, we finish three-way handshake.
Now we can send our payload to the server. Packet is constructed and sent to the server and client is waiting for the response 
which should be received within timeout set using the selector select() function:
```
                // Try to receive a packet within timeout.
                channel.configureBlocking(false);
                Selector selector = Selector.open();
                channel.register(selector, OP_READ);
                System.out.println("Waiting for the response... ");
                selector.select(15000);
```
If payload is received, it is decrypted using the same secret key and is printed in the client's terminal window.

After all these actions done, connection, 
connection between client through sending FIN message to the server and receiving FIN ACK message back.
Otherwise, connection can be identified as terminated.
                            .create();
                    channel.send(resp.toBuffer(), router);
                }
```

```
// CLOSING REQUEST
                if (packet.getType() == 3) {
                    System.out.println("SERVER: Sending back FYN-ACK");
                    Packet resp = packet.toBuilder()
                            .setType(4)
                            .create();
                    channel.send(resp.toBuffer(), router);
```
Post request (Similar to the GET request)

Request is split by spaces and analyzed on header and directory presence. We need to specify directory in order to create a file in which later we will be able to post data that we are sending.
After that http request payload is constructed. Three-way handshake described above is done. Now we can send our payload to the server. 

-------------

#### Packets
 
 So this part was very interesting.
 Each packet has their own properties:
 ```
    private final int type;
    private final long sequenceNumber;
    private final InetAddress peerAddress;
    private final int peerPort;
    private final byte[] payload;
```
Which helped me to distinguish each other. It comes useful when I sent response of some packet, and it should has the same port and address like the previous one.

Moreover, because the original BSD socket API includes the concept of "network byte order", which is big-endian. 
I have wrote methods which Create a byte buffer in BigEndian for the packet.

```
   public ByteBuffer toBuffer() {
        ByteBuffer buf = ByteBuffer.allocate(MAX_LEN).order(ByteOrder.BIG_ENDIAN);
        write(buf);
        buf.flip();
        return buf;
    }
```

Also all the packets with different packet type is directly followed by the payload data.
After execution of specific method each packet obtains defined"packet type":
```
 Packet p = new Packet.Builder()
                        .setType(0) //sending get request
                        .setSequenceNumber(1L)
                        .setPortNumber(serverAddress.getPort())
                        .setPeerAddress(serverAddress.getAddress())
                        .setPayload(message.getBytes())
                        .create();
                channel.send(p.toBuffer(), routerAddr);

   Packet p = new Packet.Builder()
                        .setType(3) //closing connection
                        .setSequenceNumber(sequenceNumber)
                        .setPortNumber(serverAddress.getPort())
                        .setPeerAddress(serverAddress.getAddress())
                        .setPayload(msg.getBytes())
                        .create();
```
Obviously sequenceNumber++; increases of each packet.
 
--------------------

#### Encryption algorithm

In order to encrypt my data in the packets despite default decryption I have used AES(Advanced Encryption Standard).
AES is block cipher capable of handling 128 bit blocks, using keys sized at 128, 192, and 256 bits. Each cipher encrypts and decrypts data in blocks of 128 bits using cryptographic keys of 128-, 192- and 256-bits, respectively. It uses the same key for encrypting and decrypting, so the sender and the receiver must both know — and use — the same secret key.

All logic is implemented in AES.java class.
I have used base64 encoding in UTF-8 charset.
Each message that sends is encrypted and decrypted in this way:
```
                String message = AES.encrypt(clientPayload, "Burlacu"); //"Burlacu" is the secret key
                String decryptedPayload = AES.decrypt(payload, "Burlacu");
```

#### Multicasting
IP multicasting is the transmission of IP datagrams to members of a group that is zero or more hosts identified by a single destination address.

The join(InetAddress,NetworkInterface,InetAddress) method is used to begin receiving datagrams sent to a group whose source address matches a given source address. This method throws UnsupportedOperationException if the underlying platform does not support source filtering. Membership is cumulative and this method may be invoked again with the same group and interface to allow receiving datagrams from other source addresses. The method returns a MembershipKey that represents membership to receive datagrams from the given source address. Invoking the key's drop method drops membership so that datagrams from the source address can no longer be received.

```
     // join multicast group on this interface, and also use this
     // interface for outgoing multicast datagrams
     InetAddress address = InetAddress.getLocalHost();
    NetworkInterface ni = NetworkInterface.getByInetAddress(address);
    InetAddress group = InetAddress.getByName("239.255.0.1");
    
     DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)
         .setOption(StandardSocketOptions.SO_REUSEADDR, true)
         .bind(new InetSocketAddress(5000))
         .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);

     MembershipKey key = channel.join(group, ni);
```
Note that DatagramChannel.join() requires a NetworkInterface to work.

## How to use
- Run first HTTPServerApplication.java class. In console write your directory with files which would like to manage "-d C:\\..." 
- Run second UDPClient.java class and in console write yor desired command is following format "post/get -h 'header' -d 'body' nameOfTheFile"
- Check file in your directory 

## Author(s)

* [**Galaju Margareta**](https://github.com/MargaretaGalaju)

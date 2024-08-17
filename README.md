# Text Server/Client Connections

## Overview

This is a simple client-server application that communicates over TCP. The client sends a message to the server, and the server responds with a message. The client-server communication is text-based. The client initiates communication to the server. The server responds to the client based on the message received. The server should reply “PARDON” whenever it receives a message it does not understand or expect. The server only has to handle a single client, and once that client has exited the server should exit.

## Usage

The server and client are written in Java. The server is a simple server that listens for a connection on a specified port. The client connects to the server and sends a message.

### Server

```java
java SimpleServer <port>
```

### Client

```java
java SimpleClient <IPaddress> <port>
```

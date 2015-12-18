This is a simple client/server program written in Java.

The general idea is that the client sends a HTML GET request to a server, which sends an answer back to the client. The client then prints the information it received.

Currently the payload sent from the client to the server consists of a path and a file. If the server finds that file, it sends it's contents back to the client. If the file does not exist, the server sends an error  message.

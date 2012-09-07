package at.starcoders.yaak.communication.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import at.starcoders.yaak.Logger;
import at.starcoders.yaak.communication.ReceiveListener;
import at.starcoders.yaak.communication.Sink;


public class TCPServer extends Sink implements ReceiveListener, Runnable{

	private int port;
	private ArrayList<TCPClient> clientList;
	private ServerSocket ss;
	private boolean running;
	
	public TCPServer(int port) {
		this.port = port;
		clientList = new ArrayList<TCPClient>();
	}
	
	public void removeSocket(TCPClient client) {
		clientList.remove(client);
	}
	
	private void init() {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (ss == null)
			init();
		running = true;
		Socket s = null;
		while (running) {
			Logger.log("Server running");
			try {
				if (ss != null && !ss.isClosed()) {
					Logger.log("Waiting for client on port " + port);
					s = ss.accept();
					Logger.log("Client connected");
					if (s != null) {
						TCPClient conn= new TCPClient(this,s);
						clientList.add(conn);
						conn.addReceivedListener(this);
				        Thread t = new Thread(conn);
				        t.start();
					}
				} else {
					Logger.log("ServerSocket closed");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		Logger.log("Exiting serversocket");
	}

	public void close() {
		try {
			for (TCPClient client : clientList)
				client.socket.close();
			if (ss != null)
				ss.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendMessage(String action) {
		for (TCPClient client : clientList) 
			client.sendMessage(action);
	}

	@Override
	public void received(String data) {
		Logger.log("Recv: " + data);
		if (data.startsWith("trigger"))
			fireTriggerEvent();
	}
	
	class TCPClient implements Runnable {
	    private boolean running;
	    private DataInputStream in;
	    private PrintStream out;
	    private TCPServer server;
	    private ArrayList<ReceiveListener> listener;
	    private Socket socket; 
	    
	    TCPClient(TCPServer server, Socket socket) {
	    
	      this.server = server;
	      this.socket = socket;
	      listener = new ArrayList<ReceiveListener>();
	      try {
			in = new DataInputStream(socket.getInputStream());
			out = new PrintStream(socket.getOutputStream());
	      } catch (IOException e) {
	    	e.printStackTrace();
	      }
	    }
	    
	    public void addReceivedListener(ReceiveListener rl) {
	    	listener.add(rl);
	    }
	    
	    private void fireReceivedEvent(String data) {
	    	for (ReceiveListener l : listener)
	    		l.received(data);
	    }

	    public void run () {
	    	running = true;
	    	while (running) {
	    		try {
	    			if (socket.isClosed()) {
	    				running = false;
	    				continue;
	    			}
	    			if (in != null) {
		    			String recv = in.readLine();
						if (recv != null) {
							if (recv.startsWith("quit")) {
								running = false;
							} else 
								fireReceivedEvent(recv);
						}
	    			} else {
	    				running = false;
	    			}
				} catch (IOException e) {
					running = false;
				} catch (NullPointerException ne) {
					running = false;
				}
	    	}
	    	System.out.println("Closing socket");
	    	try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	server.removeSocket(this);
	    }
	    
	    public void sendMessage(String data) {
	    	out.println(data);	    	
	    }
	}



}

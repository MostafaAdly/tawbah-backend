package me.mostafa.tawbah.dev.constructors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import me.mostafa.tawbah.Tawbah;

public class Server {

	private ServerSocket server;

	@SuppressWarnings("resource")
	public Server(int port) throws IOException {
		Tawbah.print("Server created.");
		server = new ServerSocket(port);
		Socket client = new Socket(Tawbah.ip, Tawbah.port);
		while (!server.isClosed()) {
			client = server.accept();
			new Client(client).startListening();
		}
	}

	public void startListener(Socket socket) throws IOException {
		new Thread(new Runnable() {

			@Override
			public void run() { // DECODE FROM 'unicode-escape'
				try {
					if (socket != null && !socket.isClosed()) {
						Tawbah.print(" -- MessageListener started -- ");
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String str = in.readLine();
						System.out.println(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

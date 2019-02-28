package manchester;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.util.Arrays;

/**
 * Classe atua como um servidor Multicast. Possui funções de envio e recebimento
 * de mensagens. Automaticamente repassa as mensagens chegadas à um "ouvinte"
 * 
 * @author Lucas
 */

class MultiCastServer implements Runnable {
	private static MultiCastServer instance;

	final String address;
	final int port;
	final private MessageListener receiveCallBack;
	final MulticastSocket socket;
	final InetAddress multicastAddressGroup;
	final ServerSocket serverSocket;
	private String myIpAddress;
	private byte[] lastMessage;//gambi

	/**
	 * Contructor
	 * 
	 * @param address
	 *            String com o endereço do multicast
	 * @param port
	 *            int com o númer da porta
	 * @param identification
	 *            String com a identificação
	 * @param receiverCallBack
	 *            ListenerMessage
	 * @throws IOException
	 */
	private MultiCastServer(String address, int port, MessageListener receiverCallBack) throws IOException {
		this.address = address;
		this.port = port;
		this.socket = new MulticastSocket(port);
		this.multicastAddressGroup = InetAddress.getByName(address);
		this.socket.joinGroup(multicastAddressGroup);
		this.serverSocket = new ServerSocket();
		this.serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));
		this.receiveCallBack = receiverCallBack;

	}

	/**
	 * Método que retorna uma instância da classe
	 *
	 * @return MulticastServer
	 * @throws IOException
	 */
	public static void replaceInstance(String address, int port, MessageListener receiverCallBack) throws IOException {
		instance = new MultiCastServer(address, port, receiverCallBack);
	}

	public static MultiCastServer getInstance() {
		return instance;
	}

	/**
	 * Método que envia uma mensagem
	 * 
	 * @param msg
	 *            Message mensagem
	 */
	public void sendMessage(byte[] data) {
		try {
			// Util.log("Sending Message from: " + msg.sender + " to: "
			// + msg.receiver+ " ttype: "+msg.type+" dataSize:
			// "+msg.data.length, Configurations.OUT_LOG);
			// Util.log("Message Total Lenght:"+data.length,
			// Configurations.OUT_LOG);
			lastMessage = Arrays.copyOf(data, data.length);

			DatagramPacket dataPack = new DatagramPacket(data, 0, data.length, multicastAddressGroup, port);
			myIpAddress = dataPack.getAddress().toString();
			socket.send(dataPack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que recebe e despacha pacotes
	 */
	public void run() {
		while (true) {
			byte[] buffer = new byte[2048];
			DatagramPacket data = new DatagramPacket(buffer, buffer.length);
			try {
				data.getAddress();
				socket.receive(data);
				final byte[] datagramData = Util.range(data.getData(), 0, data.getLength());
				if (!Util.compareArrays(lastMessage, datagramData)) {///gambi
					this.receiveCallBack.messageReceivedUpdate(datagramData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
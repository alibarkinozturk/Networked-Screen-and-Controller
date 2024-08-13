package Socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

    Scanner sc = new Scanner(System.in);
    DatagramSocket ds;
    InetAddress ip;
    byte buf[];
    byte[] msg;
    DatagramPacket DpReceive;
    int socketNum;

    public Client(int socket) throws Exception {
        System.out.println("Client");
        ds = new DatagramSocket(socket);
        ip = InetAddress.getLocalHost();
        buf = null;
        msg = new byte[65535];
        DpReceive = null;

    }

    public void sendCommand(String inp, int sckt) throws IOException {

        buf = inp.getBytes();

        DatagramPacket DpSend
                = new DatagramPacket(buf, buf.length, ip, sckt);

        ds.send(DpSend);

    }

    public String getMessage() throws IOException {
        DpReceive = new DatagramPacket(msg, msg.length);

        ds.receive(DpReceive);
        String m = String.valueOf(data(msg));
        System.out.println("Client send this message: " + m);

        msg = new byte[65535];
        return m;

    }

    public static StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    public void closeSocket() {
        ds.close();
    }
}

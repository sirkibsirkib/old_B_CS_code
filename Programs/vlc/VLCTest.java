package vlc;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;

// VLC RC Command references/examples
//   - https://github.com/mguinada/vlc-client/blob/master/lib/vlc-client/client/media_controls.rb
//   - http://getluky.net/2006/04/19/vlcs-awesome-rc-interface/
class VLCTest {
  
  private static BufferedReader in;
  private static OutputStream out;
  
  public static void main(String[] args) throws Exception {
    InetAddress localhost = InetAddress.getByName("localhost");
    Socket socket = new Socket(localhost, 8080);
    out = socket.getOutputStream();
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    // Just read responses from VLC in a seperate thread.
    Thread readerThread = new Thread(new Runnable() {
      public void run() {
        boolean ok = true;
       while(ok) {
         try {
           System.out.println(in.readLine());           
         }
         catch (Exception e) {
           System.out.println("Whoops: " + e.getMessage());
           ok = false;
         }
       } 
      }
    });
    readerThread.setDaemon(true);
    readerThread.start();
    out.write("clear\n".getBytes());

    // !!!! CHANGE THIS LINE TO POINT AT A FILE YOU WANT TO PLAY !!!!
    out.write("add /Users/brian/Desktop/brittlestars.mov\n".getBytes());

    out.write("stop\n".getBytes());
    out.write("info\n".getBytes());
    out.write("get_length\n".getBytes());
    out.write("get_time\n".getBytes());
    out.write("get_title\n".getBytes());
    Thread.sleep(200);
    out.write("is_playing\n".getBytes());
    out.write("play\n".getBytes());
    Thread.sleep(200);
    out.write("is_playing\n".getBytes());
    Thread.sleep(2000);
    out.write("get_time\n".getBytes());
    Thread.sleep(200);
    out.write("get_time\n".getBytes());
    Thread.sleep(200);
    out.write("pause\n".getBytes());
    out.close();
    in.close();
    socket.close();
    System.exit(0);
  }

}
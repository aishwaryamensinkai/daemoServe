package daemon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.net.httpserver.HttpServer;

import handler.AddressHandler;
import handler.OrderHandler;
import handler.UserHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DaemonServer {

	public static void main(String[] args) throws IOException {
        int port = 9002;
	    HttpServer server = null;
		server = HttpServer.create(new InetSocketAddress(port), 0);
	    System.out.println("Server started at PORT " + port);
	    server.createContext("/order", new OrderHandler());
	    server.createContext("/user", new UserHandler());
	    server.createContext("/address", new AddressHandler());
	    server.setExecutor(null);
	    server.start();
	    
	    new Timer().scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				BufferedReader reader,reader1,reader2;
                String line,line1,line2;
                StringBuffer responseContent=new StringBuffer();
                StringBuffer responseContent1=new StringBuffer();
                StringBuffer responseContent2=new StringBuffer();
                HttpURLConnection connection = null;
                HttpURLConnection connection1 = null;
                HttpURLConnection connection2 = null;
                try {
                URL url=new URL("http://localhost:9002/user");
                URL url1=new URL("http://localhost:9002/order");
                URL url2=new URL("http://localhost:9002/address");
                connection=(HttpURLConnection) url.openConnection();
                connection1=(HttpURLConnection) url1.openConnection();
                connection2=(HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection1.setRequestMethod("GET");
                connection1.setConnectTimeout(5000);
                connection1.setReadTimeout(5000);
                connection2.setRequestMethod("GET");
                connection2.setConnectTimeout(5000);
                connection2.setReadTimeout(5000);
                reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader1=new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                reader2=new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                while((line=reader.readLine())!=null){
                    responseContent.append(line);
                }
                while((line1=reader1.readLine())!=null){
                    responseContent1.append(line1);
                }
                while((line2=reader2.readLine())!=null){
                    responseContent2.append(line2);
                }
                reader.close();
                reader1.close();
                reader2.close();
                }
                catch(Exception exception) {
                	exception.getStackTrace();
                }
                connection.disconnect();
			}
	    }, 0, 5000);
	}
}
package handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.postgresql.util.PSQLException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import postgresql.PostgreConnection;

public class UserHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String SqlQuery = "Select * from users";
		try {
		Connection connection = new PostgreConnection().connect();
		CopyManager copManager = new CopyManager((BaseConnection) connection);
		File file = new File("/home/pelatro/eclipse-workspace/Server/src/user.csv");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		System.out.println("STARTING COPY");
		copManager.copyOut("COPY (" + SqlQuery + ") TO STDOUT WITH (FORMAT CSV,HEADER TRUE)", fileOutputStream);
		connection.close();
		System.out.println("COPY ENDED");
		}
		catch(PSQLException exception) {
			exception.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String res = "EXPORTED THE DATA";
		httpExchange.sendResponseHeaders(200, res.length());
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(res.getBytes());
		outputStream.close();
	}
}

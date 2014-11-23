package com.thalmic.myo.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thalmic.myo.Quaternion;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Stack;


public class Server  {

    static private ArrayList<String> queue = new ArrayList<String>();
    //private Stack<String> queue= new Stack<String>();

    static HttpServer server;


    static public void start(String endpoint, int port) throws Exception{
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(endpoint, new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static public void addStringToQueue(String string){
        queue.add(string);
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //String response = "This is the response";
            String response = "";
            if(queue.size()!=0) {
                response = queue.get(queue.size() - 1);
            }

            queue.remove(queue.size()-1);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}

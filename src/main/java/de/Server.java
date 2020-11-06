package de;

import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.webSocket;

/**
 * Created by Dinh-An on 25.01.2017.
 */
public class Server {
    public static void main(String[] args) {
        webSocket("/car", Simulator.class);
        port(8888);
        init();
    }
}

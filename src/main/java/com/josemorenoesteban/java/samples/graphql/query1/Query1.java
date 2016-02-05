package com.josemorenoesteban.java.samples.graphql.query1;

import static spark.Spark.*;

public class Query1 {
    public static void main(String[] args) {
        
        
        get("/hello", (request, response) -> {
            String body = request.body();
            return "Hello World";
        });

        
        // from http://sparkjava.com/documentation.html
        // all methods management
        get("/",     (req, res) ->  "" );
        
        post("/",    (req, res) ->  "" );
        put("/",     (req, res) ->  "" );
        delete("/",  (req, res) ->  "" );
        options("/", (req, res) ->  "" );
        
        // request parameters
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name"); 
        });
    }
}

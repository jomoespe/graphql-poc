package com.josemorenoesteban.java.samples.graphql.query1;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static spark.Spark.*;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * A GraphQL entry point for customers 
 * 
 * mime type for GraphQL: 'application/graphql'
 * curl -X POST http://localhost:8081/graphql
 * <p> 
 *   The queries:
 *   <pre>
 * query getHighScore { score }
 * query getHighScores(limit: 10) { score }
 *   </pre>
 * </p>
 */
public class Vehicles {
    static final String GRAPHQL_TYPE = "application/graphql";
    static final int    COUNT        = 0;
    
    public static void main(String[] args) {
        DataFetcher vehiclesFetcher = (environment) -> {
            System.out.printf("Data fetcher...\n");
            System.out.printf("\tArguments:\n");
            environment.getArguments().forEach((key, value) -> System.out.printf("\t\t%s=%s\n", key, value) );
            System.out.printf("\tFields:\n");
            environment.getFields().forEach((field) -> System.out.printf("\t\tfield=%s\n", field) );
            System.out.printf("\tSource=%s\n", environment.getSource());
            
            return new LinkedHashMap<Object, Object>() {{ put("id", 1); put("name", "toyota"); put("state", Boolean.TRUE); }};
        };
        
        GraphQLObjectType vehicleType = newObject()
            .name("Vehicle")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLString).build())
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString).build())
            .field(newFieldDefinition().name("state").type(Scalars.GraphQLString).build())
            .build();
        
        GraphQLFieldDefinition vehicleField = newFieldDefinition()
            .name("vehicle")
            .type(vehicleType)
            .dataFetcher(vehiclesFetcher)
            .build();

        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema()
            .query(newObject().name("RootVehicles").field(vehicleField).build())
            .build();


        // Configure server (port and filters)
        port(8081);
        after((request, response) -> { response.header("Content-Encoding", "gzip"); });

        post("/vehicles", GRAPHQL_TYPE, (request, response) -> {
            ExecutionResult execResult = new GraphQL(graphQLSchema).execute( request.body() );
            
            if (execResult.getErrors().isEmpty()) {
                response.status(200);
                response.type(GRAPHQL_TYPE);
                return execResult.getData();
            } else {
                response.status(500);
                return execResult.getErrors()
                    .stream()
                    .map(GraphQLError::getMessage)
                    .collect(Collectors.toList());
            }
        });
    }
}

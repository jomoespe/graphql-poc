package com.josemorenoesteban.java.samples.graphql.query1;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLEnumType.newEnum;;
import static graphql.schema.GraphQLObjectType.newObject;
import static spark.Spark.*;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLEnumType;
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
        DataFetcher vehiclesFetcher = (env) -> {
            // Query arguments
            System.out.printf("Data fetcher...\n");
            String id = env.getArgument("id");
            System.out.printf("\tid=%s\n", id);
            
            System.out.printf("Output...\n");
            env.getFields().forEach((field) -> {
                field.getSelectionSet().getSelections().forEach(System.out::println);
            });
            System.out.printf("\tSource=%s\n", env.getSource());
            return new LinkedHashMap<Object, Object>() {{ put("id", id); put("name", "toyota " + id); put("state", Boolean.TRUE); }};
        };
      
        GraphQLEnumType vehicleStateEnum = newEnum()
            .name("VechicleStatus")
            .value("FREE",     Boolean.TRUE, "")
            .value("RESERVED", Boolean.TRUE, "")
            .build();
        
        GraphQLObjectType vehicleType = newObject()
            .name("Vehicle")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLString).build())
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString).build())
            .field(newFieldDefinition().name("state").type(vehicleStateEnum).build())
            .build();

        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema()
            .query(newObject()
                    .name("oneVehicle")
                    .field(newFieldDefinition()
                        .name("Vehicle")
                        .type(vehicleType)
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLString).build())
                        .dataFetcher(vehiclesFetcher)
                        .build())
                    .build())
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

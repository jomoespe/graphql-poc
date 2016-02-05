package com.josemorenoesteban.java.samples.graphql.query1;

import graphql.ExecutionResult;
import graphql.GraphQL;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static spark.Spark.*;

import graphql.schema.GraphQLSchema;
import graphql.Scalars;
import graphql.schema.DataFetchingEnvironment;

/**
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
public class GetHighScores {
    static final String GRAPHQL_TYPE = "application/graphql";
    static final int    COUNT        = 0;
    
    public static void main(String[] args) {
        GraphQLSchema schema = GraphQLSchema.newSchema()
            .query(newObject()
                .name("RootQueryType")
                .field(newFieldDefinition()
                    .name("count")
                    .type(Scalars.GraphQLInt)
                    .description("the count!")
                    .dataFetcher((DataFetchingEnvironment environment) -> COUNT)
                    .build())
                .build())
            .build();

        // Configure server
        port(8081);
        after((request, response) -> { response.header("Content-Encoding", "gzip"); });
        
        post("/graphql", GRAPHQL_TYPE, (request, response) -> {
            String query = request.body();

            ExecutionResult execResult = new GraphQL(schema).execute(query);
            response.status(200);
            response.type(GRAPHQL_TYPE);
            return execResult.getData();
        });
    }
}

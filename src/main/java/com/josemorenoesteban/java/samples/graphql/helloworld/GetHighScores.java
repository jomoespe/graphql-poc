package com.josemorenoesteban.java.samples.graphql.helloworld;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import graphql.Scalars;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;

/**
 * Following... * https://medium.com/@clayallsopp/your-first-graphql-server-3c766ab4f0a2#.86ixurfuf
 * <p>
 * The queries:
 * <pre>
 * query getHighScore { score }
 * query getHighScores(limit: 10) { score }
 * </pre>
 * </p>
 */
public class GetHighScores {
    int count = 0;
    GraphQLSchema schema = GraphQLSchema.newSchema()
        .query(newObject()
            .name("RootQueryType")
            .field(newFieldDefinition()
                .name("count")
                .type(Scalars.GraphQLInt)
                .dataFetcher((DataFetchingEnvironment environment) -> count)
                .build())
            .build())
        .build();
}

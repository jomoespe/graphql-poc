package com.josemorenoesteban.java.samples.graphql.helloworld;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public class HelloWorld {
    public static void main(String...args) {
        GraphQLObjectType queryType = newObject()
                .name("helloWorldQuery")
                .field(newFieldDefinition()
                    .type(GraphQLString)
                    .name("hello")
                    .staticValue("world")
                    .build())
                .build();
        
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();

        ExecutionResult result = new GraphQL(schema).execute("{hello}");
        result.getErrors().stream().forEach( System.out::println );
        
        System.out.printf("result=%s\n", result.getData());
        // Prints: {hello=world}
    }
    
    
}

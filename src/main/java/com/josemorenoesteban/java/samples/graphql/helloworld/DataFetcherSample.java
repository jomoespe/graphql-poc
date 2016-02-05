package com.josemorenoesteban.java.samples.graphql.helloworld;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public class DataFetcherSample {
    static DataFetcher calculateComplicatedValue = (DataFetchingEnvironment environment) -> {
        // environment.getSource() is the value of the surrounding
        // object. In this case described by objectType

        System.out.printf("fetcher:\n");
        System.out.printf("\tenvironment:\n");
        environment.getArguments().forEach((key, value) -> {
            System.out.printf("\t\t key=%s, value=%s\n", key, value);
        });
        System.out.printf("\tfields:\n");
        environment.getFields().forEach((field) -> { System.out.printf("\t\tfield=%s\n", field); } );

        Object value = "world";
        return value;
    };


    public static void main(String...args) {
        GraphQLObjectType objectType = newObject()
            .name("ObjectType")
            .field(newFieldDefinition()
                .name("hello")
                .type(Scalars.GraphQLString)
                .dataFetcher(calculateComplicatedValue)
                .build())
            .build();        

        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(objectType)
                .build();

        ExecutionResult result = new GraphQL(schema).execute("{hello}");

        System.out.printf("result:\n");
        System.out.printf("\terrors:\n");
        result.getErrors().stream().forEach( System.out::println );
        
        System.out.printf("\tdata:\n");
        System.out.printf("\t\tresult class=%s\n", result.getData().getClass());
        System.out.printf("\t\tresult=%s\n", result.getData());
        
    }
    
    
    
}

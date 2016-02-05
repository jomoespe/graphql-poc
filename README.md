

https://github.com/andimarek/graphql-java


más cosas: https://medium.com/@clayallsopp/your-first-graphql-server-3c766ab4f0a2#.6r3b5kkvz (javascript)

## Test


query RootQueryType { count }

    curl -XPOST -d @query1.query -H "Content-Type:application/graphql" http://localhost:8081/graphql


query getHighScores(limit: 10) { score }

    curl -XPOST -d @query2.query -H "Content-Type:application/graphql" http://localhost:8081/graphql


Recupera la meta información
    {__schema { queryType { name, fields { name, description} }}}

    curl -XPOST -d @meta.query -H "Content-Type:application/graphql" http://localhost:8081/graphql


curl -XPOST -d @vehicle2.query -H "Content-Type:application/graphql" http://localhost:8081/vehicles

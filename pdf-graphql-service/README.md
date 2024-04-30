MUTATION: 

```graphql
mutation($prog: ProgInput!) {
    addProg(prog:$prog) {
        name
        type
            statements {
                name
                type
                args {
                    name
                    type
                }
            }
    }
}
```
MUTATION VARIABLE:

```graphql
{
    "prog":{
        "name": "sdasdfsdfsdfsdfsa",
        "type": "sdasda",
        "statements": [
        {
            "name": "stat1",
            "type": "statt1",
            "args":[{
                "name": "Arg03",
                "type": "003"
            },
            {
                "name": "Arg03",
                "type": "003"
            }
            ]
        }
    ]
    }
}
```

QUERY:

```graphql
query GetProg {
 getProg {
  name,
  type,
 	statements {
    name,
    type,
    args {
      name,
      type
    }
  }
}
}
```

SCHEDULED: 
```graphql
query {
  apps
}
```

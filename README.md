# footballdata

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/footballdata-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly
  JAX-RS)
- MongoDB with Panache ([guide](https://quarkus.io/guides/mongodb-panache)): Simplify your persistence code for MongoDB
  via the active record or the repository pattern
- REST resources for MongoDB with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate Jakarta REST
  resources for your MongoDB entities and repositories
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus
  REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it







show dbs

use namndb
use sportsdb

show collections

db.namnlista.find()


db.namnlista.updateMany(
{ odds: { $exists: true } },
[{ $set: { odds: { $toString: "$odds" } } }]
)

db.fixtures.updateMany({}, [
{
$replaceWith: {
$arrayToObject: {
$map: {
input: { $objectToArray: "$$ROOT" },
as: "field",
in: {
k: "$$field.k",
v: {
$cond: [
{ $eq: ["$$field.k", "_id"] },
"$$field.v",          // Rör inte _id fältet
{ $toString: "$$field.v" } // Konvertera allt annat till string
]
}
}
}
}
}
}
]);



namn1,namn2,odds,odds2
modo,timrå,2.5,1,5
giffarna,man city,,






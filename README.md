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



db.dropDatabase()

show collections

db.fixtures.find()

db.fixtures.createIndex({ key: 1 }, { unique: true })

db.tmp_fixtures.aggregate([
{
$addFields: {
// Kombinera kolumner till key (sträng)
key: { $concat: [ "$hometeam", ":", "$awayteam" ] }
}
},
{
$merge: {
into: "fixtures",
on: "key",
whenMatched: "keepExisting", // Hoppa över om ID:t redan finns
whenNotMatched: "insert"
}
}
])



/c//tools/mongodb-database-tools-windows-x86_64-100.17.0/bin/mongoimport.exe --uri="mongodb://localhost:27017" --db sportsdb --collection tmp_fixtures --type csv --headerline --file "/c/data/csv/fixture_simple.csv"

league,date,time,hometeam,awayteam,referee,b365h,b365d,b365a
B1,15/05/2026,19:45,Oud-Heverlee Leuven,Antwerp,xxx,2.7,3.2,2.45
B1,16/05/2026,15:00,Charleroi,Westerlo,yyy,1.95,3.5,3.25
D1,16/05/2026,14:30,Eintracht Frankfurt,Stuttgart,,2.7,4,2.3,2.63







![Scala CI](https://github.com/flyway/flyway-sbt/workflows/Scala%20CI/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.davidmweber/flyway-sbt.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.github.davidmweber%22%20AND%20a%3A%22flyway-sbt%22)

## Sbt 1.x plugin for [Flyway](https://flywaydb.org)

Welcome to the home for the `sbt` v1.x plugin for flyway. The [user manual](https://davidmweber.github.io/flyway-sbt-docs/)
will tell you how to get started. This project is based on the original 
[flyway-sbt](https://github.com/flyway/flyway/tree/flyway-4.2.0/flyway-sbt) that was in the flyway repository through 
version 4.2.1.

## Maintenance
This repository is a community project and not officially maintained by the Flyway Team at Redgate.
This project is looked after only by the open source community. Community Maintainers are people who have agreed to be contacted with queries for support and maintenance.
Community Maintainers: 
- [@davidmweber](https://github.com/davidmweber)

If you would like to be named as an Community Maintainer, let us know via Twitter: https://twitter.com/flywaydb.


### Getting started
Adding Flyway to your build is very easy. First, update to your `project/plugin.sbt` file to include:
```sbtshell
 addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "7.4.0")
```
Please check out [Maven Central](https://search.maven.org/search?q=g:io.github.davidmweber%20AND%20a:flyway-sbt&core=gav) 
for versions of `flyway-sbt` for previous versions of Flyway.

Edit `build.sbt` to enable the plugin and configure the database access:
```sbtshell
enablePlugins(FlywayPlugin)
version := "0.0.1"
name := "flyway-sbt-test1"

libraryDependencies += "org.hsqldb" % "hsqldb" % "2.5.0"

flywayUrl := "jdbc:hsqldb:file:target/flyway_sample;shutdown=true"
flywayUser := "SA"
flywayPassword := ""
flywayLocations := Seq("filesystem:src/main/resources/db/migration")
flywayUrl in Test := "jdbc:hsqldb:file:target/flyway_sample;shutdown=true"
flywayUser in Test := "SA"
flywayLocations in Test := Seq("filesystem:src/main/resources/db/migration")
flywayPassword in Test := ""

```

Migrate your database using `sbt flywayMigrate` or clean it using `sbt flywayClean`.

Note that the `flywayTarget` setting has been disabled due to [this bug](https://github.com/flyway/flyway/issues/1919).

### Building and testing
Build and testing uses `sbt` and it's plugin [testing framework](http://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html). 
The test cases are pretty basic (hint: we need more of those). There is no support for `sbt` prior to 1.0. Use the 
[legacy plugin](https://github.com/flyway/flyway/tree/master/flyway-sbt) instead.

Note that from v5.0.0 onwards, the plugin has to be explicitly enabled using `enablePlugins(FlywayPlugin)`. This prevents
Flyway actions triggering unrelated build activity and addresses [this issue](https://github.com/flyway/flyway/issues/1329).

Build and test the plugin using

```bash
sbt scripted
```

Early adopters should just publish a clone or fork of this repository locally:
```bash
git clone https://github.com/flyway/flyway-sbt.git
cd flyway-sbt
sbt publishLocal
```

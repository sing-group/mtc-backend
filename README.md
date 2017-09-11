# MultiTasking Cubes

## Running the application
The application has been configured to be easily run locally, by just invoking
a Maven command.

To do so, Maven will download (if it is not already) a clean WildFly
distribution to the `target` folder, configure it, start it and deploy the
application on it.

This makes very easy and straightforward to manually test the application.

### Configure a local MySQL
To execute the application you need a MySQL server running in `localhost` and
using the default port (3306).

In this server you have to create a database named `mtc` accessible for the
`mtc` user using the `mtcpass` password.

This can be configured executing the follow SQL sentences in your MySQL:

```SQL
CREATE DATABASE mtc;
GRANT ALL ON mtc.* TO mtc@localhost IDENTIFIED BY 'mtcpass';
FLUSH PRIVILEGES;
```

Of course, this configuration can be changed in the POM file.

### Building the application
The application can be built with the following Maven command:

```
mvn clean install
```

This will build the application launching the tests on a **Wildfly 8.2.1**
server. If you want to use a **WildFly 10.1.0** server, you can use the command:

```
mvn clean install -P wildfly10-embedded-h2,-wildfly-embedded-h2
```

### Starting the application
The application can be started with the following Maven command:

```
mvn package wildfly:start wildfly:deploy-only -P wildfly-mysql-run,-wildfly-embedded-h2,-metadata-build
```

This will start a **WildFly 8.2.1**. If you want to use a **WildFly 10.1.0**
server, you can use the command:

```
mvn package wildfly:start wildfly:deploy-only -P wildfly10-mysql-run,-wildfly-embedded-h2,-metadata-build
```

### Restarting the application
Once it is running, the application can be re-started with the following Maven
command:

```
mvn wildfly:shutdown wildfly:start wildfly:deploy-only -P wildfly-mysql-run,-wildfly-embedded-h2,-metadata-build
```

In case you are using the **WildFly 10.1.0** version, you should use the
following command instead:

```
mvn wildfly:shutdown wildfly:start wildfly:deploy-only -P wildfly10-mysql-run,-wildfly-embedded-h2,-metadata-build
```

### Stopping the application
The application can be stopped with the following Maven command:

```
mvn wildfly:shutdown
```

### REST API documentation
The REST API is documented using the [Swagger](https://swagger.io/) framework.
It can be browsed using the [Swagger UI](http://petstore.swagger.io/)
application to access the following URL:

```
http://localhost:8080/mtc/rest/api/swagger.json
```
## Introduction

Webtracking is a Spring Boot application to show the basics of webtracking. 
While it shows the basics it is far from production-ready. 

### Requirements
You will need 
- Java 8 or higher
- Maven 
- curl and jq to get reports from the command line

This application has been developed and tested on MacOS only. 

### Test and  run 

```
mvn test spring-boot:run
```


The application starts on port 8080. To change that either change port in `src/main/resources/application.properties`
or start the app with the following command

`mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080` 

### Demo site
A simple demo site comes with the application. 
It can be accessed from `http://localhost:8080/`. Please click around on the links and then watch the result in the reports.

### Report
Two different reports are available. A full report which contains every tracking object. And a summary that shows an aggregated view of views and visitors per url. 

The reports are available on 
```
http://localhost:8080/summary
http://localhost:8080/allVisits
```

Both reports takes optional parameters `after` and `before` to specify timerange for report.
```
http://localhost:8080/summary?after=2020-05-15T09:00&before2020-05-15T12:00
http://localhost:8080/allVisits?after=2020-05-15T09:00&before2020-05-15T12:00
```

### Report CLI
A simple report CLI has been created in `report.sh`. It uses `curl` and `jq` to query and format the data from the server. 
The report collects all data, it does not limit the data from after and before date as described above. 

Get the report by running:

`./report.sh`
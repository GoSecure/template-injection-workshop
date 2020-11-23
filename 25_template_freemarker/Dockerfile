FROM frolvlad/alpine-java
VOLUME /tmp

RUN set -x \
    && mkdir /secret \
    && printf 'Got it !\nflag-9BD7432EFBC7790EB57FAFF10D9F0880' >> /secret/flag.txt \
    && chmod 777 /secret/flag.txt

COPY target/freemarker-sandbox-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

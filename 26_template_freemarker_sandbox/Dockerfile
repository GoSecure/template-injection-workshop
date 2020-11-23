FROM frolvlad/alpine-java
VOLUME /tmp

RUN set -x \
    && mkdir /secret \
    && printf 'Got it !\nflag-3AE78AB434058FBD930963D7620B9DEF' >> /secret/flag.txt \
    && chmod 777 /secret/flag.txt

COPY target/freemarker-sandbox-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

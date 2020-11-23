FROM frolvlad/alpine-java
VOLUME /tmp

RUN set -x \
    && mkdir /secret \
    && printf 'Got it !\nflag-440d6ea7d99a370a2242586f31c1fcd5' >> /secret/flag.txt \
    && chmod 777 /secret/flag.txt

COPY build/libs/emailcamp-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

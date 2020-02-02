#FROM adoptopenjdk/openjdk8
FROM alpine:3.11

#RUN apt update && apt install -y git
RUN apk add --update-cache \
    openjdk8 \
    git 

# COPY gradlew build.gradle ./
COPY . .

RUN ./gradlew cli

# COPY . .

# ENV embulk "/bin/sh ./build/executable/embulk-0.9.23-SNAPSHOT.jar"

RUN cp ./build/executable/embulk-0.9.23-SNAPSHOT.jar /bin/embulk

RUN embulk gem install googleauth -v 0.10.0


CMD ["java", "-jar", "/bin/embulk"]

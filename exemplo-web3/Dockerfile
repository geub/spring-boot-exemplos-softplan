FROM andreptb/oracle-java:8
EXPOSE 8080
ENV JAVA_OPTS -Duser.language=pt -Duser.country=BR
COPY build/libs/exemplo-web3-1.0.0.jar exemplo-web3.jar
ENTRYPOINT ["java", "-jar", "exemplo-web3.jar"]

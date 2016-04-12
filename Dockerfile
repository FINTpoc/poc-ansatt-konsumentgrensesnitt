FROM java:8
ADD build/libs/ansatt-konsumentgrensesnitt-poc-*.jar /data/app.jar
CMD ["java", "-jar", "/data/app.jar"]
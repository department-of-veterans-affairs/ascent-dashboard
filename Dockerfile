FROM java:8

ADD target/ascent-dashboard-*.jar /ascent-dashboard.jar
ENTRYPOINT ["java", "-Xms32m", "-Xmx256m", "-jar", "/ascent-dashboard.jar"]

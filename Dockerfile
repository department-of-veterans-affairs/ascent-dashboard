FROM jluck/ascent-base

ENV JAR_FILE "/ascent-dashboard.jar"
ADD target/ascent-dashboard-*.jar $JAR_FILE
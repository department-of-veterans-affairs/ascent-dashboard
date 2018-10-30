FROM ascent/ascent-base

ENV JAR_FILE "/ascent-dashboard.jar"
ADD target/ascent-dashboard.jar $JAR_FILE

# Append app specific secrets to load to the base config
RUN echo \
'secret { \
    format = "ascent.dashboard.{{ key }}" \
    no_prefix = true \
    path = "secret/ascent-dashboard" \
}' >> $ENVCONSUL_CONFIG

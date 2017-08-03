package gov.va.ascent.dashboard;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * Created by vgadda on 4/4/17.
 */
@ConditionalOnProperty("turbine.enabled")
@EnableTurbine
public class TurbineConfiguration {
}

package gov.va.ascent.dashboard.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Service not found in discovery service")
public class ServiceNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceNotFoundException.class);

    public ServiceNotFoundException(final String message){
        super();
        LOGGER.error(message, this);
    }

}

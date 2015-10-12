package org.mifosplatform.portfolio.group.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class InvalidEmailAdressException extends AbstractPlatformResourceNotFoundException {
	 public InvalidEmailAdressException(final String id) {
	        super("error.msg.email.id.invalid", "Group with email" +id+ " invalid", id);
	    }

}

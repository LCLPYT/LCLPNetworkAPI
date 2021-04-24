/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.util;

/**
 * Thrown when an error occurs within an API request.
 */
public class APIException extends RuntimeException {

    public static final APIException NO_CONNECTION = new APIException("Connection to the server could not be established.");

    private static final long serialVersionUID = -4018512217545193467L;

    public APIException(String message) {
        super(message);
    }

    public APIException(Throwable cause) {
        super(cause);
    }
}

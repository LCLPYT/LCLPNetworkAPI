/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ResponseEvaluationException extends APIException {

    private static final long serialVersionUID = -2567210191139777783L;
    private final APIResponse response;

    public ResponseEvaluationException(APIResponse response) {
        this("Unable to evaluate response", response);
    }

    public ResponseEvaluationException(String message, APIResponse response) {
        super(message);
        this.response = Objects.requireNonNull(response);
    }

    public ResponseEvaluationException(Throwable cause, APIResponse response) {
        super(cause);
        this.response = Objects.requireNonNull(response);
    }

    @Nonnull
    public APIResponse getResponse() {
        return response;
    }

    /**
     * Tries to get the {@link APIResponse} from a {@link Throwable}'s cause.
     *
     * @param throwable The throwable to extract from it's cause.
     * @return The {@link APIResponse} that caused the exception's cause, or null, if there was no {@link APIResponse} (another exception or similar).
     */
    @Nullable
    public static APIResponse getResponseFromCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        if(throwable.getCause() == null) return null;
        else return getResponseFrom(throwable.getCause());
    }

    /**
     * Tries to get the {@link APIResponse} from a {@link Throwable}.
     *
     * @param throwable  The throwable to get the response from.
     * @return The {@link APIResponse} that caused the exception.
     */
    @Nullable
    public static APIResponse getResponseFrom(Throwable throwable) {
        Objects.requireNonNull(throwable);
        if(!(throwable instanceof ResponseEvaluationException)) return null;
        return ((ResponseEvaluationException) throwable).getResponse();
    }

}

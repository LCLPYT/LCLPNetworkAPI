/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api.annotation;

import java.lang.annotation.*;

/**
 * Used to mark API request methods that require specific scopes.
 * The required scopes can be checked with {@link Scopes#value()}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scopes {

    /**
     * Get a list of scopes required to call this API method.
     * @return An array of scope strings.
     */
    String[] value();

}

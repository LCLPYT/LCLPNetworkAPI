/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.network;

import java.util.List;
import java.util.Objects;

public class APIError {

    private final List<ElementError> violations;

    public APIError(List<ElementError> violations) {
        this.violations = Objects.requireNonNull(violations);
    }

    public List<ElementError> getViolations() {
        return violations;
    }

    public boolean has(String element, String error) {
        ElementError err = null;
        for (ElementError e : violations) {
            if (e.getElement().equals(element)) {
                err = e;
                break;
            }
        }
        if (err == null) return false;

        for (String s : err.getErrors()) {
            if (s.equalsIgnoreCase(error)) return true;
        }
        return false;
    }

    public String getFirst() {
        if (violations.isEmpty()) return "error";

        ElementError eErr = violations.get(0);
        if (eErr == null) return "error";

        List<String> errs = eErr.getErrors();
        if (errs.isEmpty()) return "error";

        return errs.get(0);
    }

    @Override
    public String toString() {
        return violations.toString();
    }

}

package org.jenkinsci.plugins.buildtriggerbadge;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.Cause;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Filtering possible duplicate causes
 *
 * @since 1.1
 *
 * @author Michael Pailloncy
 * @author ljader
 */
public class CauseFilter {

    /**
     * Filter causes by Class type and description
     *
     * @param inputCauses list of causes
     * @return filtered list, possibly empty, never null
     */
    @NonNull
    public static List<Cause> filter(@Nullable List<Cause> inputCauses) {
        if (inputCauses == null) {
            return List.of();
        }

        return inputCauses.stream().filter(distinctCause()).toList();
    }

    private static Predicate<Cause> distinctCause() {
        Set<String> seen = new HashSet<>();
        return c -> seen.add(getCauseFilter(c));
    }

    private static String getCauseFilter(Cause cause) {
        return cause.getClass().getCanonicalName() + "_" + cause.getShortDescription();
    }
}

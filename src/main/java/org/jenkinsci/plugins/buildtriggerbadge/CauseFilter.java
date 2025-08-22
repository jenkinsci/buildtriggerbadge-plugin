package org.jenkinsci.plugins.buildtriggerbadge;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.Cause;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeDeactivator;

/**
 * Filtering possible duplicate causes
 *
 * @see BuildTriggerBadgeDeactivator
 * @since 1.1
 *
 * @author Michael Pailloncy
 * @author ljader
 */
public class CauseFilter {

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(CauseFilter.class.getName());

    /**
     * Filter causes by Class type and description.
     *
     * @param inputCauses list of causes
     * @return filtered list, possibly empty, never {@code null}
     */
    @NonNull
    public static List<Cause> filter(@Nullable List<Cause> inputCauses) {
        return filter(inputCauses, null);
    }

    /**
     * Filter causes by excluding duplicate cause type and descriptions
     * and deactivated badge causes.
     *
     * @param inputCauses list of causes
     * @param deactivators list of cause deactivators, possibly {@code null}
     * @return filtered list, possibly empty, never {@code null}
     */
    @NonNull
    public static List<Cause> filter(
            @Nullable List<Cause> inputCauses, @Nullable List<BuildTriggerBadgeDeactivator> deactivators) {
        if (inputCauses == null) {
            return List.of();
        }

        return inputCauses.stream()
                .filter(distinctCause())
                .filter(notDeactivated(deactivators))
                .toList();
    }

    private static Predicate<Cause> distinctCause() {
        Set<String> seen = new HashSet<>();
        return c -> seen.add(getCauseFilter(c));
    }

    private static String getCauseFilter(Cause cause) {
        return cause.getClass().getCanonicalName() + "_" + cause.getShortDescription();
    }

    private static Predicate<Cause> notDeactivated(@Nullable List<BuildTriggerBadgeDeactivator> deactivators) {
        if (CollectionUtils.isEmpty(deactivators)) {
            return cause -> true;
        }

        return cause -> {
            for (BuildTriggerBadgeDeactivator deactivator : deactivators) {
                if (deactivator.vetoBadge(cause)) {
                    LOGGER.log(Level.FINE, "Badge for cause '{0}' disabled by extension '{1}'", new Object[] {
                        cause, deactivator
                    });
                    return false;
                }
            }

            return true;
        };
    }
}

package org.jenkinsci.plugins.buildtriggerbadge.provider;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Cause;

/**
 * Extend this class if you want to be able to disable the badge for a given cause globally.
 *
 * @author Baptiste Mathus
 */
public abstract class BuildTriggerBadgeDeactivator implements ExtensionPoint {

    /**
     * Should return true to disable a BuildTriggerBadge for a given cause.
     * @param cause the cause whose disabling is in question.
     * @return true if the cause must not have a badge, else false.
     */
    public abstract boolean vetoBadge(Cause cause);

    public static ExtensionList<BuildTriggerBadgeDeactivator> all() {
        return ExtensionList.lookup(BuildTriggerBadgeDeactivator.class);
    }
}

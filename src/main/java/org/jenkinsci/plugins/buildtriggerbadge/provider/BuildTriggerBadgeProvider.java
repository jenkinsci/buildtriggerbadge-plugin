package org.jenkinsci.plugins.buildtriggerbadge.provider;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Cause;

/**
 * Extend this class to be able to define/override an association between a class and an icon. This will then add that icon as a badge in
 * the build history side panel.
 *
 * <p>
 * Note : if many providers are found, the first one matching a given cause class will be used.
 * </p>
 *
 * @see <a
 *      href="https://wiki.jenkins-ci.org/display/JENKINS/Build+Trigger+Badge+Plugin#BuildTriggerBadgePlugin-Screenshotbadges">BuildTriggerBadge
 *      screenshot as an example</a>
 *
 * @author Baptiste Mathus
 */
public abstract class BuildTriggerBadgeProvider implements ExtensionPoint {

    /**
     * Should return the <a href="https://weekly.ci.jenkins.io/design-library/symbols/">Jenkins Symbol</a>
     * or URL to the icon corresponding to the given cause.
     *
     * @return the symbol or URL of the icon for that cause, null if none provided.
     */
    @CheckForNull
    public abstract String provideIcon(@NonNull Cause cause);

    public static ExtensionList<BuildTriggerBadgeProvider> all() {
        return ExtensionList.lookup(BuildTriggerBadgeProvider.class);
    }
}

package org.jenkinsci.plugins.buildtriggerbadge;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.BuildBadgeAction;
import hudson.model.Cause;
import hudson.model.Run;
import java.util.List;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeDeactivator;

/**
 * Badge action of the build trigger cause.
 *
 * @author Michael Pailloncy
 */
public class BuildTriggerBadgeAction implements BuildBadgeAction {

    private final Cause cause;

    /**
     * Factory method to create build trigger badge actions for the run causes.
     * Run causes will be deduped and filtered for deactivated badge causes.
     * This method only creates new action instances, it does not add them
     * to the run.
     * @see CauseFilter
     * @see BuildTriggerBadgeDeactivator
     * @param run run instance to get build trigger actions for
     * @return new list of build trigger badge actions for the run causes
     */
    @NonNull
    static List<BuildTriggerBadgeAction> createForRun(@NonNull Run<?, ?> run) {
        return CauseFilter.filter(run.getCauses(), BuildTriggerBadgeDeactivator.all()).stream()
                .map(BuildTriggerBadgeAction::new)
                .toList();
    }

    /**
     * Initialize causes of the build.
     */
    public BuildTriggerBadgeAction(@NonNull Cause cause) {
        this.cause = cause;
    }

    public String getTooltip() {
        return cause.getShortDescription();
    }

    // used by jelly
    public static BuildTriggerBadgePlugin getPlugin() {
        return Jenkins.get().getPlugin(BuildTriggerBadgePlugin.class);
    }

    // non use interface methods
    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "Trigger " + cause.getClass().getSimpleName() + " : " + getTooltip();
    }

    public String getUrlName() {
        return "";
    }

    /**
     * Returns the icon to be used as a badge for the given cause that triggered the associated build.
     *
     * @return the associated icon for the given cause.
     */
    public String getIcon() {
        return new IconFinder(cause).find();
    }
}

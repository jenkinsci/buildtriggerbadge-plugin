package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.cli.BuildCommand.CLICause;
import hudson.model.Cause;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.model.Cause.UserIdCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeProvider;

/**
 * Class responsible for finding the icon associated with a build cause.
 *
 * @author Baptiste Mathus
 */
public class IconFinder {

    private static final Logger LOGGER = Logger.getLogger(IconFinder.class.getSimpleName());

    private final Cause cause;

    public IconFinder(Cause cause) {
        this.cause = cause;
    }

    public String find() {

        // Searching for externally defined cause/icon associations
        for (BuildTriggerBadgeProvider provider : BuildTriggerBadgeProvider.all()) {
            String providedIcon = provider.provideIcon(cause);
            if (providedIcon != null) {
                LOGGER.log(Level.FINEST, "Badge for cause '{0}' set/overriden by extension '{1}': '{2}'", new Object[] {
                    cause, provider.getClass().getSimpleName()
                });
                return providedIcon;
            }
        }

        // internal lookup if nothing found previously
        Class<?> clazz = cause.getClass();
        return internalFindForClass(clazz);
    }

    private String internalFindForClass(Class<?> clazz) {
        if (clazz == null) {
            return "symbol-help-outline plugin-ionicons-api";
        }

        String path = DEFAULT_ICONS.get(clazz.getName());
        if (path == null) {
            return internalFindForClass(clazz.getSuperclass());
        }

        return path;
    }

    private static final Map<String, String> DEFAULT_ICONS = new HashMap<>();

    static {
        defineIconForCause(UserIdCause.class, "symbol-person-outline plugin-ionicons-api");
        defineIconForCause(UserCause.class, "symbol-person-outline plugin-ionicons-api");
        defineIconForCause(TimerTriggerCause.class, "symbol-time-outline plugin-ionicons-api");
        defineIconForCause(SCMTriggerCause.class, "symbol-scm-cause plugin-buildtriggerbadge");
        defineIconForCause(UpstreamCause.class, "symbol-arrow-up-outline plugin-ionicons-api");
        defineIconForCause(CLICause.class, "symbol-terminal-outline plugin-ionicons-api");
        defineIconForCause(RemoteCause.class, "symbol-radio-outline plugin-ionicons-api");
        defineIconForCause(
                "org.jvnet.hudson.plugins.m2release.ReleaseCause", "symbol-person-outline plugin-ionicons-api");
        defineIconForCause("com.cloudbees.jenkins.GitHubPushCause", "symbol-logo-github plugin-ionicons-api");
        defineIconForCause("org.jenkinsci.plugins.ghprb.GhprbCause", "symbol-github-pull-request plugin-ionicons-api");
        defineIconForCause(
                "org.jenkinsci.plugins.github.pullrequest.GitHubPRCause",
                "symbol-github-pull-request plugin-ionicons-api");
        defineIconForCause(
                "com.cloudbees.jenkins.plugins.github_pull.GitHubPullRequestCause",
                "symbol-github-pull-request plugin-ionicons-api");
        defineIconForCause(
                "stashpullrequestbuilder.stashpullrequestbuilder.StashCause",
                "symbol-github-pull-request plugin-ionicons-api");
        defineIconForCause(
                "io.jenkins.plugins.gitlabbranchsource.GitLabMergeRequestCommentCause",
                "symbol-chatbox-outline plugin-ionicons-api");
        defineIconForCause(
                "org.jenkinsci.plugins.periodicreincarnation.PeriodicReincarnationBuildCause",
                "symbol-refresh-outline plugin-ionicons-api");
        defineIconForCause(
                "com.chikli.hudson.plugin.naginator.NaginatorCause", "symbol-refresh-outline plugin-ionicons-api");
        defineIconForCause("com.cloudbees.plugins.flow.FlowCause", "symbol-flow-cause plugin-buildtriggerbadge");
        defineIconForCause(
                "com.cloudbees.jenkins.plugins.BitBucketPushCause", "symbol-logo-bitbucket plugin-ionicons-api");
        defineIconForCause("hudson.plugins.git.GitStatus$CommitHookCause", "symbol-git plugin-buildtriggerbadge");
        defineIconForCause(
                "org.jenkinsci.plugins.urltrigger.URLTriggerCause", "symbol-link-outline plugin-ionicons-api");
        defineIconForCause("org.jenkinsci.plugins.gwt.GenericCause", "symbol-link-outline plugin-ionicons-api");
        defineIconForCause(
                "jenkins.branch.BranchIndexingCause", "symbol-branch-indexing-cause plugin-buildtriggerbadge");
        defineIconForCause("jenkins.branch.BranchEventCause", "symbol-git-branch-outline plugin-ionicons-api");
        defineIconForCause(
                "org.jenkinsci.plugins.workflow.cps.replay.ReplayCause", "symbol-refresh-outline plugin-ionicons-api");
        defineIconForCause("org.jenkinsci.lib.xtrigger.XTriggerCause", "symbol-xtrigger plugin-buildtriggerbadge");
        defineIconForCause(
                "com.cloudbees.workflow.cps.checkpoint.RestoreFromCheckpointCause",
                "symbol-location-outline plugin-ionicons-api");
        defineIconForCause(
                "org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTriggerCause",
                "symbol-time-outline plugin-ionicons-api");
    }

    private static void defineIconForCause(Class<?> clazz, String path) {
        defineIconForCause(clazz.getName(), path);
    }

    private static void defineIconForCause(String className, String path) {
        DEFAULT_ICONS.put(className, path);
    }
}

package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.PluginManager;
import hudson.PluginWrapper;
import hudson.cli.BuildCommand.CLICause;
import hudson.model.Cause;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.model.Cause.UserIdCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for finding the icon associated with a build cause.
 * 
 * @author Baptiste Mathus
 */
public class IconFinder {

	private static final Logger LOGGER = Logger.getLogger(IconFinder.class.getSimpleName());

	private static final String IMAGES_PATH;

	private Cause cause;

	public IconFinder(Cause cause) {
		this.cause = cause;
	}

	public String find() {

		// Searching for externally defined cause/icon associations
		for (BuildTriggerBadgeProvider provider : BuildTriggerBadgeProvider.all()) {
			String providedIcon = provider.provideIcon(cause);
			if (providedIcon != null) {
				LOGGER.log(Level.FINEST,
				           "Badge for cause '{0}' set/overridden by extension '{1}': '{2}'",
				           new Object[]{cause, provider.getClass().getSimpleName()});
				return providedIcon;
			}
		}

		// internal lookup if nothing found previously
		Class<?> clazz = cause.getClass();
		return internalFindForClass(clazz);
	}

	private String internalFindForClass(Class clazz)
	{
		if (clazz == null) {
			return getIconPath("fallback-cause.svg");
		}
		String path = DEFAULT_ICONS.get(clazz.getName());
		if (path == null) {
			return internalFindForClass(clazz.getSuperclass());
		}
		return path;
	}

	private static final Map<String, String> DEFAULT_ICONS = new HashMap<String, String>();
	static {

		IMAGES_PATH = "/plugin/" + BuildTriggerBadgePlugin.get().getShortName() + "/images/";

		defineIconForCause(UserIdCause.class, "user-cause.svg");
		defineIconForCause(UserCause.class, "user-cause.svg");
		defineIconForCause(TimerTriggerCause.class, "timer-cause.svg");
		defineIconForCause(SCMTriggerCause.class, "scm-cause.svg");
		defineIconForCause(UpstreamCause.class, "upstream-cause.svg");
		defineIconForCause(CLICause.class, "cli-cause.svg");
		defineIconForCause(RemoteCause.class, "remote-cause.svg");
		defineIconForCause("org.jvnet.hudson.plugins.m2release.ReleaseCause", "user-cause.svg");
		defineIconForCause("com.cloudbees.jenkins.GitHubPushCause", "github-push-cause.svg");
		defineIconForCause("org.jenkinsci.plugins.ghprb.GhprbCause", "github-pull-request-cause.svg");
		defineIconForCause("org.jenkinsci.plugins.github.pullrequest.GitHubPRCause", "github-pull-request-cause.svg");
		defineIconForCause("com.cloudbees.jenkins.plugins.github_pull.GitHubPullRequestCause", "github-pull-request-cause.svg");
		defineIconForCause("stashpullrequestbuilder.stashpullrequestbuilder.StashCause", "github-pull-request-cause.svg");
		defineIconForCause("io.jenkins.plugins.gitlabbranchsource.GitLabMergeRequestCommentCause", "gitlab-merge-request-comment.svg");
		defineIconForCause("org.jenkinsci.plugins.periodicreincarnation.PeriodicReincarnationBuildCause", "periodic-reincarnation.svg");
		defineIconForCause("com.chikli.hudson.plugin.naginator.NaginatorCause", "periodic-reincarnation.svg");
		defineIconForCause("com.cloudbees.plugins.flow.FlowCause", "flow-cause.svg");
		defineIconForCause("com.cloudbees.jenkins.plugins.BitBucketPushCause", "bitbucket.svg");
		defineIconForCause("hudson.plugins.git.GitStatus$CommitHookCause", "git-hook-cause.svg");
		defineIconForCause("org.jenkinsci.plugins.urltrigger.URLTriggerCause", "url-trigger-cause.svg");
		defineIconForCause("org.jenkinsci.plugins.gwt.GenericCause", "url-trigger-cause.svg");
		defineIconForCause("jenkins.branch.BranchIndexingCause", "branch-indexing-cause.svg");
		defineIconForCause("jenkins.branch.BranchEventCause", "branch-event-cause.svg");
		defineIconForCause("org.jenkinsci.plugins.workflow.cps.replay.ReplayCause", "periodic-reincarnation.svg");
		defineIconForCause("org.jenkinsci.lib.xtrigger.XTriggerCause", "xtrigger.png");
		defineIconForCause("com.cloudbees.workflow.cps.checkpoint.RestoreFromCheckpointCause", "checkpoint.svg");
		defineIconForCause("org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTriggerCause", "timer-cause.svg");
	}

	private static void defineIconForCause(Class clazz, String path) {
		defineIconForCause(clazz.getName(), path);
	}

	private static void defineIconForCause(String className, String path) {
		DEFAULT_ICONS.put(className, getIconPath(path));
	}

	private static String getIconPath(String iconName) {

		return IMAGES_PATH + iconName;
	}
}

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
				           "Badge for cause '{0}' set/overriden by extension '{1}': '{2}'",
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
			return getIconPath("fallback-cause.png");
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

		defineIconForCause(UserIdCause.class, "user-cause.png");
		defineIconForCause(UserCause.class, "user-cause.png");
		defineIconForCause(TimerTriggerCause.class, "timer-cause.png");
		defineIconForCause(SCMTriggerCause.class, "scm-cause.png");
		defineIconForCause(UpstreamCause.class, "upstream-cause.png");
		defineIconForCause(CLICause.class, "cli-cause.png");
		defineIconForCause(RemoteCause.class, "remote-cause.png");
		defineIconForCause("org.jvnet.hudson.plugins.m2release.ReleaseCause", "user-cause.png");
		defineIconForCause("com.cloudbees.jenkins.GitHubPushCause", "github-push-cause.png");
		defineIconForCause("org.jenkinsci.plugins.ghprb.GhprbCause", "github-pull-request-cause.png");
		defineIconForCause("org.jenkinsci.plugins.github.pullrequest.GitHubPRCause", "github-pull-request-cause.png");
		defineIconForCause("com.cloudbees.jenkins.plugins.github_pull.GitHubPullRequestCause", "github-pull-request-cause.png");
		defineIconForCause("stashpullrequestbuilder.stashpullrequestbuilder.StashCause", "github-pull-request-cause.png");
		defineIconForCause("io.jenkins.plugins.gitlabbranchsource.GitLabMergeRequestCommentCause", "gitlab-merge-request-comment.png");
		defineIconForCause("com.adobe.jenkins.github_pr_comment_build.GitHubPullRequestCommentCause", "gitlab-merge-request-comment.png");
		defineIconForCause("org.jenkinsci.plugins.periodicreincarnation.PeriodicReincarnationBuildCause", "periodic-reincarnation.png");
		defineIconForCause("com.chikli.hudson.plugin.naginator.NaginatorCause", "periodic-reincarnation.png");
		defineIconForCause("com.cloudbees.plugins.flow.FlowCause", "flow-cause.png");
		defineIconForCause("com.cloudbees.jenkins.plugins.BitBucketPushCause", "bitbucket.png");
		defineIconForCause("hudson.plugins.git.GitStatus$CommitHookCause", "git-hook-cause.png");
		defineIconForCause("org.jenkinsci.plugins.urltrigger.URLTriggerCause", "url-trigger-cause.png");
		defineIconForCause("org.jenkinsci.plugins.gwt.GenericCause", "url-trigger-cause.png");
		defineIconForCause("jenkins.branch.BranchIndexingCause", "branch-indexing-cause.png");
		defineIconForCause("jenkins.branch.BranchEventCause", "branch-event-cause.png");
		defineIconForCause("org.jenkinsci.plugins.workflow.cps.replay.ReplayCause", "periodic-reincarnation.png");
		defineIconForCause("org.jenkinsci.lib.xtrigger.XTriggerCause", "xtrigger.png");
		defineIconForCause("com.cloudbees.workflow.cps.checkpoint.RestoreFromCheckpointCause", "checkpoint.png");
		defineIconForCause("org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTriggerCause", "timer-cause.png");
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

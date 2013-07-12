package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.PluginWrapper;
import hudson.cli.BuildCommand.CLICause;
import hudson.model.BuildBadgeAction;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.model.Cause.UserIdCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

import java.lang.Class;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import jenkins.model.Jenkins;

/**
 * Badge action of the build trigger cause.
 * 
 * @author Michael Pailloncy
 */
public class BuildTriggerBadgeAction implements BuildBadgeAction {

	private final Cause cause;

	/**
	 * Constructor. Initialize causes of the build.
	 * 
	 * @param build
	 *            : {@link AbstractBuild}
	 */
	public BuildTriggerBadgeAction(Cause cause) {
		// TODO : don't store cause but compute icon & title up-front for
		// "perf"?
		this.cause = cause;
	}

	public String getTooltip() {
		return cause.getShortDescription();
	}

	public String getIcon() {
		Class clazz = cause.getClass();
		String path = null;
		while (clazz != null && path == null) {
			path = iconPaths.get(clazz.getName());
			if (path == null) {
				clazz = clazz.getSuperclass();
			}
		}
		if (path == null) {
			path = "fallback-cause.png";
		}
		return getIconPath(path);
	}

	private static String getIconPath(String iconName) {
		PluginWrapper wrapper = Jenkins.getInstance().getPluginManager()
				.getPlugin(BuildTriggerBadgePlugin.class);
		return "/plugin/" + wrapper.getShortName() + "/images/" + iconName;
	}

	protected static Map<String,String> iconPaths = new HashMap<String,String>();
	static {
		iconPaths.put(UserIdCause.class.getName(), "user-cause.png");
		iconPaths.put(UserCause.class.getName(), "user-cause.png");
		iconPaths.put(TimerTriggerCause.class.getName(), "timer-cause.png");
		iconPaths.put(SCMTriggerCause.class.getName(), "scm-cause.png");
		iconPaths.put(UpstreamCause.class.getName(), "upstream-cause.png");
		iconPaths.put(CLICause.class.getName(), "cli-cause.png");
		iconPaths.put(RemoteCause.class.getName(), "remote-cause.png");
		iconPaths.put("org.jvnet.hudson.plugins.m2release.ReleaseCause", "user-cause.png");
		iconPaths.put("com.cloudbees.jenkins.GitHubPushCause", "github-push-cause.png");
		iconPaths.put("org.jenkinsci.plugins.periodicreincarnation.PeriodicReincarnationBuildCause", "periodic-reincarnation.png");
	}

	public static BuildTriggerBadgePlugin getPlugin() {
		return (BuildTriggerBadgePlugin) Jenkins.getInstance().getPlugin(BuildTriggerBadgePlugin.class);
	}
	
	// non use interface methods
	public String getIconFileName() {
		return null;
	}

	public String getDisplayName() {
		return "Trigger " + cause.getClass().getSimpleName() + " : "
				+ getTooltip();
	}

	public String getUrlName() {
		return "";
	}
}

package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.PluginWrapper;
import hudson.model.AbstractBuild;
import hudson.model.BuildBadgeAction;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserIdCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

import java.util.HashMap;
import java.util.Map;

import jenkins.model.Jenkins;

/**
 * Badge action of the build trigger cause.
 * 
 * @author Michaël Pailloncy
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
	String path = iconPaths.get(cause.getClass());
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

    protected static Map<Class<? extends Cause>, String> iconPaths = new HashMap<Class<? extends Cause>, String>();
    static {
	iconPaths.put(UserIdCause.class, "user-cause.png");
	iconPaths.put(TimerTriggerCause.class, "timer-cause.png");
	iconPaths.put(SCMTriggerCause.class, "scm-cause.png");
	iconPaths.put(UpstreamCause.class, "upstream-cause.png");
    }

    // non use interface methods
    public String getIconFileName() {
	return null;
    }

    public String getDisplayName() {
	return "Trigger "+cause.getClass().getSimpleName()+" : "+getTooltip();
    }

    public String getUrlName() {
	return "";
    }
}

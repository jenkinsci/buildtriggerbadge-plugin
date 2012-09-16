package org.jenkinsci.plugins;

import hudson.PluginWrapper;
import hudson.model.BuildBadgeAction;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Cause.UserIdCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;
import jenkins.model.Jenkins;

/**
 * Badge action of the build trigger cause.
 * @author Micha�l Pailloncy
 */
public class BuildTriggerBadgeAction implements BuildBadgeAction {

	private boolean isSCMTriggerCause;
	private boolean isTimerTriggerCause;
	private boolean isUserCause;

	/** Description displayed in the &lt;img/&gt; tooltip. */
	private String description;
	
	/**
	 * Constructor. Initialize causes of the build.
	 * @param build : {@link AbstractBuild}
	 */
	public BuildTriggerBadgeAction(AbstractBuild build) {
		System.out.println("nb de causes: "+build.getCauses().size());
		for (int index = 0; index < build.getCauses().size(); index++) {
			Cause cause = (Cause) build.getCauses().get(index);
			if(cause instanceof SCMTriggerCause) {
				isSCMTriggerCause = true;
				if(cause.getShortDescription() != null) {
					description = cause.getShortDescription();
				}
			}
			else if (cause instanceof TimerTriggerCause) {
				isTimerTriggerCause = true;
				if(cause.getShortDescription() != null) {
					description = cause.getShortDescription();
				}
				else {
					description = "Triggered by a periodic timer";
				}
			} 
			else if (cause instanceof UserIdCause) {
				isUserCause = true;
				description = cause.getShortDescription();
			} 
		}
	}

	public static String getIconTimerTriggerCausePath() {
		return getIconPath("timer-cause.png");
	}
	
	public static String getIconSCMTriggerCausePath() {
		return getIconPath("scm-cause.png");
	}
	
	public static String getIconUserCausePath() {
		return getIconPath("user-cause.png");
	}
	
	private static String getIconPath(String iconName){
		PluginWrapper wrapper = Jenkins.getInstance().getPluginManager().getPlugin(BuildTriggerBadgePlugin.class);
		return "/plugin/" + wrapper.getShortName() + "/images/" + iconName;
	}

	public boolean isTimerTriggerCause() {
		return isTimerTriggerCause;
	}

	public void setTimerTriggerCause(boolean isTimerTriggerCause) {
		this.isTimerTriggerCause = isTimerTriggerCause;
	}

	public boolean isSCMTriggerCause() {
		return isSCMTriggerCause;
	}

	public void setSCMTriggerCause(boolean isSCMTriggerCause) {
		this.isSCMTriggerCause = isSCMTriggerCause;
	}
	
	public boolean isUserCause() {
		return isUserCause;
	}

	public void setUserCause(boolean isUserCause) {
		this.isUserCause = isUserCause;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// non use interface methods
	public String getIconFileName() { return null; }
	public String getDisplayName() { return ""; }
	public String getUrlName() { return ""; }

}

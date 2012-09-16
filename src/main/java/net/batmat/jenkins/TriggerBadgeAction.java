package net.batmat.jenkins;

import hudson.model.BuildBadgeAction;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

public class TriggerBadgeAction implements BuildBadgeAction {

	private final Cause buildCause;

	public TriggerBadgeAction(Cause cause) {
		this.buildCause = cause;
	}

	public String getTooltip() {
		String text = "no information";
		if (buildCause != null) {
			text = buildCause.getShortDescription();
			text += "("+buildCause.getClass().getSimpleName()+")";
			System.err.println(text);
		}
		return  Messages.TriggerBadgeAction_trigger(text);
	}

	public String getDisplayName() {
		return Messages.TriggerBadgeAction_displayName();
	}

	public String getIcon() {
		if(SCMTriggerCause.class.isInstance(buildCause))
		{
			return IconScmCause;
		}
		else if(UserCause.class.isInstance(buildCause))
		{
			return IconUserCause;
		}
		else if(UpstreamCause.class.isInstance(buildCause))
		{
			return IconUpstreamCause;
		}
		else if(TimerTriggerCause.class.isInstance(buildCause))
		{
			return IconTimerCause;
		}
		return IconCatchAllCause;
	}

	public String getUrlName() {
		return null;
	}

	public String getIconFileName() {
		return null;
	}
	
	private static final String IconUpstreamCause = "/plugin/triggerbadge/images/trigger-badge-upstream-cause.png";
	private static final String IconScmCause = "/plugin/triggerbadge/images/trigger-badge-scm-cause.png";
	private static final String IconUserCause = "/plugin/triggerbadge/images/trigger-badge-user-cause.png";
	private static final String IconTimerCause = "/plugin/triggerbadge/images/trigger-badge-timer-cause.png";
	private static final String IconCatchAllCause = "/plugin/triggerbadge/images/trigger-badge.png";
	
}

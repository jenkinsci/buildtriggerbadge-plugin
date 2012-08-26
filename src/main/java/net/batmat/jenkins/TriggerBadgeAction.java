package net.batmat.jenkins;

import hudson.model.BuildBadgeAction;
import hudson.model.Cause;

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
		return "/plugin/triggerbadge/images/trigger-badge.png";
	}

	public String getUrlName() {
		return null;
	}

	public String getIconFileName() {
		return null;
	}
}

package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.listeners.RunListener;

import java.util.List;

import jenkins.model.Jenkins;

import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeDeactivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener to all build to add the badge action.
 * 
 * @author Michael Pailloncy
 */
@Extension
public class RunListenerImpl extends RunListener<AbstractBuild> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunListenerImpl.class);

	public RunListenerImpl() {
		super(AbstractBuild.class);
	}

	@Override
	public void onStarted(AbstractBuild build, TaskListener listener) {
		BuildTriggerBadgePlugin plugin = Jenkins.getInstance().getPlugin(BuildTriggerBadgePlugin.class);
		if (plugin.isActivated()) {
			List<Cause> causes = CauseFilter.filter((List<Cause>) build.getCauses());
			for (Cause cause : causes) {
				if (isEnabled(cause)) {
					build.addAction(new BuildTriggerBadgeAction(cause));
				}
			}
		}
		super.onStarted(build, listener);
	}

	/**
	 * Checks all the {@link BuildTriggerBadgeDeactivator} implementations to see if the given cause should have its badge disabled.
	 * 
	 * @param cause
	 *            the cause to be tested against potential deactivators.
	 * @return true if the cause is still to be given a badge, else false.
	 */
	private boolean isEnabled(Cause cause) {
		for (BuildTriggerBadgeDeactivator deactivator : BuildTriggerBadgeDeactivator.all()) {
			if (deactivator.vetoBadge(cause)) {
				LOGGER.debug("Badge for cause '{}' disabled by extension ''", cause, deactivator);
				return false;
			}
		}
		return true;
	}
}

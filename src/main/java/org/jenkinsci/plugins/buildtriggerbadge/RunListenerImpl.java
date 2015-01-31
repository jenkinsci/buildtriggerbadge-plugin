package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jenkins.model.Jenkins;

/**
 * Listener to all build to add the badge action.
 * 
 * @author Michael Pailloncy
 */
@Extension
public class RunListenerImpl extends RunListener<AbstractBuild> {

	public RunListenerImpl() {
		super(AbstractBuild.class);
	}

	@Override
	public void onStarted(AbstractBuild build, TaskListener listener) {
		BuildTriggerBadgePlugin plugin = Jenkins.getInstance().getPlugin(BuildTriggerBadgePlugin.class);
		if (plugin.isActivated()) {
			Set<String> causeClasses = new HashSet<String>();
			List<Cause> causes = CauseFilter.filter((List<Cause>) build.getCauses());
			if (causes != null) {
				for (Cause cause : causes) {
					build.addAction(new BuildTriggerBadgeAction(cause));
				}
			}
		}
		super.onStarted(build, listener);
	}

}

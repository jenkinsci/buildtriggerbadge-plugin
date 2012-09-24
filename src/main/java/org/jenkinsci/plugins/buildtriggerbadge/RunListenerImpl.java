package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.List;

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
		if(plugin.isActivated()) {
			List<Cause> causes = build.getCauses();
			for (Cause cause : causes) {
				build.addAction(new BuildTriggerBadgeAction(cause));
			}
		}
		super.onStarted(build, listener);
	}
}

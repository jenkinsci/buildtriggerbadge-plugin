package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.listeners.RunListener;

/**
 * Listener to all build to add the badge action.
 * @author Michaël Pailloncy
 */
@Extension
public class RunListenerImpl extends RunListener<AbstractBuild>{

	public RunListenerImpl() {
		super(AbstractBuild.class);
	}
	
	@Override
	public void onStarted(AbstractBuild r, TaskListener listener) {
		// add BuildTriggerBadgeAction on each builds.
		r.addAction(new BuildTriggerBadgeAction(r));
		super.onStarted(r, listener);
	}
}

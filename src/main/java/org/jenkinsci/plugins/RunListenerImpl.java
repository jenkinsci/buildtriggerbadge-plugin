package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.List;

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
	public void onStarted(AbstractBuild build, TaskListener listener) {
		List<Cause> causes = build.getCauses();
    	for(Cause cause:causes){
			build.addAction(new BuildTriggerBadgeAction(cause));
		}
		super.onStarted(build, listener);
	}
}

package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.Plugin;
import hudson.PluginWrapper;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.ArrayList;
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
		if(plugin.isActivated()) {
			Set<String> causeClasses =  new HashSet<String>();
			List<Cause> causes = build.getCauses();
			for (Cause cause : causes) {
				// filter causes by Class type and description
				String filter = getCauseFilter(cause);
				if(!causeClasses.contains(filter)) {
					causeClasses.add(filter);
					build.addAction(new BuildTriggerBadgeAction(cause));
				}
			}
		}
		super.onStarted(build, listener);
	}
	
	private static String getCauseFilter(Cause cause) {
		return cause.getClass().getCanonicalName()+ "_" + cause.getShortDescription();
	}
	
}

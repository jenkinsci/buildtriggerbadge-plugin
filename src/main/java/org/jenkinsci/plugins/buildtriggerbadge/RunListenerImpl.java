package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.ExtensionList;

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
				// get the extension list for BuildTriggerBadgeValidator
				ExtensionList<BuildTriggerBadgeValidator> btbValidatorExtensionList = Jenkins.getInstance().getExtensionList(BuildTriggerBadgeValidator.class);
				
				for (Cause cause : causes) {
					
					// assume the cause icon was not handled differently
					boolean isApplicable = false;
					
					// go through all the implementations of the extension point BuildTriggerBadgeValidator, if they exist
					for (BuildTriggerBadgeValidator buildTriggerBadgeValidator : btbValidatorExtensionList) {
				
						// check if the cause in question has an overwriting icon defied outside of this plugin
						// this functionality in an opt-in feature which should become active only then the below check is verified, for at least one extension
						if (buildTriggerBadgeValidator.isApplicable(cause)) {
							isApplicable = true;
						}
					}

					// default case of handling the assignment of icons to the specific cause types, when no extension point overwrote the functionality
					if (isApplicable == false) {
						build.addAction(new BuildTriggerBadgeAction(cause));
					}
				}
			}
		}
		super.onStarted(build, listener);
	}

}

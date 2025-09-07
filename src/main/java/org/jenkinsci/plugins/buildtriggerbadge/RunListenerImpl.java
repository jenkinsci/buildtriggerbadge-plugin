package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Extension;
import hudson.PluginWrapper;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * Listener to all build to add the badge action.
 *
 * @author Michael Pailloncy
 */
@Extension
public class RunListenerImpl extends RunListener<Run<?, ?>> {

    @Override
    public void onStarted(Run build, TaskListener listener) {
        PluginWrapper plugin = BuildTriggerBadgePlugin.get();
        if (plugin.isActive()) {
            BuildTriggerBadgeAction.createForRun(build).forEach(build::addAction);
        }
        super.onStarted(build, listener);
    }
}

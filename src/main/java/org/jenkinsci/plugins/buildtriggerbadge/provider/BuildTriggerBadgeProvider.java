package org.jenkinsci.plugins.buildtriggerbadge.provider;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Cause;
import jenkins.model.Jenkins;

/**
 * Extend this class to be able to define/override an association between a class and an icon. This will then add that icon as a badge in
 * the build history side panel.
 * 
 * <p>
 * Note : if many providers are found, the first one matching a given cause class will be used.
 * </p>
 * 
 * @see <a
 *      href="https://wiki.jenkins-ci.org/display/JENKINS/Build+Trigger+Badge+Plugin#BuildTriggerBadgePlugin-Screenshotbadges">BuildTriggerBadge
 *      screenshot as an example</a>
 * 
 * @author Baptiste Mathus
 */
public abstract class BuildTriggerBadgeProvider implements ExtensionPoint {

	/**
	 * Should return the URL to the icon corresponding to the given cause.
	 * 
	 * @return the URL to the icon for that cause, null if none provided.
	 */
	public abstract String provideIcon(Cause cause);

	public static ExtensionList<BuildTriggerBadgeProvider> all() {
		return Jenkins.getInstance().getExtensionList(BuildTriggerBadgeProvider.class);
	}
}

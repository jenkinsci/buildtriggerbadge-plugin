package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.Plugin;
import hudson.model.Descriptor.FormException;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Plugin extension.
 * @author Michael Pailloncy
 */
public class BuildTriggerBadgePlugin extends Plugin {

	/** Indicates if the plugin is activated. */
	private boolean activated = true;

	public BuildTriggerBadgePlugin() {
		
	}
	
	@DataBoundConstructor
    public BuildTriggerBadgePlugin(boolean activated) {
        this.activated = activated;
    }

	@Override
	public void configure(StaplerRequest req, JSONObject formData)
			throws IOException, ServletException, FormException {
		
		super.configure(req, formData);
		// get activated value from system configuration save.
		this.setActivated(formData.getBoolean(FIELD_ACTIVATED));
		// serialize to XML
		this.save();
	}

	public boolean isActivated() {	
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	public static final String FIELD_ACTIVATED = "buildtriggerbadge_activated";
}

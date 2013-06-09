package org.jenkinsci.plugins.buildtriggerbadge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

/**
 * Column showing last build cause as icon and description
 * 
 * @since 1.1
 * @author ljader
 * 
 */
public class LastBuildTriggerColumn extends ListViewColumn {
    private static final class BuildNodeColumnDescriptor extends ListViewColumnDescriptor {
        @Override
        public String getDisplayName() {
         return Messages.LastBuildTriggerColumn_DisplayName();
        }

        @Override
        public ListViewColumn newInstance(final StaplerRequest request, final JSONObject formData) 
        		throws FormException {
            return new LastBuildTriggerColumn();
        }

        @Override
        public boolean shownByDefault() {
            return false;
        }
    }
    
    @Extension
    public static final Descriptor<ListViewColumn> DESCRIPTOR = new BuildNodeColumnDescriptor();

    @Override
    public Descriptor<ListViewColumn> getDescriptor() {
        return DESCRIPTOR;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, String> getLastBuildCauses(Job job) {
    	Run r = job.getLastBuild();
    	if (r != null) {
    		List<Cause> lastCauses = r.getCauses();
    		if (lastCauses != null) {
    			Map<String,String> causeEntries = new HashMap<String,String>();
    			for (Cause cause : lastCauses) {
    				causeEntries.put(new BuildTriggerBadgeAction(cause).getIcon(), 
    						cause.getShortDescription());
    			}
    			return causeEntries;
    		}
    	}
    	return null;
    }
}

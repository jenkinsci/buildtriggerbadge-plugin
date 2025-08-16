package org.jenkinsci.plugins.buildtriggerbadge;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Column showing last build cause as icon and description
 *
 * @since 1.1
 * @author ljader
 *
 */
public class LastBuildTriggerColumn extends ListViewColumn {
    public static final String ICON_ONLY = "iconOnly";
    public static final String ICON_AND_DESC = "iconAndDesc";

    private final String causeDisplayType;

    @DataBoundConstructor
    public LastBuildTriggerColumn(String causeDisplayType) {
        super();
        this.causeDisplayType = causeDisplayType;
    }

    public LastBuildTriggerColumn() {
        this(ICON_ONLY);
    }

    public String getCauseDisplayType() {
        return causeDisplayType;
    }

    private static final class BuildNodeColumnDescriptor extends ListViewColumnDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return Messages.LastBuildTriggerColumn_DisplayName();
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

    @SuppressWarnings({"unused"}) // used by jelly
    public List<BuildTriggerBadgeAction> getLastBuildCauses(Job<?, ?> job) {
        Run<?, ?> r = job.getLastBuild();
        if (r != null) {
            return BuildTriggerBadgeAction.createForRun(r);
        }

        return null;
    }
}

package org.jenkinsci.plugins.buildtriggerbadge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.BuildBadgeAction;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.queue.QueueTaskFuture;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import java.util.List;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeDeactivator;
import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestExtension;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class BuildTriggerBadgeActionTest {

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
    }

    @Test
    void stupidlyMockedUpstreamCause() {
        Cause cause = mock(UpstreamCause.class);
        when(cause.getShortDescription()).thenReturn("upstream cause, blablabla");

        BuildTriggerBadgeAction action = new BuildTriggerBadgeAction(cause);
        assertThat(action.getTooltip(), containsStringIgnoringCase("upstream"));
        assertThat(action.getDisplayName(), containsString("upstream"));
    }

    private static class MyCause extends Cause {
        @Override
        public String getShortDescription() {
            return "mycause_pouet";
        }
    }

    @TestExtension("externallyAddedCause")
    public static class MyCauseProvider extends BuildTriggerBadgeProvider {

        @Override
        public String provideIcon(@NonNull Cause cause) {
            return "...pouetpouet.png";
        }
    }

    @Test
    void externallyAddedCause() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        QueueTaskFuture<FreeStyleBuild> futureBuild = project.scheduleBuild2(0, new MyCause());
        FreeStyleBuild build = futureBuild.get();
        assertThat(build.getCauses(), hasSize(1));
        assertThat(build.getCauses().get(0), instanceOf(MyCause.class));

        List<BuildBadgeAction> badgeActions = build.getBadgeActions();
        assertThat(badgeActions, hasSize(1));
        BuildBadgeAction badgeAction = badgeActions.get(0);
        assertThat(badgeAction, instanceOf(BuildTriggerBadgeAction.class));
        BuildTriggerBadgeAction buildTriggerBadgeAction = (BuildTriggerBadgeAction) badgeAction;
        assertThat(buildTriggerBadgeAction.getIcon(), containsString("pouetpouet"));
    }

    @TestExtension
    public static class SCMTriggerCauseDisablingProvider extends BuildTriggerBadgeDeactivator {
        @Override
        public boolean vetoBadge(@NonNull Cause cause) {
            return cause instanceof SCMTriggerCause;
        }
    }

    @Test
    void externallyDisabledCause() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        QueueTaskFuture<FreeStyleBuild> futureBuild = project.scheduleBuild2(0, new SCMTriggerCause("Boum"));
        FreeStyleBuild build = futureBuild.get();
        assertThat(build.getCauses(), hasSize(1));

        List<BuildBadgeAction> badgeActions = build.getBadgeActions();
        assertThat(badgeActions, empty());
    }
}

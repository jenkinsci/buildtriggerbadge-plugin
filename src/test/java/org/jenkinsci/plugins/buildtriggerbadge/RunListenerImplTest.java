package org.jenkinsci.plugins.buildtriggerbadge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

import hudson.model.BuildBadgeAction;
import hudson.model.FreeStyleBuild;
import hudson.model.Cause;
import hudson.model.Cause.RemoteCause;
import hudson.model.FreeStyleProject;
import hudson.triggers.SCMTrigger.SCMTriggerCause;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * Tests for {@link RunListenerImpl}
 *
 * @author Michael Pailloncy
 */
@WithJenkins
class RunListenerImplTest {

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
    }

	@Issue("JENKINS-15474")
	@Test
	void sameIconMultipleTimesCorrection() throws IOException, InterruptedException, ExecutionException {
		FreeStyleProject p = j.createFreeStyleProject();
		Future<FreeStyleBuild> futureBuild = p.scheduleBuild2(2, new SCMTriggerCause("scm change 1"));
		p.scheduleBuild2(1, new SCMTriggerCause("scm change 2"));
		p.scheduleBuild2(1, new SCMTriggerCause("scm change 3"));
		p.scheduleBuild2(1, new SCMTriggerCause("scm change 4"));
		p.scheduleBuild2(1, new SCMTriggerCause("scm change 5"));
		FreeStyleBuild build = futureBuild.get();
		assertThat(build.getCauses(), hasSize(5));
		// there should be only one badge action here
		assertThat(build.getBadgeActions(), hasSize(1));
	}

	@Test
	void checkBadges() throws Exception {
		checkBuildCause(new SCMTriggerCause("bim"), "scm-cause.png");
		checkBuildCause(new RemoteCause("hop", "kk"), "remote-cause.png");
	}

	private void checkBuildCause(Cause buildCause, String expected) throws IOException, InterruptedException, ExecutionException {
		FreeStyleProject project = j.createFreeStyleProject();
		Future<FreeStyleBuild> futureBuild = project.scheduleBuild2(0, buildCause);
		FreeStyleBuild build = futureBuild.get();
		List<BuildBadgeAction> badgeActions = build.getBadgeActions();
		assertThat(badgeActions, hasSize(1));
		BuildBadgeAction badgeAction = badgeActions.get(0);
		assertThat(badgeAction, instanceOf(BuildTriggerBadgeAction.class));
		BuildTriggerBadgeAction buildTriggerBadgeAction = (BuildTriggerBadgeAction) badgeAction;
		assertThat(buildTriggerBadgeAction.getIcon(), containsString(expected));
	}
}

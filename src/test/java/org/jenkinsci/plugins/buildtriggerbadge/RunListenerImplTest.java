package org.jenkinsci.plugins.buildtriggerbadge;

import static org.fest.assertions.api.Assertions.assertThat;
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

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Tests for {@link RunListenerImpl}
 * 
 * @author Michael Pailloncy
 */
public class RunListenerImplTest {
	@Rule
	public JenkinsRule j = new JenkinsRule();

	@Bug(15474)
	@Test
	public void sameIconMultipleTimesCorrection() throws IOException, InterruptedException, ExecutionException {
		FreeStyleProject p = j.createFreeStyleProject();
		Future<FreeStyleBuild> futureBuild = p.scheduleBuild2(20, new SCMTriggerCause("scm change 1"));
		assertThat(p.scheduleBuild(new SCMTriggerCause("scm change 2"))).isFalse();
		assertThat(p.scheduleBuild(new SCMTriggerCause("scm change 3"))).isFalse();
		assertThat(p.scheduleBuild(new SCMTriggerCause("scm change 4"))).isFalse();
		assertThat(p.scheduleBuild(new SCMTriggerCause("scm change 5"))).isFalse();
		FreeStyleBuild build = futureBuild.get();
		// there should be only one badge action here
		assertThat(build.getBadgeActions()).hasSize(1);
	}

	@Test
	public void checkBadges() throws Exception {
		checkBuildCause(new SCMTriggerCause("bim"), "scm-cause.png");
		checkBuildCause(new RemoteCause("hop", "kk"), "remote-cause.png");
	}

	private void checkBuildCause(Cause buildCause, String expected) throws IOException, InterruptedException, ExecutionException {
		FreeStyleProject project = j.createFreeStyleProject();
		Future<FreeStyleBuild> futureBuild = project.scheduleBuild2(0, buildCause);
		FreeStyleBuild build = futureBuild.get();
		List<BuildBadgeAction> badgeActions = build.getBadgeActions();
		assertThat(badgeActions).hasSize(1);
		BuildBadgeAction badgeAction = badgeActions.get(0);
		assertThat(badgeAction).isInstanceOf(BuildTriggerBadgeAction.class);
		BuildTriggerBadgeAction buildTriggerBadgeAction = (BuildTriggerBadgeAction) badgeAction;
		assertThat(buildTriggerBadgeAction.getIcon()).contains(expected);
	}
}

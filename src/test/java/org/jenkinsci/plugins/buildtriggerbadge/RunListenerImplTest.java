package org.jenkinsci.plugins.buildtriggerbadge;

import static org.fest.assertions.api.Assertions.assertThat;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.triggers.SCMTrigger.SCMTriggerCause;

import java.io.IOException;
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
}

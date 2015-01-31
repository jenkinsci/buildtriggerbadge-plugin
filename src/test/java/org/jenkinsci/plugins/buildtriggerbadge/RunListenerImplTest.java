package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.triggers.SCMTrigger.SCMTriggerCause;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Tests for {@link RunListenerImpl}
 * 
 * @author Michael Pailloncy
 */
public class RunListenerImplTest extends HudsonTestCase {

	@Bug(15474)
	public void testSameIconMultipleTimesCorrection() throws IOException, InterruptedException, ExecutionException {
		FreeStyleProject p = createFreeStyleProject();
		Future<FreeStyleBuild> futureBuild = p.scheduleBuild2(20, new SCMTriggerCause("scm change 1"));
		assertFalse(p.scheduleBuild(new SCMTriggerCause("scm change 2")));
		assertFalse(p.scheduleBuild(new SCMTriggerCause("scm change 3")));
		assertFalse(p.scheduleBuild(new SCMTriggerCause("scm change 4")));
		assertFalse(p.scheduleBuild(new SCMTriggerCause("scm change 5")));
		FreeStyleBuild build = futureBuild.get();
		// there should be only one badge action here
		assertEquals(1, build.getBadgeActions().size());
	}
}

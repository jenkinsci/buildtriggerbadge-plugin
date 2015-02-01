package org.jenkinsci.plugins.buildtriggerbadge;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hudson.model.BuildBadgeAction;
import hudson.model.FreeStyleBuild;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.FreeStyleProject;
import hudson.model.queue.QueueTaskFuture;

import java.util.List;

import org.jenkinsci.plugins.buildtriggerbadge.provider.BuildTriggerBadgeProvider;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestExtension;

public class BuildTriggerBadgeActionTest {
	@Rule
	public JenkinsRule j = new JenkinsRule();

	@Test
	public void stupidlyMockedUpstreamCause() {
		Cause cause = mock(UpstreamCause.class);
		when(cause.getShortDescription())
				.thenReturn("upstream cause, blablabla");

		BuildTriggerBadgeAction action = new BuildTriggerBadgeAction(cause);
		assertThat(action.getTooltip()).containsIgnoringCase("upstream");
		assertThat(action.getDisplayName()).contains("upstream");
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
		public String provideIcon(Cause cause) {
			return "...pouetpouet.png";
		}

	}
	@Test
	public void externallyAddedCause() throws Exception {
		FreeStyleProject project = j.createFreeStyleProject();
		QueueTaskFuture<FreeStyleBuild> futureBuild = project.scheduleBuild2(0, new MyCause());
		FreeStyleBuild build = futureBuild.get();
		assertThat(build.getCauses()).hasSize(1);
		assertThat(build.getCauses().get(0)).isInstanceOf(MyCause.class);
		
		List<BuildBadgeAction> badgeActions = build.getBadgeActions();
		assertThat(badgeActions).hasSize(1);
		BuildBadgeAction badgeAction = badgeActions.get(0);
		assertThat(badgeAction).isInstanceOf(BuildTriggerBadgeAction.class);
		BuildTriggerBadgeAction buildTriggerBadgeAction = (BuildTriggerBadgeAction) badgeAction;
		assertThat(buildTriggerBadgeAction.getIcon()).contains("pouetpouet");
	}
}

package org.jenkinsci.plugins.buildtriggerbadge;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;

import org.junit.Test;

public class BuildTriggerBadgeActionTest {
	@Test
	public void stupidlyMockedUpstreamCause() {
		Cause cause = mock(UpstreamCause.class);
		when(cause.getShortDescription())
				.thenReturn("upstream cause, blablabla");

		BuildTriggerBadgeAction action = new BuildTriggerBadgeAction(cause);
		assertThat(action.getTooltip()).containsIgnoringCase("upstream");
		assertThat(action.getDisplayName()).contains("upstream");

	}
}

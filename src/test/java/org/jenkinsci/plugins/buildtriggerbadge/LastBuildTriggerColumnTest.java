package org.jenkinsci.plugins.buildtriggerbadge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.triggers.TimerTrigger;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class LastBuildTriggerColumnTest {

    private FreeStyleProject job;

    @BeforeEach
    public void setUp() {
        job = mock(FreeStyleProject.class);
    }

    @Test
    void getLastBuildCauses() {
        FreeStyleBuild lastRun = mock(FreeStyleBuild.class);
        when(job.getLastBuild()).thenReturn(lastRun);
        when(lastRun.getCauses())
                .thenReturn(List.of(
                        new Cause.RemoteCause("localhost", null),
                        new Cause.RemoteCause("localhost", null),
                        new Cause.RemoteCause("otherhost", null),
                        new TimerTrigger.TimerTriggerCause(),
                        new TimerTrigger.TimerTriggerCause()));
        LastBuildTriggerColumn col = new LastBuildTriggerColumn();
        List<BuildTriggerBadgeAction> lastBuildCauses = col.getLastBuildCauses(job);

        assertEquals(
                List.of(
                        "symbol-radio-outline plugin-ionicons-api",
                        "symbol-radio-outline plugin-ionicons-api",
                        "symbol-time-outline plugin-ionicons-api"),
                lastBuildCauses.stream().map(BuildTriggerBadgeAction::getIcon).toList());
        assertEquals(
                List.of("Started by remote host localhost", "Started by remote host otherhost", "Started by timer"),
                lastBuildCauses.stream()
                        .map(BuildTriggerBadgeAction::getTooltip)
                        .toList());
    }

    @Test
    void getLastBuildCausesLastRunNull() {
        LastBuildTriggerColumn col = new LastBuildTriggerColumn();
        assertNull(col.getLastBuildCauses(job));
    }
}

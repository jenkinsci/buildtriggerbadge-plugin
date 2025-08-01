package org.jenkinsci.plugins.buildtriggerbadge;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import hudson.model.Cause;
import hudson.triggers.TimerTrigger;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CauseFilterTest {
    static Stream<Arguments> filterTestCases() {
        var local1 = new Cause.RemoteCause("localhost", null);
        var local2 = new Cause.RemoteCause("localhost", null);
        var other1 = new Cause.RemoteCause("otherhost", null);
        var time1 = new TimerTrigger.TimerTriggerCause();
        var time2 = new TimerTrigger.TimerTriggerCause();
        return Stream.of(
                arguments(null, List.of()),
                arguments(List.of(), List.of()),
                arguments(List.of(local1, local2, other1, time1, time2), List.of(local1, other1, time1)));
    }

    @ParameterizedTest
    @MethodSource("filterTestCases")
    void filter(List<Cause> input, List<Cause> output) {
        assertEquals(output, CauseFilter.filter(input));
    }
}

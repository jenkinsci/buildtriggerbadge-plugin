package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.model.Cause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filtering possible duplicate causes
 * 
 * @since 1.1
 * 
 * @author Michael Pailloncy
 * @author ljader
 *
 */
public class CauseFilter {
	/**
	 * Filter causes by Class type and description
	 * 
	 * @param inputCauses
	 * @return filtered list or null
	 */
	public static List<Cause> filter(List<Cause> inputCauses) {
		if (inputCauses == null)
			return null;

		List<Cause> outCauses = new ArrayList<Cause>();
		Set<String> causeClasses = new HashSet<String>();
		for (Cause cause : inputCauses) {
			// filter causes by Class type and description
			String filter = getCauseFilter(cause);
			if (!causeClasses.contains(filter)) {
				causeClasses.add(filter);
				outCauses.add(cause);
			}
		}

		return outCauses;
	}

	private static String getCauseFilter(Cause cause) {
		return cause.getClass().getCanonicalName() + "_" + cause.getShortDescription();
	}
}

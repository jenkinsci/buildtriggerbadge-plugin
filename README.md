Build Trigger Badge Plugin
========================

[![buildtriggerbadge version](https://img.shields.io/jenkins/plugin/v/buildtriggerbadge.svg?label=buildtriggerbadge)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge installs](https://img.shields.io/jenkins/plugin/i/buildtriggerbadge.svg)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge license](https://img.shields.io/github/license/jenkinsci/buildtriggerbadge-plugin)](https://github.com/jenkinsci/buildtriggerbadge-plugin/blob/master/LICENSE)

Jenkins' plugin to display an icon representing the trigger cause of a build.

See the [wiki page](https://wiki.jenkins-ci.org/display/JENKINS/Build+Trigger+Badge+Plugin) 
for a more complete documentation.

What if I want to add, override, or disable a badge for some build cause? 
-------------------------------------------------------------------------

* Adding a badge for a cause defined in a public plugin should be straightforward: file a feature request in JIRA
  on the buildtriggerbadge component: https://issues.jenkins-ci.org/browse/JENKINS/component/16924

* Adding a cause coming from a private/custom plugin, or overriding some badge is possible since version 2.0 :
  
Extend a BuildTriggerBadgeProvider, and implement the provide method to match your needs. 

```java
@Extension
public class MyProvider extends BuildTriggerBadgeProvider {
	@Override
	public String provideIcon(Cause cause) {
		if (cause instanceof SCMTriggerCause) {
			return "http://example.com/someimage.png";
		}
		return null;
	}
}
```

* Disable a badge for a given cause
Let's say you want to disable the badge for the typical periodic timer (TimerTriggerCause), then just extends the 
BuildTriggerBadgeDeactivator extension point and deploy your plugin.

Example :

```java
@Extension
public class MyDeactivator extends BuildTriggerBadgeDeactivator {
	@Override
	public boolean vetoBadge(Cause cause) {
		if (cause instanceof TimerTriggerCause) {
			System.out.println("Disabling badge for timertriggercause, yay!!");
			return true;
		}
		return false;
	}
}
```

Contributing
------------
If you are using Eclipse, please try and submit pull-requests using the code formatting rules provided.
We're currently not strictly requiring it, but if it can unify the codebase at least a bit, that can only be for 
the better.

Thanks

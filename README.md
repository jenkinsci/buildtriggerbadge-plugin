Build Trigger Badge Plugin
==========================

[![buildtriggerbadge version](https://img.shields.io/jenkins/plugin/v/buildtriggerbadge.svg?label=buildtriggerbadge)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge installs](https://img.shields.io/jenkins/plugin/i/buildtriggerbadge.svg)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge license](https://img.shields.io/github/license/jenkinsci/buildtriggerbadge-plugin)](https://github.com/jenkinsci/buildtriggerbadge-plugin/blob/master/LICENSE)

Jenkins plugin to display an icon representing the trigger cause of a build.

See the [wiki page](https://wiki.jenkins-ci.org/display/JENKINS/Build+Trigger+Badge+Plugin) 
for a more complete documentation.

What if I want to add, override, or disable a badge for some build cause? 
-------------------------------------------------------------------------

* Adding a badge for a cause defined in a public plugin should be straightforward: file a [feature request](https://issues.jenkins.io/issues/?jql=project+%3D+JENKINS+AND+component+%3D+buildtriggerbadge-plugin)
  in JIRA on the `buildtriggerbadge-plugin` component.

* Adding a cause coming from a private/custom plugin, or overriding some badge is possible since version 2.0.
  Extend `BuildTriggerBadgeProvider`, and implement the `provideIcon` method to match your needs. 
  See [Jenkins Symbols](https://weekly.ci.jenkins.io/design-library/symbols/).
  
  ```java
  @Extension
  public class MyProvider extends BuildTriggerBadgeProvider {
      @Override
      public String provideIcon(Cause cause) {
          if (cause instanceof SCMTriggerCause) {
              return "symbol-git-commit-outline plugin-ionicons-api";
          }
          return null;
      }
  }
  ```

* Disable a badge for a given cause. Let's say you want to disable the badge for the typical periodic timer 
  (`TimerTriggerCause`), then just extends the `BuildTriggerBadgeDeactivator` extension point and deploy your plugin.
  
  ```java
  @Extension
  public class MyDeactivator extends BuildTriggerBadgeDeactivator {
      @Override
      public boolean vetoBadge(Cause cause) {
          if (cause instanceof TimerTriggerCause) {
              // Disabling badge for timertriggercause, yay!!
              return true;
          }
          return false;
      }
  }
  ```

Contributing
------------

Refer to our [contribution guidelines](CONTRIBUTING.md)

Thanks

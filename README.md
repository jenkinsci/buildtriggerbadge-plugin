# Build Trigger Badge Plugin

[![buildtriggerbadge version](https://img.shields.io/jenkins/plugin/v/buildtriggerbadge.svg?label=buildtriggerbadge)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge installs](https://img.shields.io/jenkins/plugin/i/buildtriggerbadge.svg)](https://plugins.jenkins.io/buildtriggerbadge)
[![buildtriggerbadge license](https://img.shields.io/github/license/jenkinsci/buildtriggerbadge-plugin)](https://github.com/jenkinsci/buildtriggerbadge-plugin/blob/master/LICENSE)

Jenkins plugin to display an icon representing the trigger cause of a build directly in the build history. 
It lets you quickly know which cause triggered a build.

_Note: after installation, the plugin will currently only add icons on
new builds. Old builds won't have the indicator._

## What if I want to add, override, or disable a badge for some build cause?

* Adding a badge for a cause defined in a public plugin should be straightforward. We basically only need 
  the fully qualified name (FQN) of that build cause, and an icon. File a [feature request](https://www.jenkins.io/participate/report-issue/redirect/#16924/buildtriggerbadge).

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

## Wait, this does not work, I see a question mark icon for my builds!

This means the *cause* of the build is not yet supported by the plugin.
It's pretty easy to add one, we basically only need the fully qualified
name (FQN) of that build cause, and an icon.  

Finding out that FQN is pretty easy. You can simply use the Jenkins API
through your browser.

Open the build page, and suffix it with `/api/xml?xpath=//cause&wrapper=causes`. To give something like:
`https://yourjenkinsserver/job/somejob/15/api/xml?xpath=//cause&wrapper=causes`

Then, paste that output in a [Improvement Request](https://www.jenkins.io/participate/report-issue/redirect/#16924/buildtriggerbadge) 
explaining your case. Even better, filing a pull-request would be great.

## Contributing

Refer to our [contribution guidelines](CONTRIBUTING.md)

## Report an Issue

Please report issues and enhancements through the
[Jenkins issue tracker](https://www.jenkins.io/participate/report-issue/redirect/#16924/buildtriggerbadge).

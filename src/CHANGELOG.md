# Changelog 

### Newer versions

See [GitHub releases](https://github.com/jenkinsci/buildtriggerbadge-plugin/releases)

### Version 2.10 - 21 Dec 2018

-   Support the [Parameterized Scheduler Plugin](https://wiki.jenkins.io/display/JENKINS/Parameterized+Scheduler+Plugin) cause
    ([PR-33](https://github.com/jenkinsci/buildtriggerbadge-plugin/pull/33))
-   Update to Java 8 and Jenkins 2.60.3 minimum requirement

### Version 2.9 - 06 Dec 2017

-   Support the [CloudBees Checkpoints Plugin](https://release-notes.cloudbees.com/product/110) cause

### Version 2.8 - 17/02/2017

-   Support the branch-api plugin's `jenkins.branch.BranchEventCause`.

### Version 2.7 - 20/11/2016

-   Support the proprietary [CloudBees Pull-Request Builder for GitHub Plugin](https://go.cloudbees.com/docs/cloudbees-documentation/cje-user-guide/index.html#pull-request-builder-for-github)
    cause

### Version 2.6 - 15/10/2016

-   Support the XTrigger Plugin Cause. Requested through
    [JENKINS-38932](https://issues.jenkins-ci.org/browse/JENKINS-38932)

### Version 2.5 - 07/10/2016

-   Support the Pipeline Replay Cause. See the [related article to know more about that](https://www.cloudbees.com/blog/replay-pipeline)

### Version 2.4 - 04/10/2016

-   Support for [Branch Indexing](https://github.com/jenkinsci/buildtriggerbadge-plugin/commit/660abcbf054b27a51595ae8b0417220122aaf058)
    from the [Branch API Plugin](https://wiki.jenkins.io/display/JENKINS/Branch+API+Plugin)
-   (Internal: updated to the 2.x plugin parent pom)

### Version 2.3 - 17/06/2016

-   Support for the [Github Integration Plugin](https://github.com/jenkinsci/buildtriggerbadge-plugin/commit/4766f2c55b5b575e98b299944e8ce69e80b97ecc)

### Version 2.2 - 10/10/2015

-   [JENKINS-27225](https://issues.jenkins-ci.org/browse/JENKINS-27225)
    Workflow plugin support (now Workflow jobs also have the
    BuildTriggerBadge icons)
-   Text for column changed from "Last Build Trigger" to just "Cause" so
    that it's short. Particularly important/useful when using the "icon
    only mode" available since 2.1

### Version 2.1 - 01/09/2015

Pull-requests Based Release:

-   Support for the [Stash Pull Request Builder Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Stash+pullrequest+builder+plugin)
    cause, thanks to Alexis Morelle
-   Support for the [URL Trigger Plugin](https://wiki.jenkins-ci.org/display/JENKINS/URLTrigger+Plugin)
    cause, thanks to Lucy Davies
-   [JENKINS-26186](https://issues.jenkins-ci.org/browse/JENKINS-26186)
    Enable changing display format for Last Build Trigger Column, thanks
    Łukasz Jąder

### Version 2.0 - 06/02/2015

-   Support for externally defined badges: one can now [add, override or disable badges through extension points](https://github.com/jenkinsci/buildtriggerbadge-plugin#what-if-i-want-to-add-override-or-disable-a-badge-for-some-build-cause-)

### Version 1.4 - 31/01/2015

-   JENKINS-26233 : support for the [GitHub Pull Request Builder Plugin cause](https://wiki.jenkins-ci.org/display/JENKINS/GitHub+pull+request+builder+plugin)

### Version 1.3 - 24/09/2014

-   Some french translations fixes/adjustments
-   add badge for general Git commit hook cause (thanks Domi)

### Version 1.2 - 29/04/2014

-   Added icons for :
    -   Build Flow Plugin
    -   Naginator Plugin
    -   Bitbucket pushes
-   [JENKINS-22557: Column should have text in tooltip (like history)](https://issues.jenkins-ci.org/browse/JENKINS-22557)
-   Typos fixes in french translations

### Version 1.1

-   Added a column showing cause and short description of the last build
    `->` thanks to Łukasz Jąder (ljader) : [pullrequest #3](https://github.com/jenkinsci/buildtriggerbadge-plugin/pull/3)
-   Added periodic-reincarnation-plugin cause `->` thanks to Mirko
    Friedenhagen (mfriedenhagen) : [pullrequest #4](https://github.com/jenkinsci/buildtriggerbadge-plugin/pull/4)
-   Upgrade required core version to current Jenkins LTS version
    (1.509.2)

### Version 1.0

-   Added M2Release cause as UserCause `->` thanks to Christian Apel
    (christianapel) : [pullrequest #1](https://github.com/jenkinsci/buildtriggerbadge-plugin/pull/1)
-   Added GitPush cause + Fall back on superclass if a cause does not
    have a badge `->` thanks to James Clarke (jrtc27) : [pullrequest #2](https://github.com/jenkinsci/buildtriggerbadge-plugin/pull/2)

### Version 0.9

-   [JENKINS-15474](https://issues.jenkins-ci.org/browse/JENKINS-15474) :
    filter duplicates of the same Cause type and the same description

To remove duplicates of the same Cause type and the same description
(see [JENKINS-15474](https://issues.jenkins-ci.org/browse/JENKINS-15474)),
you can apply this groovy script :

```groovy
import org.jenkinsci.plugins.buildtriggerbadge.BuildTriggerBadgeAction;

for(item in Jenkins.instance.items) {
  for(build in item.builds) {
    def set = new HashSet();
    for(action in build.badgeActions) {
      if(action instanceof BuildTriggerBadgeAction) {
        def filter = action.cause.class.canonicalName + "_" + action.cause.shortDescription
        if(set.contains(filter)) {
        println "removing duplicates BuildTriggerBadgeAction in build " + build.id
            build.actions.remove(action)
            build.save()
        }
        else {
            set.add(filter);
        }
      }
    }
  }
}
```

It's not necessary to apply this script for versions `>= 0.9`.

### Version 0.8

-   [JENKINS-15307](https://issues.jenkins-ci.org/browse/JENKINS-15307)
    : fix images URL when Jenkins is running as non-context root
    (deployed as a tomcat webapp, for example).

### Version 0.7

-   Added a global option to de/activate the plugin

### Version 0.6

-   Added support for builds triggered from command line using
    jenkins-cli.jar (CLICause) and for remotely triggered build
    (RemoteCause).

### Version 0.5

-   initial release.
    -   Shows icons for builds triggered by user, timer, scm or upstream
        cause. Any other cause will currently show a question mark.
# Contributing to SVJIS

Thank you for considering contributing to SVJIS project. Here are some of the ways you can contribute to:

* Contribute new code.
* File bug reports.
* Fix bugs.
* Develop ideas for new features and file them.
* Participate in code reviews.

## How to Contribute Code

New code contributions should be primarily made using GitHub pull requests. This involves you creating a personal fork of the project, adding your new code to a branch in your fork, and then triggering a pull request using the GitHub web UI (it's easier than it sounds). A pull request is both a technical process (to get the code from your branch into the main repository) and a framework for performing code reviews.  

This project uses [Gitflow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow). The branch naming conventions in your fork should follow projects name conventions.  

*For new features:*

* **feature/**meaningful-feature-name
  * ex. `feature/component-error-handler`

*For defects:*

* **fix/**feature-name/short-name-of-problem-being-fixed
  * ex. `fix/error-page-handler/parent-page-lookup`


In many cases, it is worth having a discussion with the community before investing serious time in development. For these cases, create a GitHub issue with a description of the problem you are trying to solve.  

If you already have commit rights, bug fixes and minor updates should just be made in the shared repository itself.  

There's a good guide to performing pull requests at [https://help.github.com/articles/using-pull-requests](https://help.github.com/articles/using-pull-requests). In the terms used in that article, we use both the **Fork & Pull** and the **Shared Repository Model**.

### Before Contributing Code

The best pull request are small and focused. Don't try to change the world in one pull request.

* Add JUnit test for Java code. Our coverage ratio isn't great, but we don't want it to get worse.

## Participating in Code Reviews

Even if you don't have time to contribute code, reviewing code contributed by other people is an option. To do this, go to Pull requests to see the open pull requests.

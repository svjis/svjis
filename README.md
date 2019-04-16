# SVJIS information system

Information system for Owners Associations. See [wiki](https://gitlab.com/svjis/svjis/wikis/home) for more information.  

## Three main branches
* `master` - development branch containing testing version
* `staging` - staging branch containing acceptance test version
* `production` - production branch containing production version

## Development workflow

Developer takes ticket and creates new feature branch **from staging branch**:

1. `git checkout staging`
1. `git pull`
1. `git checkout -b Issue_#3`
1. `git push -u origin Issue_#3`

Developing is done in feature branch:

1. `git status`
1. `git add .`
1. `git commit -m "what have been changed"`
1. `git push`

At the end developer will merge feature branch to master and deploy to test:

1. `git checkout master`
1. `git pull` 
1. `git merge --no-ff origin/Issue_#3`
1. `git push`

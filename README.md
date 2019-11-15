# SVJIS information system

Information system for Owners Associations. See [wiki](https://github.com/berk76/svjis/wiki) for more information.  

## Three main branches
* `develop` - development branch containing testing version
* `staging` - staging branch containing acceptance test version
* `production` - production branch containing production version

## Development workflow

Developer takes ticket and creates new feature branch **from staging branch**:

1. `git checkout staging`
1. `git pull`
1. `git checkout -b Issue_#3`
1. `git push -u origin Issue_#3`

When developing is done in feature branch:

1. `git status`
1. `git add .`
1. `git commit -m "what have been changed"`
1. `git push`

At the end developer will merge feature branch to the develop branch or create pull request:

1. `git checkout develop`
1. `git pull` 
1. `git merge --no-ff origin/Issue_#3`
1. `git push`

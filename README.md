# Fusion of Souls

Fusion of Souls is a Roguelike Auto-Battler that adds a new level of difficult decision-making by allowing you to fuse units and items into a powerful hero opening up a whole new layer of strategy to the genre.

## Branching and Release Strategy

GitHub Actions are setup to build, test, and package the application. The packages are outputted for Windows, MacOS, Ubuntu, and RedHat and these are the packages used for release builds.

### Branching Strategy:

**Primary Branches**
* `current-release` must always reflect the code used to generate the currently playable version on Steam
  * As the game is not currently in a Release state, this branch is the code state of the most recently completed Milestone
* `next-release` must always reflect a mostly-stable, playable version of the game that players can opt-in to preview upcoming changes to the game.
  * This will likely be merged into upon completion of a GitHub Issue
* `working` is the mainline branch where continuous work is merged into from feature branches. This branch is likely to be unstable.

**Secondary Branches**
* Version branches (e.g. `v0.2.0`) are created off of one of the two `release` branches when a release is being prepared. This makes it easy to not have to differentiate between minor or patch releases
* Issue branches (e.g. `FusionOfSouls-20`) are created off of either the `working` branch for work on a future Minor release or off of a Version branch if prepping fixes for a patch release

###Branch and Release Flow

Let's say `current-release` is stable at version `0.1.0`, `next-release` is stable at `0.2.0`, and `working` contains work that is being prepped for `0.3.0`

**Release to `current-release` process**
1. A version branch is created off of `current-release` matching the current version in `next-release`, let's say `v0.3.5` in this case
2. A Pull Request is opened with `next-release` targeting this new Version branch
3. Upon review and testing, this PR will be merged
4. `next-release` is bumped to the next Minor version (`0.4.0` in this example)
5. Final testing is done in the Version branch
6. Release notes are created (hopefully generated?)
7. Upon final release approval, the Version branch is merged into `current-release`
8. A tag is created after this merge
9. A release is created with this current version and the artifacts from the GitHub build are added to the release
10. The release is shipped to Steam

**If a patch is required for the Current Release**
1. A Version branch is created off of `current-release` with the patch version bumped by 1 (e.g. `v0.3.6` following example above)
2. Issue Branches are created off of the Version branch to complete necessary patch work and merged down via PRs on completion
3. Upon completion of work and testing for the patch, the Version branch merged and released following the final steps in the `current-release` process above

**Release/Patch to `next-release` process**

This mimics the `current-release` process using the `working` branch as the source of new code. The main difference is that the tag and release are marked as pre-release, and it's shipped to the Beta branch on Steam.

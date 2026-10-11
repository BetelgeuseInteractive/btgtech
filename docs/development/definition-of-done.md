# Definition of Done

A work package is done only when every item is checked. Decision: ADR [0062](../decisions/0062-definition-of-done.md).

- [ ] `.\gradlew.bat build` succeeds (compilation, formatting, unit tests, architecture tests, data checks).
- [ ] `.\gradlew.bat runData` has been run and the generated output is committed.
- [ ] If behavior changed: `.\gradlew.bat runGameTestServer` succeeds, and the new behavior has a game test.
- [ ] All new translation keys exist in all eight languages.
- [ ] If something visual changed: checked with `.\gradlew.bat runClient` and reported to the owner.
- [ ] If the design changed: affected documents updated; if a decision changed: new ADR written with the owner's approval.
- [ ] No placeholder names, unused files or unowned TODOs.

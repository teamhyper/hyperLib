# hyperLib

[![Build Status](https://travis-ci.org/teamhyper/hyperLib.svg?branch=master)](https://travis-ci.org/teamhyper/hyperLib)

Team HYPER #69 Enhancements to WPILib

Documentation is available on the [project website](https://teamhyper.github.io/hyperLib).

Also check out the [wiki](https://github.com/teamhyper/hyperlib/wiki).

# Release process

We currently publish to a maven repo at <https://hyperlib.hyperonline.org>,
using `gradle publish`.  The publishing job infers the version number from the
most recent *annotated* git tag.  So to do a release (say `2.0.0`), you would
ensure your work is committed and you have master checked out, then run:

```bash
git tag -a v2.0.0
 ./gradlew publish -PhyperlibUser=... -PhyperlibPassword=...
```

Note that the second line starts with a space, so bash will not save the
credentials to `~/.bash_history`.  Ask James or Chris for the necessary
credentials.

# License
Copyright 2016-2019 James Hagborg, Christopher McGroarty, Timothy Nguyen

HYPERLib is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
	
HYPERLib is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with HYPERLib.  If not, see <https://www.gnu.org/licenses/>.

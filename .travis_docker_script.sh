#!/bin/bash

set -e -x

Xvbf :99 & export DISPLAY=:99

cd /repo
./gradlew assemble
./gradlew check

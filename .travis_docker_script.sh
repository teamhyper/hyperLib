#!/bin/bash

set -e -x

Xvfb :99 & export DISPLAY=:99

cd /repo
./gradlew assemble
./gradlew check

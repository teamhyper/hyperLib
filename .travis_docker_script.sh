#!/bin/bash

set -e -x

cd /repo
./gradlew assemble
./gradlew check

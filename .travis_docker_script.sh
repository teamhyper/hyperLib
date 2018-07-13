#!/bin/bash

set -e -x

cd /hyperLib
./gradlew assemble
./gradlew check

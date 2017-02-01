#!/bin/bash

# Exit the script if something messes up, so we don't accidentally the whole thing
set -e

DEPLOY_DIR=$1

echo "Deploying docs to ${DEPLOY_DIR}"

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

echo "Pulling from gh-pages"
mkdir $HOME/gh-pages
cd $HOME/gh-pages
git init
git pull "https://github.com/teamhyper/hyperLib.git" gh-pages

echo "Copying docs over..."
mkdir -p doc/${DEPLOY_DIR}
cp -Rf ${TRAVIS_BUILD_DIR}/build/docs/javadoc/* doc/${DEPLOY_DIR}

echo "Committing and pushing..."
git add -f .
git commit -m "Add latest javadoc from build $TRAVIS_BUILD_NUMBER to $DEPLOY_DIR"
git push -f "https://${GH_TOKEN}@github.com/teamhyper/hyperLib.git" +master:gh-pages &> /dev/null

echo "Docs successfully deployed"

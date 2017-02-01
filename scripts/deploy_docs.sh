#!/bin/bash

# Exit the script if something messes up, so we don't accidentally the whole thing
set -e

DEPLOY_DIR=$1

echo "Deploying docs to ${DEPLOY_DIR}"

echo "Pulling existing github pages..."
# Set up git

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

mkdir $HOME/gh-pages
cd $HOME/gh-pages
echo "debug: cwd is $(pwd)"
git init
git pull "https://${GH_TOKEN}@github.com/teamhyper/hyperLib.git" gh-pages &> /dev/null
ls -la
git status
git branch

echo "Copying docs over..."
# Copy docs over
mkdir -p doc/${DEPLOY_DIR}
cp -Rf ${TRAVIS_BUILD_DIR}/build/docs/javadoc/* doc/${DEPLOY_DIR}

echo "Committing and pushing..."
# Commit and push changes
git add -f .
git commit -m "Add latest javadoc from build $TRAVIS_BUILD_NUMBER to $DEPLOY_DIR"
git push -f "https://${GH_TOKEN}@github.com/teamhyper/hyperLib.git" gh-pages &> /dev/null

echo "Docs successfully deployed"

DEPLOY_DIR=$1

echo "Deploying docs to ${DEPLOY_DIR}"

echo "Pulling existing github pages..."
# Set up git
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages "https://${GH_TOKEN}@github.com/teamhyper/hyperLib" gh-pages &> /dev/null

echo "Copying docs over..."
# Copy docs over
cd gh-pages
mkdir -p doc/${DEPLOY_DIR}
cp -Rf ${TRAVIS_BUILD_DIR}/build/docs/javadoc/* doc/${DEPLOY_DIR}

echo "Committing and pushing..."
# Commit and push changes
git add -f .
git commit -m "Add latest javadoc from build $TRAVIS_BUILD_NUMBER to $DEPLOY_DIR"
git push -fq origin gh-pages &> /dev/null

echo "Docs successfully deployed"

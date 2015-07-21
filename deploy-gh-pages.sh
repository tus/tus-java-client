#!/bin/sh

echo "Publishing javadocs to GitHub pages..."git

set -x

git log
git fetch gh-pages
git checkout gh-pages
git log
git merge master
git log
javadoc io.tus.java.client -sourcepath ./src/main/java -d ./javadoc
git add ./javadoc
git commit -m "Update javadoc for $(git rev-parse HEAD)" || true
git push origin gh-pages
git checkout master

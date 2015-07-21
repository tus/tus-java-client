#!/bin/sh

echo "Publishing javadocs to GitHub pages..."git

set -x

git clone --branch=gh-pages git://github.com/tus/tus-java-client.git ../gh-pages
commit=$(git rev-parse HEAD)
git log
javadoc io.tus.java.client -sourcepath ./src/main/java -d ../gh-pages/javadoc

cd ../gh-pages
git add ./javadoc
git commit -m "Update javadoc for ${commit}" || true
git push origin gh-pages

cd ../tus-java-client

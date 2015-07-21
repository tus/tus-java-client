#!/bin/sh

echo "Publishing javadocs to GitHub pages..."

git clone --quiet --branch=gh-pages https://account:$GITHUB_TOKEN@github.com/tus/tus-java-client.git ../gh-pages
commit=$(git rev-parse HEAD)
javadoc io.tus.java.client -sourcepath ./src/main/java -d ../gh-pages/javadoc

cd ../gh-pages
git add ./javadoc
git commit -m "Update javadoc for ${commit}" || true
git push origin gh-pages --quiet

cd ../tus-java-client

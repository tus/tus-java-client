#!/bin/sh

set -e

# Generate files
./gradlew jar

base_url="https://acconut:$BINTRAY_TOKEN@api.bintray.com/"
version="$TRAVIS_TAG"

# Create new version
curl \
    -X POST \
    $base_url/packages/tus/maven/tus-java-client/versions \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"${version}\",\"vcs_tag\":\"${version}\"}"

# Upload files
curl \
    -X PUT \
    "$base_url/content/tus/maven/tus-java-client/$version/tus-java-client-$version.jar?publish=1" \
    -d @build/libs/tus-java-client.jar

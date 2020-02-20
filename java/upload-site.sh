#!/bin/bash
VERSION=`mvn -f ./pom.xml -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec`
aws s3 sync ./target/site/ s3://mtnfog-public/philter-sdk/java/$VERSION/

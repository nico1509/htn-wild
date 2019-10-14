#!/bin/bash

set -eu

source ./.env.local

echo "--> Clearing LDP..."
curl "${ldbbc_url}" -X DELETE -L

echo "--> Filling LDP..."
find ontologies -name '*.ttl' -exec curl "${ldbbc_url}/" -X PUT -Hcontent-type:text/turtle -T {} -L \; -print
find data       -name '*.ttl' -exec curl "${ldbbc_url}/" -X PUT -Hcontent-type:text/turtle -T {} -L \; -print
#find data       -name '*.ttl' -exec curl "${ldbbc_url}"  -X POST -Hcontent-type:text/turtle -T {} -L \; -print

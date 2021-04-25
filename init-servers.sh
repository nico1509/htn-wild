#!/bin/bash

set -eu

source ./.env.local

echo "--> Clearing LDP..."
curl "${ldbbc_url}" -X DELETE -L

echo "--> Filling LDP..."
find ontologies -name '*.ttl' -exec curl "${ldbbc_url}/" -X PUT -Hcontent-type:text/turtle -T {} -L \; -print
find data       -name 'flamethrower*.ttl' -exec curl "${ldbbc_url}/" -X PUT -Hcontent-type:text/turtle -T {} -L \; -print
#find rules      -name '*.n3' -exec curl "${ldbbc_url}/"  -X PUT -Hcontent-type:application/n3 -T {} -L \; -print

#!/bin/bash

source ./.env.local

curl -f -X PUT -T wild-demo/wild-vocab.ttl "${ldbbc_url}"/wild-vocab.ttl -L -Hcontent-type:text/turtle || exit 3
curl -f -X PUT -T wild-demo/list-vocab.ttl "${ldbbc_url}"/list-vocab.ttl -L -Hcontent-type:text/turtle || exit 4

JAVA_OPTS=-Dldfu.optimiser=OFF "${ldfu}" \
\
`   # WiLD-specific rules`\
    -p wild-demo/data-retrieval-ldpc.n3 \
    -p wild-demo/speaker.n3 \
    -p wild-demo/wild-semantics.n3 \
    -p wild-demo/list-semantics.n3 \
    -p wild-demo/selected_ontology_semantics.n3 \
    -p wild-demo/spin_sparql_ask_where_query_processing.n3 \
\
`   # add the rest of the CLI parameters to LDFU`\
    $@

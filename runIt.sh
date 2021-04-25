#!/bin/bash

set -eu

source ./.env.local

$ldfu \
`     # Generic ontologies and semantics:`\
      -i http://people.aifb.kit.edu/co1683/2017/list/vocab     `# was: http://purl.org/list/vocab `    \
      -p http://people.aifb.kit.edu/co1683/2017/list/semantics `# was: http://purl.org/list/semantics` \
      -p rules/spin_sparql_ask_where_query_processing.n3 \
      -p rules/selected_ontology_semantics.n3 \
\
`     # Data retrieval:`\
      -p rules/data-retrieval*.n3 \
\
`     # HTN:`\
      -p rules/htn-*.n3 \
\
`     # add the rest of the CLI parameters to LDFU`\
      $@

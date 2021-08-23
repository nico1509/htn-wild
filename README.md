# Planning and Executing a Manufacturing Workflow in a Virtual Manufacturing Environment with Linked Data Interfaces

This is the example implementation for our demo submission for ATAC21.

## Components
### Ontologies/Rules

In these directories the vocabulary along with the operational semantics is stored as Turtle and N3 files. Every file name that belongs to the HTN Ontology starts with htn* . The vocab and the rules are commented with explanations. Other files include the list vocab taken from the WiLD setup and some implementation-specific rules for data retrieval.

### Data

In this directory you can store your planning problem descriptions. For reference, an example description for building a flamethrower is included.

## How to run this submission
First set up the environment, then run the startup scripts.

### How to setup your environment
* Clone this repository _recursively_ `git clone --recursive https://github.com/nico1509/htn-wild`
* Download [Linked Data-Fu](https://linked-data-fu.github.io/)
* Move `.env` to `.env.local` and adapt the paths/urls
* Setup your `/etc/hosts` so that *tok450s.lan* points to your local IP
* Start [LDBBC](https://github.com/kaefer3000/ldbbc) and the virtual factory by using the `start-servers.sh` script (stop using the `stop-servers.sh` script).

### Startup scripts
1. `init-servers.sh` to upload vocabulary, problem description and instances to LDBBC
2. `runIt.sh -n` to start the HTN decomposition. One way to see that the decomposition is done is to check that there are no more unsafe requests (PUT/POST/DELETE) in the console output.
3. `runWild.sh -n` to start the generated workflows

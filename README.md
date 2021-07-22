# Planning and Executing a Manufacturing Workflow in a Virtual Manufacturing Environment with Linked Data Interfaces

This is the example implementation for our demo submission for ATAC21.

## Components
### Ontologies/Rules

In these directories the vocabulary along with the operational semantics is stored as Turtle and N3 files. Every file name that belongs to the HTN Ontology starts with htn* . The vocab and the rules are commented with explanations. Other files include the list vocab taken from the WiLD setup and some implementation-specific rules for data retrieval.

### Data

In this directory you can store your planning problem descriptions. For reference, an example description for building a flamethrower is included. To test it, first set up the environment as described below, then run the following scripts:

1. `init-servers.sh` to upload vocab, problem description and instances to LDBBC
2. `runIt.sh -n` to start the decomposition
3. `runWild.sh -n` to start the generated workflows

How to setup your environment

* Move `.env` to `.env.local` and adapt the paths/urls
* Start [LDBBC](https://github.com/kaefer3000/ldbbc) and the virtual factory by using the `start-servers.sh` and `stop-servers.sh` scripts.
* Setup your `/etc/hosts` so that *tok450s.lan* points to your local IP
* Download [Linked Data-Fu](https://linked-data-fu.github.io/) and adapt the executable path in `.env.local`

# HTN4WiLD

This is the example implementation for our paper "Integrated Planning and Execution on Read-Write Linked Data".

## Components
### Ontology

In this directory the vocabulary along with the operational semantics is stored as Turtle and N3 files. Every file name that belongs to the HTN Ontology starts with htn* . The vocab and the rules are commented with explanations. For details refer to the paper. Other files include the list vocab taken from the WiLD setup and some implementation-specific rules for data retrieval.

### Description

In this directory you can store your planning problem descriptions. For reference, an example description for building a stool is included. To test it, decide for one goal by changing the symlink of `data/stool-Problem.ttl` to the `RED` or `GREEN` Problem. Then run the following scripts:

1. `init-servers.sh` to upload vocab, problem description and instances to LDBBC
2. `runIt.sh [-n 100]` to start the decomposition
3. `runWild.sh [-n 100]` to start the generated workflows

Do not forget to setup your environment (copy `.env` to `.env.local`):

* [LDBBC](https://github.com/kaefer3000/ldbbc) needs to run and be accessible via `http://tok450s.lan/ldbbc/` &rightarrow; see `servers/ldbbc-lists`
* [Linked Data-Fu](https://linked-data-fu.github.io/) needs to be installed and executable

### Example UI

Along with the stool example comes a very basic UI to simulate an IoT device performing actions of an robotic arm. Run it with `npm start` in `servers/stool` as `http://localhost:3000/`. Then open the HTML file in the `web/` directory with a web browser.

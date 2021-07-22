# jmonkey-ld
Linked Data (REST+RDF) interface to JMonkey3, a virtual reality / game engine written in Java. Since 2014.

The code served as part of our [demo High Performance Linked Data Processing for Virtual Reality Environments](http://people.aifb.kit.edu/ja9784/publications/2014/iswc2014demo/) at the [2014 edition of the International Semantic Web Conference](http://iswc2014.semanticweb.org/).

## Build
* Obviously, you need Java. Tested with Java 8.
* To build, you also need Maven.
* Have NxParser version 3.0.0-SNAPSHOT installed. If not, quickly:

````
cd /tmp/
git clone https://github.com/nxparser/nxparser
cd nxparser
mvn clean install -Dmaven.test.skip=true
````

* Then:

````
mvn package
````

## Run
Deploy the `.war` file on a Servlet engine such as Apache Tomcat or Eclipse Jetty, or simply use:

````
mvn jetty:run
````

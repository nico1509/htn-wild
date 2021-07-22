#!/bin/bash

if [ ! -f servers/ldbbc-lists.pid ]; then
  cd servers/ldbbc-lists && mvn -Djetty.port=80 jetty:run > /dev/null &
  echo $! > servers/ldbbc-lists.pid
else
  echo "ldbbc already running"
fi

if [ ! -f servers/jmonkey-ld-master.pid ]; then
  cd servers/jmonkey-ld-master && mvn -Djetty.port=8080 jetty:run > /dev/null &
  echo $! > servers/jmonkey-ld-master.pid
else
  echo "jmonkey-ld-master server already running"
fi

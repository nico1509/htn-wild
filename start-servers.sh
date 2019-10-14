#!/bin/sh

if [ ! -f servers/ldbbc-lists.pid ]; then
  cd servers/ldbbc-lists && mvn -Djetty.port=80 jetty:run > /dev/null &
  echo $! > servers/ldbbc-lists.pid
else
  echo "ldbbc already running"
fi

if [ ! -f servers/stool.pid ]; then
  cd servers/stool && node index.js > /dev/null &
  echo $! > servers/stool.pid
else
  echo "stool server already running"
fi



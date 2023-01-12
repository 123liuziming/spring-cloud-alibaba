#!/bin/bash

if [ ! -z $DEBUG_MODE ]; then
  echo "OK"
  exit 0
fi


PORT=80
if [ $SERVER_PORT ] ; then
  PORT=$SERVER_PORT
fi

ret=`curl http://127.0.0.1:$PORT/checkpreload`
if [ "$ret" = "success" ];then
	echo "OK"
	exit 0
else
  ret=`curl http://127.0.0.1:8080/checkpreload`
  if [ "$ret" = "success" ];then
  	echo "OK"
  	exit 0
  else
    echo "ERROR"
  	exit 1
  fi
fi

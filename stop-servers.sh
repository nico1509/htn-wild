#!/bin/bash

find servers -name '*pid' -exec pkill -F {} \; -delete
find servers -name '*pid' -delete

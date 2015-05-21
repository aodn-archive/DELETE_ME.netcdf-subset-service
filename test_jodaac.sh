#!/bin/bash
#set -x

NUM_JOBS=${1:-4}
NUM_CONCURRENT_JOBS=${2:-$NUM_JOBS}

JODAAC_URL="http://jodaac-edge.aodn.org.au/jodaac/subset"
JODAAC_DATA="typeName=anmn_ts&CQL_FILTER=INTERSECTS(geom,POLYGON((113.33 -33.09,113.33 -30.98,117.11 -30.98,117.11 -33.09,113.33 -33.09))) AND TIME >= %272015-01-13T23:00:00Z%27 AND TIME <= %272015-04-14T00:00:00Z%27"

echo "Starting $NUM_JOBS jobs..."
seq "$NUM_JOBS" | xargs -I ___SEQ___ -P$NUM_CONCURRENT_JOBS curl -v $JODAAC_URL -o download___SEQ___.zip -d "$JODAAC_DATA"

find -type f -size 0b -iname "download*.zip" -exec rm {} \;

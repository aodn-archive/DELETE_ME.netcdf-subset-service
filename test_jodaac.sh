#!/bin/bash
#set -x

NUM_JOBS=${1:-4}
NUM_CONCURRENT_JOBS=${2:-$NUM_JOBS}

JODAAC_HOST="http://localhost:8080/netcdf-subset"
JODAAC_URL="$JODAAC_HOST/subset?typeName=anmn_ts&CQL_FILTER=INTERSECTS(geom,POLYGON((113.33 -33.09,113.33 -30.98,117.11 -30.98,117.11 -33.09,113.33 -33.09))) AND TIME >= %272015-01-13T23:00:00Z%27 AND TIME <= %272015-04-14T00:00:00Z%27"
JODAAC_COMMAND="wget '$JODAAC_URL' -O 'download{#}.zip'"

echo "Starting $NUM_JOBS jobs..."
seq "$NUM_JOBS" | parallel -j"$NUM_CONCURRENT_JOBS" $JODAAC_COMMAND

find -type f -size 0b -iname "download*.zip" -exec rm {} \;

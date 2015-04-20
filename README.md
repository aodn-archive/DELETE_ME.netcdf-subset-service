# NetCDF Subset Service

[![Build Status](https://travis-ci.org/aodn/netcdf-subset-service.png?branch=master)](https://travis-ci.org/aodn/netcdf-subset-service)


## Prerequisites

This project uses the grails wrapper, therefore the only prerequisite is a JRE/JDK.  To run:

```
$ ./grailsw run-app
```

## Usage

```
http://localhost:8080/netcdf-subset/subset?typeName=${LAYER_NAME}&CQL_FILTER=INTERSECTS(${GEOMETRY_ATTR},${GEOMETRY_LIT}) AND ${TIMESTAMP_ATTR} >= '${FROM_TIMESTAMP}' AND $(TIMESTAMP_ATTR) <= '${TO_TIMESTAMP}'
```

Where

 * LAYER_NAME is the geoserver layer name
 * GEOMETRY_ATTR is the layer's geometry attribute
 * GEOMETRY is a geometry expressed in WKT format
 * TIMESTAMP_ATTR is the timestamp attribute to filter on
 * FROM_TIMESTAMP is the from time in [ISO 8601 Combined date and time format][1]
 * TO_TIMESTAMP is the to time in [ISO8601 Combined date and time format][1]

Example (using `curl` and encoding URL parameters):

```
$ curl -L "http://localhost:8080/netcdf-subset/subset" \
       --data-urlencode "typeName=anmn_ts_timeseries_data" \
       --data-urlencode "CQL_FILTER=INTERSECTS(geom,POLYGON((113.33 -33.09,113.33 -30.98,117.11 -30.98,117.11 -33.09,113.33 -33.09))) AND TIME >= '2015-01-13T23:00:00Z' AND TIME <= '2015-04-14T00:00:00Z'" \
       > test.zip 2> /dev/null

$ unzip -l test.zip 
Archive:  test.zip
 Length   Method    Size  Ratio   Date   Time   CRC-32    Name
--------  ------  ------- -----   ----   ----   ------    ----
      18  Defl:N       20 -11%  04-20-15 11:15  082c8fdd  TestFile.nc
--------          -------  ---                            -------
      18               20 -11%                            1 file
```

[1]: http://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations

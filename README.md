# NetCDF Subset Service

## Usage

### 

    http://localhost:8080/netcdf-subset?typeName=${LAYER_NAME}&CQL_FILTER=INTERSECTS(${GEOMETRY_ATTR},${GEOMETRY_LIT}) AND {$TIMESTAMP_ATTR} >= '${FROM_TIMESTAMP}' AND ($TIMESTAMP_ATTR) <= '${TO_TIMESTAMP}'

Where 

 * LAYER_NAME is the geoserver layer name
 * GEOMETRY_ATTR is the layer's geometry attribute
 * GEOMETRY is a geometry expressed in WKT format
 * TIMESTAMP_ATTR is the timestamp attribute to filter on
 * FROM_TIMESTAMP is the from time in [ISO 8601 Combined date and time format][1]
 * TO_TIMESTAMP is the to time in [ISO8601 Combined date and time format][1]

Example:

    http://localhost:8080/netcdf-subset?typeName=anmn_ts_timeseries_data&CQL_FILTER=INTERSECTS(geom,POLYGON((113.3349609375 -33.091796875,113.3349609375 -30.982421875,117.1142578125 -30.982421875,117.1142578125 -33.091796875,113.3349609375 -33.091796875))) AND TIME >= '2015-01-13T23:00:00Z' AND TIME <= '2015-04-14T00:00:00Z'

[1]: http://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations

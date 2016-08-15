# Features

## GET
* All standard paths
* Navigating to sub-properties of complex properties. e.g.: `Thing(1)/properties/property1`
* property/$value
* Collection/$ref


## POST, PUT, PATCH
* String and Numeric observation results. Json type is exaclty retained (if you put in 2.00, you get 2.00 back out)
* In-line objects, nested as deep as you want
* GeoJSON geospatial objects for Location and FeatureOfInterest. Other encodings are accepted but can not be used in filters


## DELETE
* Data integrity is maintained. Deleting a Thing will also delete the Datastreams and Observations of that Thing.


## Supported queries:
* $top
* $skip
* $count
* $select
  As per OData:
  * $select on navigation properties adds the navigationLink to the item
  * $expand on navigation properties adds the content of the item
  * If you want both the navigation link and the content, use both $select and $expand
* $expand - Including full nesting. Each expand can have all query parameters ($top, $skip, $count, $select, $expand, $orderby and $filter)
* $orderby - Includes full function support of all functions listed under $filter
* $filter - Full support, except for geo.length and time.
  * Arbitrary deep searches. For instance, you can find all Things that have ever been in a certain location with:
    * `.../v1.0/Things?$filter=st_within(HistoricalLocations/Location/location, geography'POLYGON((7.5 51.5, 7.5 53.5, 8.5 53.5, 8.5 51.5, 7.5 51.5))')`
  * Unfortunately, SensorThingsAPI nor OData defines functions for interval logic like overlaps, starts, meets, etc.
    * When comparing time intervals (`Datastream/phenomenonTime`, `Datastream/resultTime`, `Observation/validTime` and (possibly) `Observation/phenomenonTime`) to time instants:
      * equal tests of the time instant is inside the interval (inclusive)
      * not equal tests of the time instant is outside the interval (exclusive)
      * `gt`, `ge`, `lt`, `le` work as expected
    * When comparing a time interval to a time interval
      * equals test if the start and end times are the same
      * `gt` and `lt` test whether the end of one is greater / less than the start of the other
      * `ge` and `le` give an error, since it is not clear what is expected in this case
    * Durations can be specified in a url as `duration'PnYnMnDTnHnMnS'`
    * Subtracting two times results in a duration
    * Adding / subtracting a duration to / from a datetime results in a datetime
    * Adding / subtracting a duration to / from a time interval results in a time interval
  * When filtering on Observations/result:
    * If the filter is numeric ($filter=result gt 3), string results are ignored
    * If the filter is string-based ($filter=result gt '3') numbers are cast to strings. This might need discussion.


## To Do:
* geo.length() is not implemented yet
* time() Since the server converts all times to UTC, the OData specification states that the time function should throw an error if it is given a time that is not UTC. Currently time() does not.
* Support for results types other than string and numeric:
  * boolean
  * object
  * array
* The Datastream field observedArea is not yet auto generated
* Backends for different databases
* Investigate which additional database indices are needed
* Configure if navigationLink should be relative or absolute.
* Extensions:
  * Batch Requests
  * MultiDatastream extension
  * Data Array Extension
  * MQTT Extension


## Conformance Test Compliance

OGC publishes a conformance test at [GitHub](https://github.com/opengeospatial/ets-sta10). The goal of this implementation of the API is to be fully compliant with the test suite. This test suite is still a work in progress, so as the tests change, the implementation might no longer pass.
At the same time, the test suite is only intended to be a minimum requirement for implementations. Therefore we have a fork of the test suite with additional tests, published [here](https://github.com/hylkevds/ets-sta10).
Differences between the two versions:
* The official version does not yet implement the `name` property for Thing, Sensor, Datastream, Location and FeatureOfInterest. See https://github.com/opengeospatial/ets-sta10/pull/13
* The official version does not check whether the server returns numeric observation results as numeric, and string results as strings. See https://github.com/opengeospatial/ets-sta10/pull/15
* The official version only tests String comparisons on Observation results, not numerical comparisons. See https://github.com/opengeospatial/ets-sta10/issues/14
* The official version does not yet test operator precedence. See https://github.com/opengeospatial/ets-sta10/pull/17
* The official version does not yet test any functions.
* The official version does not yet test time interval and time duration handling.

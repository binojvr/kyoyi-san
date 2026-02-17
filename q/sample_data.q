/ sample_data.q - populate tables with example records

// load schema if not already
\l schema.q

// sample bonds
`bonds insert (`JGB2Y; 0.0025; 2028.03.20D00:00:00.000; 100);
`bonds insert (`JGB5Y; 0.005; 2031.03.20D00:00:00.000; 100);
`bonds insert (`JGB10Y; 0.01; 2036.03.20D00:00:00.000; 100);

// sample yield curve (zero rates)
`yields insert (`2Y; 0.0020);
`yields insert (`5Y; 0.0045);
`yields insert (`10Y; 0.0090);

// sample portfolio
`portfolios insert (`PORT1; `JGB2Y; 1000);
`portfolios insert (`PORT1; `JGB10Y; 500);

// example scenario record
`scenarios insert (uuid[]; `parallel; 0.0001; .z.p);

show bonds;
show yields;
show portfolios;
show scenarios;

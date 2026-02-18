/ sample_data.q - populate tables with example records

/ sample bonds
bonds,: enlist (`JGB2Y; 0.0025; 2028.03.20T00:00:00.000; 100f)
bonds,: enlist (`JGB5Y; 0.005; 2031.03.20T00:00:00.000; 100f)
bonds,: enlist (`JGB10Y; 0.01; 2036.03.20T00:00:00.000; 100f)


/ sample yield curve
yields,: enlist (`2Y; 0.0020)
yields,: enlist (`5Y; 0.0045)
yields,: enlist (`10Y; 0.0090)

/ sample portfolio
portfolios,: enlist (`PORT1; `JGB2Y; 1000)
portfolios,: enlist (`PORT1; `JGB10Y; 500)

/ example scenario
scenarios,: enlist (`SCN1; `parallel; 0.0001; .z.z)

show bonds
show yields
show portfolios
show scenarios

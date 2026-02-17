/ check_math.q - verify analytic functions against known values

\l analytics.q

/ simple test: price of zero-coupon bond
expected:95.1229
computed:price[0.0;100;0.005;5]

// more tests can be added

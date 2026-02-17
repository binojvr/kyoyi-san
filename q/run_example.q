// run_example.q - load everything and execute a scenario
\l schema.q
\l sample_data.q
\l analytics.q

/ perform a 1bp parallel shift
print "=== Scenario: +1bp parallel shift ===";
result: runScenario 0.0001;
show result;

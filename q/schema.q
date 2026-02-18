/ schema.q - define tables for bonds, yields, portfolios

// bonds: unique identifier, coupon rate, maturity date, face value
bonds:([] id:`symbol$(); coupon:`float$(); maturity:`datetime$(); face:`float$())

// yields: tenor label and associated zero rate
yields:([] tenor:`symbol$(); rate:`float$())

// portfolios holds positions: portfolio id, bond id, quantity
portfolios:([] portfolio_id:`symbol$(); bond_id:`symbol$(); quantity:`long$())

// scenarios logs the requests made via Java or user input
scenarios:([] id:`symbol$(); scenario_type:`symbol$(); shift:`float$(); request_ts:`datetime$())


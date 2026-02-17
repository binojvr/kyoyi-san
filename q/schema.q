/ schema.q - define tables for bonds, yields, portfolios

// bonds: unique identifier, coupon rate, maturity date, face value
bonds:([] id:`symbol$(); coupon:0.0; maturity:`timespan$(); face:1.0)

// yields: tenor label and associated zero rate
yields:([] tenor:`symbol$(); rate:0.0)

// portfolios holds positions: portfolio id, bond id, quantity
portfolios:([] portfolio_id:`symbol$(); bond_id:`symbol$(); quantity:0)

// scenarios logs the requests made via Java or user input
scenarios:([] id:`uuid$(); type:`symbol$(); shift:0.0; requested:.z.p)

@test[`bonds;()] / simple assertion to ensure table exists

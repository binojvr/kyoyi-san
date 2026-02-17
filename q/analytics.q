// analytics.q - pricing and risk functions for fixed-rate bonds

/ price: clean price given coupon, face, ytm, time to maturity
// assume annual coupons and act/365
price:{[coupon;face;ytm;t]
    // present value of coupons + redemption
    n:floor t;                / whole years
    frac:t - n;
    pvCoupons:coupon*face * sum 1%(1+ytm) til n;
    pvRedemption:face*(1+coupon) % pow[1+ytm; t];
    pvCoupons + face*(coupon)% (1+ytm) + (face)% pow[1+ytm; t];
}

/ duration: Macaulay duration
duration:{[coupon;face;ytm;t]
    n:floor t;
    times:til n + 1;              / integer year times for cashflows
    cashflows:face*coupon 1+0*til n; / each year coupon, last year includes principal
    cashflows[n]:face*(1+coupon);
    disc:1 % pow[1+ytm; times];
    weighted:times * cashflows * disc;
    sum weighted % (sum cashflows * disc)
}

/ convexity
convexity:{[coupon;face;ytm;t]
    n:floor t;
    times:til n + 1;
    cashflows:face*coupon 1+0*til n;
    cashflows[n]:face*(1+coupon);
    disc:1 % pow[1+ytm; times];
    sum times*(times+1) * cashflows * disc % pow[1+ytm;2] % (sum cashflows * disc)
}

/ dv01: derivative of price w.r.t. 1bp move
// approximate using small shift
dv01:{[coupon;face;ytm;t]
    shift:0.0001;
    p0:price[coupon;face;ytm;t];
    p1:price[coupon;face;ytm+shift;t];
    (p1-p0)*100
}

/ scenario: parallel shift
parallelShift:{[curve;shift]
    update rate:rate+shift from curve
}

/ example scenario run: apply to yields and compute portfolio P&L
runScenario:{[shift]
    / apply a parallel shift to the global yield curve (returns new curve)
    newYields:parallelShift[yields;shift];
    / join portfolio with bond static data
    pos:bonds lj `id xkey portfolios;
    / compute time to maturity in years (Act/365)
    pos:update t:((maturity - .z.D) % 365) from pos;
    pos:update oldPrice:price[coupon;face;rate; t] from pos;
    pos:update newPrice:price[coupon;face;rate+shift; t] from pos;
    pos:update pnl:(newPrice-oldPrice)*quantity;
    select portfolio_id, bond_id, quantity, oldPrice, newPrice, pnl from pos
}

package com.wellsfargo.cmt.option;

import com.wellsfargo.cmt.model.OptionData;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.*;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Target;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by hems on 07/05/19.
 */
@Component
public class PriceCalculator {


    public double price(OptionData optionData, double underlying) {

        final Option.Type type = Option.Type.Call;
        //String optionName = optionData.getOptionName();
		/* @Rate */final double riskFreeRate = 0.0256;
        double impVol = optionData.getVolatility();
        if (impVol == 0.0)
            impVol = 1;
        final double volatility = impVol;
        final double dividendYield = 0.00;
        // set up dates
        final Calendar calendar = new Target();
        final Date todaysDate = new Date(new java.util.Date());
        new Settings().setEvaluationDate(todaysDate);
        LocalDate localDate = optionData.getExpiryDate();
        Date expiryDate = new Date(java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        final DayCounter dayCounter = new Actual365Fixed();
        final Exercise europeanExercise = new EuropeanExercise(expiryDate);

        // bootstrap the yield/dividend/volatility curves
        final Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(
                underlying));
        final Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(
                new FlatForward(todaysDate, dividendYield, dayCounter));
        final Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(
                new FlatForward(todaysDate, riskFreeRate, dayCounter));
        final Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(
                new BlackConstantVol(todaysDate, calendar, volatility,
                        dayCounter));
        final Payoff payoff = new PlainVanillaPayoff(type, optionData.getStrike());

        final BlackScholesMertonProcess bsmProcess = new BlackScholesMertonProcess(
                underlyingH, flatDividendTS, flatTermStructure, flatVolTS);

        // European Options
        final VanillaOption europeanOption = new EuropeanOption(payoff,
                europeanExercise);

        europeanOption.setPricingEngine(new AnalyticEuropeanEngine(bsmProcess));
        // Black-Scholes for European
        return europeanOption.NPV();
    }
}

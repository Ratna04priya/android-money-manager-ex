package com.money.manager.ex.investment;

/**
 * Factory for security price updater.
 * Set here when changing the updater.
 */
public class SecurityPriceUpdaterFactory {
    public static ISecurityPriceUpdater getUpdaterInstance() {
        // currently using Yahoo.
        ISecurityPriceUpdater updater = new YahooSecurityPriceUpdater();
        return updater;
    }
}

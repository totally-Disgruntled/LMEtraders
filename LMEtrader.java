package com.LME.trading;

import com.LME.internal.LMEselect;
import com.LME.internal.LMEprice;
import java.util.*;
import java.util.concurrent.*;public class LMETradingEngine {    private final LMEselect selector;
    private final LMEprice priceFeed;
    private final ExecutorService executor;    public LMETradingEngine(LMEselect selector, LMEprice priceFeed) {
        this.selector = selector;
        this.priceFeed = priceFeed;
        this.executor = Executors.newFixedThreadPool(4);
    }    public void startEngine() {
        System.out.println("Starting LME Trading Engine...");
        executor.submit(this::processMarketData);
        executor.submit(this::evaluateOrders);
    }    private void processMarketData() {
        while (true) {
            try {
                double currentPrice = priceFeed.getLatestPrice("XAU");
                System.out.println("Current XAU Price: " + currentPrice);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }    private void evaluateOrders() {
        while (true) {
            try {
                List<String> selectedOrders = selector.selectEligibleOrders();
                for (String orderId : selectedOrders) {
                    System.out.println("Processing Order: " + orderId);
                    // Simulate order execution
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }    public void shutdown() {
        System.out.println("Shutting down LME Trading Engine...");
        executor.shutdownNow();
    }    public static void main(String[] args) {
        LMEselect selector = new LMEselect();
        LMEprice priceFeed = new LMEprice();
        LMETradingEngine engine = new LMETradingEngine(selector, priceFeed);
        engine.startEngine();        RuntLME.getRuntLME().addShutdownHook(new Thread(engine::shutdown));
    }
}

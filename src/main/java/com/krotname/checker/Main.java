package com.krotname.checker;

import com.krotname.checker.ui.CheckerUiServer;

import java.io.IOException;

public final class Main {
    private static final int DEFAULT_SERVER_PORT = 8080;

    /**
     * Supports three startup modes:
     * - no args: print usage/help;
     * - --help: print usage/help;
     * - --server [port]: start embedded HTTP UI;
     * - otherwise: treat the first arg as INN and print a single check result.
     */
    public static void main(String[] args) {
        if (args.length == 0 || "--help".equalsIgnoreCase(args[0])) {
            printHelp();
            return;
        }

        if ("--server".equalsIgnoreCase(args[0])) {
            int port = DEFAULT_SERVER_PORT;
            if (args.length >= 2) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Port should be an integer.");
                    printHelp();
                    return;
                }
            }
            runServer(port);
            return;
        }

        String inn = args[0];
        CheckerCorporate checker = new CheckerCorporate();
        System.out.println(checker.check(inn).message());
    }

    private static void runServer(int port) {
        // Isolated startup path: all CLI server orchestration is centralized here for predictable local run behavior.
        try {
            CheckerCorporate checker = new CheckerCorporate();
            CheckerUiServer server = new CheckerUiServer(checker, port);
            int boundPort = server.start();
            System.out.printf("Checker UI server started at http://localhost:%d%n", boundPort);
            // Keep the process alive; this is a demo-only console process container.
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Server thread interrupted.");
        } catch (IOException e) {
            String message = e.getMessage() == null ? "unknown IO error" : e.getMessage();
            System.out.printf("Failed to start server: %s%n", message);
        }
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  java -jar checker-corporate.jar <ИНН>");
        System.out.println("  java -jar checker-corporate.jar --server [port]");
        System.out.println("  java -jar checker-corporate.jar --help");
        System.out.println("");
        System.out.println("Environment variable: DADATA_TOKEN");
        System.out.println("Or resources/checker.properties with key token=...");
    }
}

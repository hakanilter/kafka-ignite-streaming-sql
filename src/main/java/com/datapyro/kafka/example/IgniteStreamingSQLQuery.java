package com.datapyro.kafka.example;

import com.datapyro.kafka.ignite.NetworkSignalIgniteRepository;

import java.util.Arrays;
import java.util.List;

/**
 * This class connects Ignite instance and displays multiple SQL query results continuously
 */
public class IgniteStreamingSQLQuery implements Runnable {

    private static final long POLL_TIMEOUT = 3000;

    private static final List<String> QUERIES = Arrays.asList(
            "SELECT deviceId, SUM(rxData) AS rxTotal, SUM(txData) AS txTOTAL FROM NetworkSignalEntity " + "GROUP BY deviceId ORDER BY rxTotal DESC, txTotal DESC LIMIT 5",
            "SELECT networkType, SUM(rxData) AS rxTotal, SUM(txData) AS txTotal FROM NetworkSignalEntity GROUP BY networkType",
            "SELECT networkType, AVG(rxSpeed) AS avgRxSpeed, AVG(txSpeed) AS avgTxSpeed FROM NetworkSignalEntity GROUP BY networkType");

    @Override
    public void run() {
        System.out.println("Initializing Ignite queries...");
        NetworkSignalIgniteRepository networkSignalRepository = new NetworkSignalIgniteRepository();
        try {
            List<List<?>> rows = null;
            do {
                // wait for incoming data
                try {
                    Thread.sleep(POLL_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                for (String sql : QUERIES) {
                    rows = networkSignalRepository.sqlQuery(sql);
                    System.out.println("------------------------------------------");
                    for (List<?> row : rows) {
                        System.out.println(row);
                    }
                }
                System.out.println();
            }
            while (rows != null && rows.size() > 0);
        } finally {
            networkSignalRepository.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new IgniteStreamingSQLQuery().run();
    }

}

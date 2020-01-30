
import java.util.*;
import java.io.*;

/**
 * Arbitrage class
 */
public class Arbitrage {
    /**
     * This function reads in a file containing exchange rate information
     * (we've done this for you),
     * and should create a weighted, directed graph
     * <p>
     * The output should be a list of currencies (i.e. vertices) that you can follow
     * to create an arbitrage opportunity
     * <p>
     * e.x. if trading from currency 1 to 2 to 3 back to 1 yields an arbitrage
     * opportunity, you should output a list containing {1, 2, 3, 1}
     * <p>
     * If no arbitrage opportunity is present, you should output an empty list
     */
    public static List<Integer> arbitrageOpportunity(String filename) throws IOException {

        //parses the input file into a list of exchange rates
        //see the comments above readExchangeRates for details on its output
        double[][] exchangeRates = readExchangeRates(filename);
        List<Integer> result = new ArrayList<>(exchangeRates.length);
        double[][] logExchangeRates = new double[exchangeRates.length][exchangeRates.length];
        for (int i = 0; i < exchangeRates.length; i++) {
            for (int j = 0; j < exchangeRates[0].length; j++) {
                logExchangeRates[i][j] = -Math.log(exchangeRates[i][j]);
            }
        }
        int[][] next = new int[exchangeRates.length][exchangeRates.length];
        for (int i = 0; i < exchangeRates.length; ++i) {
            for (int j = 0; j < exchangeRates.length; ++j) {
                next[i][j] = j;
            }
        }
        for (int k = 0; k < exchangeRates.length; k++) {
            for (int i = 0; i < exchangeRates.length; i++) {
                for (int j = 0; j < exchangeRates[0].length; j++) {
                    if (logExchangeRates[i][j] > logExchangeRates[i][k] + logExchangeRates[k][j]) {
                        logExchangeRates[i][j] = logExchangeRates[i][k] + logExchangeRates[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }

            for (int i = 0;i < logExchangeRates.length; i++) {
                if (logExchangeRates[i][i] < 0) {
                    int[] coordinates = new int[logExchangeRates.length];
                    int j = 0;
                    for (int l = 0; l < logExchangeRates.length; l++) {
                        if (logExchangeRates[l][l] < 0) {
                            coordinates[j++] = l + 1;
                        }
                    }
                    for (int coord : coordinates) {
                        if (coord == 0) {
                            continue;
                        }
                        --coord;
                        int prev = next[coord][coord];
                        result.add(coord);
                        while (prev != coord) {
                            result.add(prev);
                            prev = next[prev][coord];
                        }
                        result.add(coord);
                        break;
                    }
                    return result;
                }
            }
        }
        return result;
    }


    /**
     * You don't have to modify this function
     * <p>
     * Parses a file containing exchange rates between countries into an NxN 2d array
     * <p>
     * In general, if we have two countries i and j,
     * arr[i][j] gives the exchange rate from country i to j.
     * <p>
     * I.e. if arr[i][j] = 4.00, then 1 quantity of currency i can be exchanged
     * for 4.00 quantities of currency j
     */
    public static double[][] readExchangeRates(String filename) throws IOException {
        //System.out.println("starting to read exchange rates");
        BufferedReader br = new BufferedReader(new FileReader(filename));

        //first line contains the number of countries
        int n = Integer.parseInt(br.readLine());
        double[][] exchangeRates = new double[n][n];

        //parse the file as a 2d array
        //in general, element j in row i has the exchange rate from country i to country j
        for (int i = 0; i < n; i++) {
            String[] line = br.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                double rate = Double.parseDouble(line[j]);
                exchangeRates[i][j] = rate;
            }
        }
        br.close();
        return exchangeRates;

    }

}

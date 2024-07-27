package UNIT_TEST;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class PsaAuctionPrices {

    private static final int PAGE_MAX = 250;
    private static final String SCRAPE_URL = "https://www.psacard.com/auctionprices/GetItemLots";
    private static final String EXAMPLE_URL = "https://www.psacard.com/auctionprices/baseball-cards/1967-topps/mets-rookies/values/187370";

    private String cardUrl;

    public PsaAuctionPrices(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public void scrape() throws IOException {
        System.out.println("Collecting data for " + cardUrl);

        // Extract card ID from URL
        int cardId = getCardId(cardUrl);

        // Initialize connection and JSON data retrieval
        HttpURLConnection connection = null;
        Scanner scanner = null;
        try {
            URL url = new URL(SCRAPE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Construct JSON data for POST request
            JSONObject formData = new JSONObject();
            formData.put("specID", cardId);
            formData.put("draw", 1);
            formData.put("start", 0);
            formData.put("length", PAGE_MAX);

            // Write JSON data to connection
            // Not necessary if you're not writing to a file anymore

            // Parse JSON response
            scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray sales = jsonResponse.getJSONArray("data");

            // Process each sale data
            List<String> images = new ArrayList<>();
            List<String> auctionUrls = new ArrayList<>();
            List<Double> prices = new ArrayList<>();
            List<String> dates = new ArrayList<>();
            List<String> grades = new ArrayList<>();
            List<String> qualifiers = new ArrayList<>();
            List<String> lotNumbers = new ArrayList<>();
            List<String> auctionHouses = new ArrayList<>();
            List<String> sellers = new ArrayList<>();
            List<String> saleTypes = new ArrayList<>();
            List<String> certs = new ArrayList<>();

            for (int i = 0; i < sales.length(); i++) {
                JSONObject sale = sales.getJSONObject(i);

                // Example data extraction (adapt as needed)
                images.add(getImageUrl(sale));
                auctionUrls.add(getAuctionUrl(sale));
                prices.add(getPrice(sale));
                dates.add(getSaleDate(sale));
                grades.add(getGrade(sale));
                qualifiers.add(getQualifier(sale));
                lotNumbers.add(getLotNumber(sale));
                auctionHouses.add(getAuctionHouse(sale));
                sellers.add(getSellerName(sale));
                saleTypes.add(getSaleType(sale));
                certs.add(getPsaCert(sale));
            }

            // Print data to console
            for (int i = 0; i < sales.length(); i++) {
                System.out.println("Sale " + (i + 1) + ":");
                System.out.println("Image URL: " + images.get(i));
                System.out.println("Auction URL: " + auctionUrls.get(i));
                System.out.println("Price: $" + prices.get(i));
                System.out.println("Sale Date: " + dates.get(i));
                System.out.println("Grade: " + grades.get(i));
                System.out.println("Qualifier: " + qualifiers.get(i));
                System.out.println("Lot Number: " + lotNumbers.get(i));
                System.out.println("Auction House: " + auctionHouses.get(i));
                System.out.println("Seller: " + sellers.get(i));
                System.out.println("Sale Type: " + saleTypes.get(i));
                System.out.println("PSA Certification: " + certs.get(i));
                System.out.println("----------------------------------");
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private int getCardId(String cardUrl) {
        String[] parts = cardUrl.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private String getImageUrl(JSONObject sale) {
        return sale.optString("ImageURL");
    }

    private String getAuctionUrl(JSONObject sale) {
        return sale.optString("URL");
    }

    private Double getPrice(JSONObject sale) {
        String salePrice = sale.optString("SalePrice");
        if (salePrice.isEmpty()) {
            return Double.NaN;
        }
        return Double.parseDouble(salePrice.replace("$", "").replace(",", ""));
    }

    private String getSaleDate(JSONObject sale) {
        return sale.optString("EndDate");
    }

    private String getGrade(JSONObject sale) {
        return sale.optString("GradeString");
    }

    private String getQualifier(JSONObject sale) {
        return sale.optBoolean("HasQualifier") ? sale.optString("Qualifier") : null;
    }

    private String getLotNumber(JSONObject sale) {
        return sale.optString("LotNo");
    }

    private String getAuctionHouse(JSONObject sale) {
        return sale.optString("Name");
    }

    private String getSellerName(JSONObject sale) {
        return sale.optString("AuctionName");
    }

    private String getSaleType(JSONObject sale) {
        return sale.optString("AuctionType");
    }

    private String getPsaCert(JSONObject sale) {
        return sale.optString("CertNo");
    }

    public static void main(String[] args) {

        String inputUrl = EXAMPLE_URL;
        try {
            PsaAuctionPrices scraper = new PsaAuctionPrices(inputUrl);
            scraper.scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

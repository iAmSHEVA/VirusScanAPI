package org.baraa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private final static String API_Report = "https://www.virustotal.com/vtapi/v2/file/report?";
    private final static String API_Scan = "https://www.virustotal.com/vtapi/v2/file/scan";
    public static File file;
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {

        int choice;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Virus Scan program: ");
            System.out.println("1. Virus Scan by file\n2. Virus check by Hash or SCAN ID");
            System.out.print("Your choice: ");
            try {
                choice = in.nextInt();
                if (choice == 1) {
                    VirusScan();
                    break;
                } else if (choice == 2) {
                    VirusReport();
                    break;
                } else
                    System.exit(0);
            } catch (InputMismatchException e) {
                System.exit(0);
            }

        } while (true);
    }

    public static void VirusScan() throws IOException, InterruptedException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".")); //sets current directory

        int r = fileChooser.showOpenDialog(null); //select file to open
        if (r == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        } else
            System.exit(0);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_Scan))
                .header("accept", "text/plain")
                .header("content-type", "application/x-www-form-urlencoded")
                .method("POST", HttpRequest.BodyPublishers.ofString("apikey=781dc33df7ae27f765cf69c4f0f6c87cc159a5260fdf2c132a5eada8dc229fac&file=" + file))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response != null) {
            //System.out.println(response.body());
            ScanInfo sInfo = parseVirusScanResponse(response.body(), ScanInfo.class);
            System.out.println(sInfo);
        }

    }

    public static ScanInfo parseVirusScanResponse(String responseString, Class<?> elementClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode ScanInfoNode = objectMapper.readTree(responseString);
            ScanInfo sInfo = new ScanInfo();
            String verbose_msg = ScanInfoNode.get("verbose_msg").toString();
            String permalink = ScanInfoNode.get("permalink").toString();
            String scan_id = ScanInfoNode.get("scan_id").toString();

            sInfo.setScan_id(scan_id);
            sInfo.setVerbose_msg(verbose_msg);
            sInfo.setPermalink(permalink);

            return sInfo;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }

    }

    public static void VirusReport() {
        URIBuilder uriBuilder = null;
        String resource = "";
        System.out.print("Enter the resource (Scan ID or File Hash): ");
        resource = input.nextLine();
        try {
            uriBuilder = new URIBuilder(API_Report);
            uriBuilder.addParameter("apikey", "781dc33df7ae27f765cf69c4f0f6c87cc159a5260fdf2c132a5eada8dc229fac");
            uriBuilder.addParameter("resource", resource);
            uriBuilder.addParameter("allinfo", "false");
            URI uri = uriBuilder.build();
            HttpResponse<String> response = HTTPHelper.sendGet(uri);
            if (response != null) {
                //System.out.println(response.body());
                VirusInfo vInfo = parseVirusResponse(response.body(), VirusInfo.class);
                System.out.println(vInfo);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Enter SCAN ID OR FILE HASH !");
            VirusReport();
        }

    }

    private static VirusInfo parseVirusResponse(String responseString, Class<?> elementClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode VirusInfoNode = objectMapper.readTree(responseString);
            VirusInfo vInfo = new VirusInfo();
            String detected = VirusInfoNode.get("scans").get("McAfee").get("detected").toString();
            String result = VirusInfoNode.get("scans").get("McAfee").get("result").toString();

            vInfo.setDetected(detected);
            vInfo.setResult(result);

            return vInfo;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URL;



public class Controller {
	@FXML
	private TextField input;
	@FXML
	private AnchorPane container;
	private WebView mapView;
    private WebEngine webEngine;
    
    @FXML
    private Label countryLabel;
    @FXML
    private Label regionLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label districtLabel;
    @FXML
    private Label timezoneLabel;
    @FXML
    private Label ispLabel;
    @FXML
    private Label orgLabel;
    
    @FXML
    private Label countryInfo;
    @FXML
    private Label regionInfo;
    @FXML
    private Label cityInfo;
    @FXML
    private Label districtInfo;
    @FXML
    private Label timezoneInfo;
    @FXML
    private Label ispInfo;
    @FXML
    private Label orgInfo;
    @FXML
    private Rectangle infoRect;
    @FXML
    private ChoiceBox<String> mapChoice;
    @FXML
    private Button githubButton;
    
    public static String map;
    
    
	public class IPInfo {
		public static String country;
		public static String region;
		public static String city;
		public static String district;
		
		// Latitude and Longitude; Useful for determining precise geolocation 
		public static double lat; 
		public static double lon; 
		
		public static String timezone;
		public static String isp; // ISP - Internet Service Provider
		public static String org; // Organization (If any is registered under the given IP)
	}
	
	
	
	public void initialize() {

        mapChoice.getItems().addAll("Google Maps", "Open Street Map");
        
        // Make Google Maps the default choice
        mapChoice.setValue("Google Maps");
        map = "Google Maps";
        
        mapChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                map = newValue;
            }
        });
    }
	
	public void openGitHub() {
		String githubUrl = "https://github.com/6outtaTen";

        try {
            URI uri = new URI(githubUrl);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
	}
	
	public void searchInit() {
		input.setOnAction(this::lookup);
	}
	
	@SuppressWarnings("deprecation")
	public String getDataFromAPI(String search) throws Exception {
		String url = "http://ip-api.com/json/" + search + "?fields=1634297";
		
		URL apiUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");
        
        // Check the response code 
        int responseCode = connection.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            return response.toString();
        } else {
            System.err.println("HTTP GET request failed with response code: " + responseCode);
            throw new Exception("HTTP GET request failed with response code: " + responseCode);
        }
	}
	
	public void setLabels() {
		countryLabel.setVisible(true);
		regionLabel.setVisible(true);
		cityLabel.setVisible(true);
		districtLabel.setVisible(true);
		timezoneLabel.setVisible(true);
		ispLabel.setVisible(true);
		orgLabel.setVisible(true);
		
		infoRect.setVisible(true);
		
		countryInfo.setText(IPInfo.country);
		countryInfo.setVisible(true);
		regionInfo.setText(IPInfo.region);
		regionInfo.setVisible(true);
		cityInfo.setText(IPInfo.city);
		cityInfo.setVisible(true);
		districtInfo.setText(IPInfo.district);
		districtInfo.setVisible(true);
		timezoneInfo.setText(IPInfo.timezone);
		timezoneInfo.setVisible(true);
		ispInfo.setText(IPInfo.isp);
		ispInfo.setVisible(true);
		orgInfo.setText(IPInfo.org);
		orgInfo.setVisible(true);
		
		ispInfo.setWrapText(true);
		ispInfo.setTextAlignment(TextAlignment.JUSTIFY);
		ispInfo.setMaxWidth(200);
	}
	
	public void lookup(ActionEvent e) {
		
		String user_search = input.getText();
		
		try {
			String jsonResponse = getDataFromAPI(user_search);
			
			JSONObject jsonObject = new JSONObject(jsonResponse); 

            
            // Set new values of IPInfo properties
    		IPInfo.country = jsonObject.getString("country");
    		IPInfo.region = jsonObject.getString("regionName");
    		IPInfo.city = jsonObject.getString("city");
    		IPInfo.lat = jsonObject.getDouble("lat");
    		IPInfo.lon = jsonObject.getDouble("lon");
    		IPInfo.timezone = jsonObject.getString("timezone");
    		IPInfo.isp = jsonObject.getString("isp");
    		IPInfo.org = jsonObject.getString("org");
    		IPInfo.district = jsonObject.getString("district");
    		
    		mapView = new WebView();
            webEngine = mapView.getEngine();

            // Load user's map of choice
            
            if (map == "Google Maps") {
            	webEngine.load("https://maps.google.com/maps/place/" + IPInfo.lat + "," + IPInfo.lon);
            } else {
            	webEngine.load("https://www.openstreetmap.org/#map=15/" + IPInfo.lat + "/" + IPInfo.lon); // #map=15 - zoom level; the higher the more zoomed in 
            }
            
            
            mapView.setPrefSize(700, 700);
            mapView.setMinSize(10, 10);
            

            container.getChildren().add(mapView);
            
            // Set map position and size
            AnchorPane.setTopAnchor(mapView, 200.0); 
            AnchorPane.setBottomAnchor(mapView, null); 
            AnchorPane.setLeftAnchor(mapView, 0.0);
            AnchorPane.setRightAnchor(mapView, null);
            
			setLabels();
            
		} catch (Exception er) {
			er.printStackTrace();
		}
		
		
		
		
	}

}
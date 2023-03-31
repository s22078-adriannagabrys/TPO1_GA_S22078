package zad2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;

public class ServiceApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private String country;
    private String city;
    private String currencyCode;
    private JSONObject weather;
    private Service service;

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        //Input data
        VBox vBox = new VBox();

        Label infLabel = new Label("Enter the information here: ");
        Label inpCountryLabel = new Label("Country: ");
        Label inpCityLabel = new Label("City: ");
        Label inpCurrencyCodeLabel = new Label("Currency code: ");

        TextField inputCountry = new TextField();
        inputCountry.setPrefWidth(100);

        TextField inputCity = new TextField();
        inputCity.setPrefWidth(100);

        TextField inputCurrencyCode = new TextField();
        inputCurrencyCode.setPrefWidth(100);

        //Output data
        HBox hBox = new HBox();

        TextArea weatherField = new TextArea("Weather: ");
        TextArea nbpField = new TextArea("NBP: ");
        TextArea rateField = new TextArea("Currency rate: ");

        //Wikipedia
        WebView webView = new WebView();

        Button applyInputButton = new Button("Apply");
        applyInputButton.setOnMouseClicked(event -> {
            if(inputCountry.getText().isEmpty() || inputCity.getText().isEmpty() || inputCurrencyCode.getText().isEmpty()){
                getErrorAlert("You didn't provide enough information!");
            }else{
                country = inputCountry.getText();
                city = inputCity.getText();
                currencyCode = inputCurrencyCode.getText();
                service = new Service(country);
                service.nbpRate = currencyCode;
                if( service.getCountryCode() == null || service.getCurrencyCode() == null || service.getWeather(city) == null || service.getNBPRate() == null || service.getRateFor(currencyCode) == null){
                        getErrorAlert("Check again! You entered wrong information");
                }else {
                        weather = new JSONObject(service.getWeather(city));
                        String cityWeather = "City: " + weather.getString("name") + "\n" + "Description: " + weather.getJSONArray("weather").getJSONObject(0).getString("description") +
                                 "\n" + "Temperature: " + weather.getJSONObject("main").getBigDecimal("temp") + "\n" + "Humidity: " + weather.getJSONObject("main").getInt("humidity");
                        weatherField.setText("Weather: " + "\n" + cityWeather);

                        nbpField.setText("NBP: " + "\n" + service.getNBPRate());

                        rateField.setText("Currency rate: " + "\n" + service.getRateFor(currencyCode));

                        webView.getEngine().load("https://wikipedia.org/wiki/" + city);
                }
            }
        });

        vBox.getChildren().addAll(infLabel, inpCountryLabel, inputCountry, inpCityLabel, inputCity,inpCurrencyCodeLabel, inputCurrencyCode, applyInputButton);

        HBox.setHgrow(weatherField, Priority.ALWAYS);
        HBox.setHgrow(nbpField, Priority.ALWAYS);
        HBox.setHgrow(rateField, Priority.ALWAYS);
        hBox.getChildren().addAll(weatherField, nbpField, rateField);

        root.setTop(vBox);
        root.setCenter(hBox);
        root.setBottom(webView);

        Scene scene = new Scene(root, 900, 900);
        primaryStage.setTitle("TPOzad2_s22078");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void getErrorAlert(String textToPrint) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(textToPrint);
        alert.showAndWait();
    }



}

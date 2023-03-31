/**
 *
 *  @author Gabryś Adrianna S22078
 *
 */

package zad2;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;

public class Service {
    private final String country;
    static private final String myApiForWeather = "ffb363446e57d4226c244672e630503e";
    public String nbpRate;

    public Service(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        for (String isoCountry : Locale.getISOCountries()) {
            Locale locale = new Locale("", isoCountry);
            if (locale.getDisplayCountry().equals(this.country)) {
                return isoCountry;
            }
        }
        return null;
    }

    public String getCurrencyCode() {
        for (Locale locale: Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().equals(this.country)) {
                return Currency.getInstance(locale).getCurrencyCode();
            }
        }
        return null;
    }

    String getWeather(String city){
        String cityWeather;
        try{
            String urlBase = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s", city, getCountryCode(), myApiForWeather);
            JSONObject jsWeather = getJsonObjectFromURL(urlBase);
            cityWeather = jsWeather.toString();
        } catch (Exception ex){
            return null;
        }
        return cityWeather;
    }

    Double getRateFor(String currency_rate) {
        double rate;
        try{
            String urlBase = String.format("https://api.exchangerate.host/latestbase?from=%s&to=%s", currency_rate, getCurrencyCode());
            JSONObject jsRate = getJsonObjectFromURL(urlBase);
            Double selectedCountryCurrencyRate = jsRate.getJSONObject("rates").getDouble(currency_rate);
            Double baseCountryCurrencyRate = jsRate.getJSONObject("rates").getDouble(getCurrencyCode());
            rate = selectedCountryCurrencyRate/baseCountryCurrencyRate;
        } catch (Exception ex){
            return null;
        }
        return rate;
    }

    Double getNBPRate() {
        double nbp;
        try {
            String urlBase = String.format("http://api.nbp.pl/api/exchangerates/rates/A/%s", this.nbpRate);
            JSONObject jsNBP = getJsonObjectFromURL(urlBase);
            nbp = jsNBP.getJSONArray("rates").getJSONObject(0).getDouble("mid");
        } catch (FileNotFoundException ex){
            try{
                String urlBase2 = String.format("http://api.nbp.pl/api/exchangerates/rates/B/%s", this.nbpRate);
                JSONObject js = getJsonObjectFromURL(urlBase2);
                nbp = js.getJSONArray("rates").getJSONObject(0).getDouble("mid");
            } catch (Exception e){
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return nbp;
    }

    private JSONObject getJsonObjectFromURL(String urlBase) throws IOException {
        URL url = new URL(urlBase);
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return new JSONObject(in.readLine());
    }
}  

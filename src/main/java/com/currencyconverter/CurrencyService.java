package com.currencyconverter;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

@Service

public class CurrencyService {
	  private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

	    private final RestTemplate restTemplate;

	    
	    @Autowired
	    public CurrencyService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }

	    public Map<String, Object> getExchangeRates(String base) {
	        try {
	            Map<String, Object> response = restTemplate.getForObject(API_URL + base, Map.class);
	            if (response == null || !response.containsKey("rates")) {
	                throw new RuntimeException("Invalid response from currency API.");
	            }
	            return response;
	        } catch (HttpClientErrorException e) {
	            throw new RuntimeException("Error fetching exchange rates: " + e.getMessage());
	        }
	    }

	    public Map<String, Object> convertCurrency(CurrencyRequest request) {
	        Map<String, Object> rates = getExchangeRates(request.getFrom());

	        if (rates == null || !rates.containsKey("rates")) {
	            throw new RuntimeException("Currency rates not available.");
	        }

	        @SuppressWarnings("unchecked")
	        Map<String, Double> rateMap = (Map<String, Double>) rates.get("rates");

	        if (!rateMap.containsKey(request.getTo())) {
	            throw new IllegalArgumentException("Invalid currency code: " + request.getTo());
	        }

	        double rate = rateMap.get(request.getTo());
	        double convertedAmount = request.getAmount() * rate;

	        return Map.of(
	                "from", request.getFrom(),
	                "to", request.getTo(),
	                "amount", request.getAmount(),
	                "rate", rate,
	                "convertedAmount", convertedAmount
	        );
	    }

}

package com.currencyconverter;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    // GET /api/rates?base=USD
    @GetMapping("/rates")
    public Map<String, Object> getExchangeRates(@RequestParam(defaultValue = "USD") String base) {
        return currencyService.getExchangeRates(base);
    }

    // POST /api/convert
    @PostMapping("/convert")
    public Map<String, Object> convertCurrency(@RequestBody CurrencyRequest request) {
        return currencyService.convertCurrency(request);
    }
}

package com.wellsfargo.cmt.handler;


import com.wellsfargo.cmt.model.OptionData;
import com.wellsfargo.cmt.model.Stock;
import com.wellsfargo.cmt.option.PriceCalculator;
import com.wellsfargo.cmt.redis.RedisPublisher;
import com.wellsfargo.cmt.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hems on 26/04/19.
 */
@Service
public class PriceTickEventHandler {

    private static Logger log = LoggerFactory.getLogger(PriceTickEventHandler.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private PriceCalculator priceCalculator;

    private RedisPublisher redisPublisher;

    Map<String, List<OptionData>> stockOptions = new HashMap<>();


    PriceTickEventHandler() {
        redisPublisher = new RedisPublisher("optpricing.3monrn.0001.use2.cache.amazonaws.com", 6379);
    }


    public void handleEvent(final Stock stockPrice) {
        log.info("handleEvent..start - {}", stockPrice.symbol);
        List<OptionData> optionDataList = getOptions(stockPrice.symbol);
        Map<String, String> redisCache = new HashMap<>();
        if(optionDataList != null && !optionDataList.isEmpty()) {
            optionDataList.stream().forEach(optionData -> {
                double optionPrice = priceCalculator.price(optionData,stockPrice.price);
                optionData.setOptionPrice(optionPrice);
                if(redisPublisher != null) {
                    redisCache.put(optionData.getOptionName(), String.valueOf(optionData.getOptionPrice()));
                }
                //redisPublisher.publish(optionData);
            });
            if(redisPublisher != null){
                redisPublisher.publish("OPTION_PRICES", redisCache);
            }
            log.info("Prices calculated for {} -underlying: {}", stockPrice.symbol, stockPrice.price);
        }else {
            log.warn("Not Options Found For Symbol: {}" , stockPrice.symbol);
        }
        log.info("handleEvent..ends. - {}", stockPrice.symbol);

    }

    private List<OptionData> getOptions(String symbol){
        if(! stockOptions.containsKey(symbol)) {
            RestTemplate restTemplate = new RestTemplate();
            OptionData[] optionDataArray = restTemplate.getForObject("http://nimbus:8080/refdata/option/" + symbol, OptionData[].class);
            stockOptions.put(symbol, Arrays.asList(optionDataArray));
        }
        return stockOptions.get(symbol);
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public PriceCalculator getPriceCalculator() {
        return priceCalculator;
    }

    public void setPriceCalculator(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }
}

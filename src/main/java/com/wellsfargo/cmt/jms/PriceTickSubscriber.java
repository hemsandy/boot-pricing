package com.wellsfargo.cmt.jms;

import com.google.gson.*;
import com.wellsfargo.cmt.handler.PriceTickEventHandler;
import com.wellsfargo.cmt.model.Stock;
import com.wellsfargo.cmt.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.format.DateTimeFormatter;

/**
 * Created by hems on 26/04/19.
 */
@EnableJms
@Component
public class PriceTickSubscriber {

    private static Logger log = LoggerFactory.getLogger(PriceTickSubscriber.class);

    @Autowired
    private PriceTickEventHandler priceTickEventHandler;

    @Autowired
    private AppProperties appProperties;

    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");


    @JmsListener(destination = "CMT.MARKET.DATA.TICKER.QUEUE")
    public void receiveMessage(final Message jsonMessage) throws JMSException {
        log.info("receiveMessage..");
        try{
            if(jsonMessage instanceof  TextMessage) {
                TextMessage textMessage = (TextMessage) jsonMessage;
                String payLoad = textMessage.getText();
                log.debug("Payload: {} ", payLoad);

                Stock stock = Stock.gson.fromJson(payLoad, Stock.class);

                priceTickEventHandler.handleEvent(stock);

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            jsonMessage.acknowledge();
        }
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
}


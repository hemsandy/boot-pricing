package com.wellsfargo.cmt.model;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hems on 13/04/19.
 */
public class Stock {

    public Stock(String string, double d) {
        this.symbol = string;
        this.price = d;
    }

    public String symbol;
    public double price;
    public double delta;
    public LocalDateTime lastUpdate;

    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    public static Gson gson  = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>(){

                public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc,
                                             JsonSerializationContext context) {
                    return new JsonPrimitive(formatter.format(localDateTime));
                }
            }).registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                                 JsonDeserializationContext context) throws JsonParseException {
                    LocalDateTime dt = LocalDateTime.parse(json.getAsString(),formatter);
                    return dt;
                }
            }).create();

    public void update(double delta2) {
        this.delta = delta2;
        price = price + delta2;
        lastUpdate=LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Stock [symbol=")
                .append(symbol).append(", price=")
                .append(price).append(", delta=")
                .append(delta).append(", lastUpdate=")
                .append(lastUpdate).append("]");
        return builder.toString();
    }

    public String toJSONString() {

        return gson.toJson(this);
    }
    public static Stock fromJsonString(String jsonObjectStr) {
        Stock stock = null;
        try {
            stock = gson.fromJson(jsonObjectStr, Stock.class);

        }catch(Exception e) {
            System.out.println("Parse Exception :" + e.getMessage() + "forMessage:" + jsonObjectStr);
        }

        return stock;
    }
}

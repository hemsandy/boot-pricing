package com.wellsfargo.cmt.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hems on 19/04/19.
 */
@ConfigurationProperties(prefix = "cmt.options.refdata")
public class AppProperties {

    private String jmsUrl;
    private String queueNameForTickPrice;
    private String dateFormat;
    private String refDataUrl;

    public String getJmsUrl() {
        return jmsUrl;
    }

    public void setJmsUrl(String jmsUrl) {
        this.jmsUrl = jmsUrl;
    }

    public String getQueueNameForTickPrice() {
        return queueNameForTickPrice;
    }

    public void setQueueNameForTickPrice(String queueNameForTickPrice) {
        this.queueNameForTickPrice = queueNameForTickPrice;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getRefDataUrl() {
        return refDataUrl;
    }

    public void setRefDataUrl(String refDataUrl) {
        this.refDataUrl = refDataUrl;
    }
}

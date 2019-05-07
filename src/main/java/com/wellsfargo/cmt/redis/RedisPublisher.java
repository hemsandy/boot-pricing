package com.wellsfargo.cmt.redis;

import com.wellsfargo.cmt.model.OptionData;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by hems on 08/05/19.
 */

public class RedisPublisher {

    private Jedis jedis;

    public RedisPublisher(String hostUrl, int port) {
        jedis = new Jedis(hostUrl, port);
    }

    public int publish(List<OptionData> optionDataList){

        optionDataList.stream().forEach(optionData -> {
            jedis.set(optionData.getOptionName(), String.valueOf(optionData.getOptionPrice()));
        });
        return optionDataList.size();
    }

    public void publish(OptionData optionData){
        jedis.set(optionData.getOptionName(), String.valueOf(optionData.getOptionPrice()));
    }
}

package com.rikka.mall;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class MallApplicationTests {


    protected Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
/*    @Test
    void contextLoads() {
//        RedisTemplate
    }*/

}

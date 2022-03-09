package io.renren.mfy;

import com.hzsmk.sso.client.validation.util.RedisUtil;
import com.hzsmk.sso.client.validation.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.validation.UserInfo;
import org.jasig.cas.client.validation.UserInfoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTest {
        public static UserInfo getUserInfoByOpenid(String openid) {
                if (StringUtils.isBlank(openid)) {
                        return null;
                } else {
                        try {
                                openid = openid.trim();
                                Jedis jedis = RedisUtil.getJedis();
                                String redisKey = "SSO_" + openid;
                                byte[] byt = jedis.get(redisKey.getBytes());
//                                RedisUtil.returnResource(jedis);
                                if (byt == null) {
                                        return null;
                                } else {
                                        UserInfo us = (UserInfoImpl) SerializeUtil.unserizlize(byt);
                                        return us;
                                }
                        } catch (Exception var5) {
                                return null;
                        }
                }
        }


        @Test
        public void test1(){
                UserInfo userInfoByOpenid = this.getUserInfoByOpenid("04e6a36af16241d483ec9e76cd912159");
                System.out.println(userInfoByOpenid);
        }
}

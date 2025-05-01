package com.bjut.blockchain.web.util;

import com.codahale.shamir.Scheme;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.codahale.shamir.Scheme;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommonUtil{

    /**
     * 获取本地ip
     * @return
     */
    public static String getLocalIp() {
		try {
            InetAddress ip4 = InetAddress.getLocalHost();
            return ip4.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
        }
        return "";
    }

}
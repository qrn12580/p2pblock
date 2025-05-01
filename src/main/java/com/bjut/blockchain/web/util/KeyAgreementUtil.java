package com.bjut.blockchain.web.util;

import com.bjut.blockchain.web.service.CAImpl;
import com.bjut.blockchain.web.service.NodeJoinAndQuit;
import lombok.Getter;

import javax.crypto.KeyAgreement;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class KeyAgreementUtil {

    @Getter
    public static String keyAgreementValue = null;

    private static CopyOnWriteArrayList<String> keyArray = new CopyOnWriteArrayList<>();
    private static Timer timer = new Timer(true);
    private static long startTime = -1; // 初始值为-1，表示未开始计时
    private static boolean isTimerSet = false; // 标记是否已经设置了定时任务

    private static void keyAgreement(String[] strings) {
        try {
            strings = Arrays.stream(strings).distinct().toArray(String[]::new);
            Arrays.sort(strings);
            StringBuilder sb = new StringBuilder();
            for (String str : strings) {
                sb.append(str);
            }
            String concatenatedString = sb.toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(concatenatedString.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            keyAgreementValue = hexString.toString();
            System.out.println("密钥协商结果：" + keyAgreementValue);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public static void agreementKey(String key) {
        if (keyAgreementValue == null) {
            agreementKeyArray(key);
        } else {
            NodeJoinAndQuit nodeJoinAndQuit = new NodeJoinAndQuit();
            nodeJoinAndQuit.agreement();
        }
    }

    private static synchronized void agreementKeyArray(String key) {
        // 如果keyArray为空，开始计时
        if (keyArray.isEmpty()) {
            System.out.println("开始计时");
            startTime = System.currentTimeMillis();
        }

        // 将key加入到keyArray中
        keyArray.add(key);

        // 如果已经设置了定时任务，则不再重复设置
        if (!isTimerSet) {
            isTimerSet = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //synchronized (KeyAgreementUtil.class) {
                        // 检查是否超时
                        if (System.currentTimeMillis() - startTime >= 4000) {
                            System.out.println("开始密钥协商");
                            keyArray.add(CryptoUtil.byte2Hex(CAImpl.getKeyPair().getPublic().getEncoded()));
                            keyAgreement(keyArray.toArray(new String[0]));
                            // 清空keyArray
                            keyArray.clear();
                            // 重置startTime
                            startTime = -1;
                            // 重置定时任务标记
                            isTimerSet = false;
                        }
                    //}
                }
            }, 4000);
        }
    }

    public static void main(String[] args) {
        agreementKey("key1");
        agreementKey("key2");
        try {
            Thread.sleep(2000); // 等待2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        agreementKey("key3");
        try {
            Thread.sleep(10000); // 再等待2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        agreementKey("key4");
    }
}
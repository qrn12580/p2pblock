package com.bjut.blockchain;

import com.bjut.blockchain.web.util.CertificateValidator;
import com.bjut.blockchain.web.util.HttpRequestUtil;
import com.bjut.blockchain.web.util.IntegrityChecker;
import com.bjut.blockchain.web.util.PublicKeyUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

@SpringBootApplication
public class CaApplication {

    public static void main(String[] args) {
        //使用jar运行前放开
        if (true || IntegrityChecker.verifyIntegrity()) {
            System.out.println("Code integrity verified. Starting the application...");
            SpringApplication.run(CaApplication.class, args);
        } else {
            System.err.println("Code integrity check failed. Application cannot start.");
        }
    }

}
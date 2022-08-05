package com.app.misc;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiscApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiscApplication.class, args);
	}

	@Bean
	public AmazonSNS getSnsClient() {
		BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials("",
				"");
		return AmazonSNSClient
				.builder()
				.withRegion("ap-south-1")
				.withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
				.build();
	}
}

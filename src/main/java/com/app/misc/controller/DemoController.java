package com.app.misc.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.app.misc.model.DownloadReportSNSPayload;
import com.app.misc.model.Token;
import com.app.misc.service.HtmlToPdfService;
import com.app.misc.wkhtmltopdf.wrapper.Pdf;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.CharacterCodingException;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    HtmlToPdfService htmlToPdfService;

    @Autowired
    AmazonSNS snsClient;

    @GetMapping("/")
    public String home(){
        return "OK";
    }

    @GetMapping("/generate/tenantId/{tenantId}/secret/{secret}")
    public ResponseEntity<Token> tokenGenerator(@PathVariable String secret,@PathVariable String tenantId){
        String identityToken = null;
        long tokenExpiry = Long.parseLong("86400000");
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            identityToken = JWT.create()
                    .withSubject("identity")
                    .withClaim("tenantId", tenantId)
                    .withClaim("timestamp", ZonedDateTime.now().toString())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiry))
                    .sign(algorithm);
        } catch (JWTCreationException e ){
            System.out.println(e.getMessage());
        }
        Token token = new Token("Bearer "+ identityToken);
        return new ResponseEntity<Token>(token , HttpStatus.OK);
    }

    @PostMapping("/html-to-byte-array")
    public ResponseEntity<byte[]> getPdfAsStream(@RequestParam String hostname,@RequestBody String domString) throws IOException, InterruptedException {
        hostname = URLDecoder.decode(hostname, "UTF-8");
        Pdf pdf = htmlToPdfService.generateReportForDownload(hostname,domString);
        InputStream inputStream = new ByteArrayInputStream(pdf.getPDF());
        return new ResponseEntity<>(inputStream.readAllBytes(),HttpStatus.OK);
    }


    @PostMapping("/create-payload/download-report")
    private PublishResult createSnsPayloadForDownloadReport(
            @RequestParam(name = "resourceId") String resourceId,
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "classId") String classId,
            @RequestParam(name = "className") String className,
            @RequestParam(name = "provider") String provider,
            @RequestParam(name = "tenantId", required = false) String tenantId) throws JsonProcessingException {

        DownloadReportSNSPayload downloadReportSNSPayload =
                DownloadReportSNSPayload.builder()
                        .resourceId(resourceId)
                        .classId(classId)
                        .className(className)
                        .fileName(fileName)
                        .provider(provider)
                        .tenantId(tenantId).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String snsMessage = objectMapper.writeValueAsString(downloadReportSNSPayload);
        String topicArn = "arn:aws:sns:ap-south-1:206668483250:TriggerDownloadReport"
                ,subject = "arn:aws:sns:ap-south-1:206668483250:TriggerDownloadReport:36b2b951-7d4c-41d9-8c12-86a82f3784d6";
        PublishRequest publishRequest = new PublishRequest(topicArn, snsMessage, subject);
        return  snsClient.publish(publishRequest);
    }
}
package com.gateway;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assert.*;

public class GatewayApplicationLiveTest {


    @Autowired
    Environment environment;

    @Test
    public void testAccess() {

       String randomPort="39091";

        String baseUrl="http://localhost:";

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String testUrl = baseUrl+randomPort;



       MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
       form.clear();
       form.add("username", "user");
       form.add("password", "password");

       ResponseEntity<String> response = testRestTemplate.postForEntity(testUrl+ "/login", form, String.class);
       System.out.println("----------------------------------- "+response);
       String sessionCookie = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        System.out.println("Cookie-------------------------------------------: "+sessionCookie);
         headers.add("max-age","3600");
        //c51b1c97-cf69-4a1e-a582-69a114fb6dd0
        headers.add("Set-Cookie",sessionCookie);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        System.out.println("Size-------------------------------------------: "+ response.getHeaders());
        response = testRestTemplate.exchange(testUrl+"/booking-service",HttpMethod.GET, httpEntity,String.class);
        System.out.println(response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    public void testAccessToUnprotectedUrl(){

        String randomPort="43895";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String baseUrl="http://localhost:";
        String testUrl = baseUrl+"40123"+"/booking-service/";
        ResponseEntity<String> response = testRestTemplate.getForEntity(testUrl,String.class);


        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", "user");
        form.add("password", "password");
        response = testRestTemplate
                .postForEntity(baseUrl+randomPort + "/login", form, String.class);


     //   Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
       // Assertions.assertNull(response.getBody());
    }


    @Test
    public void testAccessToProtectedUrl(){
        String randomPort="39091";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String baseUrl="http://localhost:";

        String testUrl = baseUrl+randomPort+"/login";

      //  ResponseEntity<String> response = testRestTemplate.getForEntity(testUrl,String.class);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username","user");
        form.add("password","password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:"+randomPort+"/login",form,String.class);

        response = testRestTemplate
                .getForEntity(testUrl, String.class);

        String sessionCookie = response.getHeaders().get("Set-Cookie")
                .get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        System.out.println("------- "+sessionCookie);
        ResponseEntity<String>  response3 = testRestTemplate.exchange(baseUrl+randomPort+"/booking-service", HttpMethod.GET,httpEntity,String.class);
     // ResponseEntity<String>  response2= testRestTemplate.exchange(baseUrl+randomPort+"/booking-service", HttpMethod.GET,httpEntity,String.class);
       Assertions.assertEquals(HttpStatus.OK, response3.getStatusCode());


    }



}

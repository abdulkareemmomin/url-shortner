package com.example.demo1.controller;

import com.example.demo1.service.UrlService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequestMapping("/url")
@RestController
public class UrlShortnerController {

    private final UrlService urlService;
    private final String domain = "http://localhost:8080/url/";

    public UrlShortnerController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(){
//        System.out.println("hello abdulkareem ");
        return ResponseEntity.ok("welcome home");
    }

    @GetMapping("/short")
    public ResponseEntity<?> urlShort(@RequestParam("url") String originalUrl){
        System.out.println("line number: 30 " + originalUrl);

        return ResponseEntity.ok(domain + urlService.shortUrl(originalUrl));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortUrl){

        String originalUrl = urlService.getOriginalUrl(shortUrl);

        return ResponseEntity.status(302)
                .location(URI.create(originalUrl))
                .build();
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteUrl(@RequestParam("url") String url){

        urlService.deleteUrl(url);

        return ResponseEntity.ok("Deleted Successfully");
    }
}

package ru.college.practice.firstapp;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Практика 3. Раздел 4.7.1 — переадресация (листинги 4.17–4.18).
 *
 * В книге contact и details переписываются под переадресацию. Здесь они вынесены под префикс
 * /redirect, чтобы страница «Контакты» из прошлых примеров продолжала работать.
 *
 * <ul>
 *   <li>HttpResponseRedirect("/about") — временная переадресация, код 302;</li>
 *   <li>HttpResponsePermanentRedirect("/") — постоянная переадресация, код 301.</li>
 * </ul>
 */
@RestController
@RequestMapping("/redirect")
public class RedirectController {

    /** Временная переадресация: /redirect/contact → /about (302 Found). */
    @GetMapping("/contact")
    public ResponseEntity<Void> contact() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/about"))
                .build();
    }

    /** Постоянная переадресация: /redirect/details → / (301 Moved Permanently). */
    @GetMapping("/details")
    public ResponseEntity<Void> details() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/"))
                .build();
    }
}

package ru.college.practice.firstapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Практика 3. Раздел 4.7.2 — отправка пользователю статусных кодов.
 *
 * В Django для этого есть классы HttpResponseBadRequest (400), HttpResponseForbidden (403),
 * HttpResponseNotFound (404) и т. д. В Spring код ответа задаётся через {@link ResponseEntity}.
 */
@RestController
public class StatusController {

    private static final MediaType HTML_UTF8 = MediaType.parseMediaType("text/html;charset=UTF-8");

    /**
     * Проверка возраста: /access/25.
     * <ul>
     *   <li>возраст вне 1…110 — 400 Bad Request;</li>
     *   <li>младше 18 — 403 Forbidden;</li>
     *   <li>иначе — 200 OK.</li>
     * </ul>
     */
    @GetMapping({"/access/{age:\\d+}", "/access/{age:\\d+}/"})
    public ResponseEntity<String> access(@PathVariable int age) {
        if (age < 1 || age > 110) {
            return html(HttpStatus.BAD_REQUEST, "<h2>Некорректные данные</h2>");
        }
        if (age > 17) {
            return html(HttpStatus.OK, "<h2>Доступ разрешён</h2>");
        }
        return html(HttpStatus.FORBIDDEN, "<h2>Доступ заблокирован: недостаточно лет</h2>");
    }

    private static ResponseEntity<String> html(HttpStatus status, String body) {
        return ResponseEntity.status(status).contentType(HTML_UTF8).body(body);
    }
}

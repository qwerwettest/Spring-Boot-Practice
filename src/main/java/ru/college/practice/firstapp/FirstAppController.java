package ru.college.practice.firstapp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Практика 3. Представления и маршрутизация (Постолит, гл. 4).
 *
 * Аналог файлов views.py + urls.py приложения firstapp:
 * в Django маршрут и функция-представление описываются отдельно, в Spring — вместе,
 * аннотацией над методом контроллера.
 *
 * {@code @RestController} возвращает строку прямо в тело ответа — как {@code HttpResponse("...")}.
 * {@code produces = text/html} — чтобы браузер отрисовал теги {@code <h2>}, а не показал их текстом.
 */
@RestController
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
public class FirstAppController {

    // ---------- 4.1–4.2. Главная, О сайте, Контакты ----------

    /** path('', views.index) — только корень сайта. */
    @GetMapping("/")
    public String index() {
        return "<h2>Главная</h2>";
    }

    /**
     * re_path(r'^about', views.about) — любой адрес, начинающийся с /about:
     * /about, /about/, /about/что-угодно.
     * Шаблон {@code /**} — «ноль или больше сегментов пути».
     */
    @GetMapping("/about/**")
    public String about() {
        return "<h2>О сайте</h2>";
    }

    /** re_path(r'^contact', views.contact). */
    @GetMapping("/contact/**")
    public String contact() {
        return "<h2>Контакты</h2>";
    }

    // ---------- 4.3. Очерёдность маршрутов ----------

    /**
     * re_path(r'^about/contact/', views.contact).
     * В Django этот маршрут обязан стоять раньше '^about', иначе до него не дойдёт очередь.
     * В Spring порядок объявления не важен: из подходящих шаблонов выбирается самый конкретный,
     * поэтому /about/contact попадёт сюда, а не в {@link #about()}.
     */
    @GetMapping("/about/contact/**")
    public String aboutContact() {
        return contact();
    }

    // ---------- 4.4–4.5. Регулярные выражения и параметры представлений ----------

    /**
     * re_path(r'^products/$', views.products) и
     * re_path(r'^products/(?P<productid>\d+)/', views.products).
     *
     * {@code {productid:\d+}} — именованный параметр с регулярным выражением, как (?P<productid>\d+).
     * Если id не передан, используется значение по умолчанию 1 (листинг 4.8).
     */
    @GetMapping({"/products", "/products/", "/products/{productid:\\d+}", "/products/{productid:\\d+}/"})
    public String products(@PathVariable(required = false) Integer productid) {
        int id = (productid != null) ? productid : 1;
        return "<h2>Продукт № %d</h2>".formatted(id);
    }

    /**
     * re_path(r'^users/(?P<id>\d+)/(?P<name>\D+)/', views.users):
     * id — только цифры, name — только «не цифры».
     */
    @GetMapping({"/users/{id:\\d+}/{name:\\D+}", "/users/{id:\\d+}/{name:\\D+}/"})
    public String users(@PathVariable int id, @PathVariable String name) {
        return "<h2>Пользователь</h2><h3>id: %d Имя: %s</h3>".formatted(id, escape(name));
    }

    // ---------- 4.6. Параметры строки запроса ----------

    /**
     * Параметры после «?»: /user/?name=Tom&age=38.
     * Аналог request.GET.get("name", "Undefined").
     */
    @GetMapping({"/user", "/user/"})
    public String user(@RequestParam(defaultValue = "Undefined") String name,
                       @RequestParam(defaultValue = "0") int age) {
        return "<h2>Имя: %s Возраст: %d</h2>".formatted(escape(name), age);
    }

    /**
     * Данные из адреса нельзя вставлять в HTML как есть: в имени может прийти {@code <script>}.
     * Django-шаблоны экранируют автоматически, а при сборке HTML строкой это делаем сами.
     */
    static String escape(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

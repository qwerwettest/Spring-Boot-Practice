package ru.college.practice.firstapp;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Практика 4. Шаблоны (Постолит, гл. 5).
 *
 * В отличие от {@code @RestController} из практики 3, {@code @Controller} возвращает не текст ответа,
 * а <b>имя шаблона</b>: строка "firstapp/home" означает файл
 * {@code src/main/resources/templates/firstapp/home.html}. Это аналог
 * {@code render(request, "firstapp/home.html")} в Django.
 */
@Controller
public class TemplatesController {

    /** Пользователь для примера со сложными данными (в книге — словарь {"name": ..., "age": ...}). */
    public record User(String name, int age) {
    }

    /**
     * 5.5.5. Главная страница на основе базового шаблона (листинги 5.33–5.34):
     * return render(request, "firstapp/index.html").
     */
    @GetMapping("/")
    public String index() {
        return "firstapp/index";
    }

    /**
     * 5.1 и 5.5. Шаблон в подпапке приложения, стили из static/css и картинка из static/images
     * (листинги 5.5–5.6, 5.20–5.23).
     */
    @GetMapping({"/home", "/home/"})
    public String home() {
        return "firstapp/home";
    }

    /**
     * 5.3. Передача простых данных в шаблон (листинги 5.9–5.10).
     * В Django — словарь context, в Spring — объект {@link Model}: каждый addAttribute — одна пара
     * «ключ: значение», в шаблоне она доступна как ${header}, ${message}.
     */
    @GetMapping({"/data", "/data/"})
    public String data(Model model) {
        model.addAttribute("header", "Передача параметров в шаблон Thymeleaf");
        model.addAttribute("message", "Загружен шаблон templates/firstapp/index_app1.html");
        return "firstapp/index_app1";
    }

    /**
     * 5.4. Передача сложных данных (листинги 5.11–5.13).
     * Возвращается {@link ModelAndView} — шаблон и данные одним объектом. Это аналог
     * TemplateResponse(request, "index.html", data) из раздела 5.2.
     */
    @GetMapping({"/complex", "/complex/"})
    public ModelAndView complex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("header", "Персональные данные");                        // обычная переменная
        mv.addObject("langs", List.of("Английский", "Немецкий", "Испанский")); // список (в книге — массив)
        mv.addObject("user", new User("Максим", 30));                          // объект (в книге — словарь)
        mv.addObject("address", List.of("Виноградная", 23, 45));               // в книге — кортеж
        return mv;
    }

    /**
     * 5.5.3. Страница «Контакты» с дополнительными данными (листинги 5.25–5.27):
     * TemplateView.as_view(template_name="firstapp/contact.html", extra_context={"work": work}).
     *
     * Страница без данных («О сайте») подключена как TemplateView через WebConfig, а здесь нужны
     * данные, поэтому — обычный метод контроллера.
     * Пути те же, что в практике 3: /contact/** и более конкретный /about/contact/** (раздел 4.3).
     */
    @GetMapping({"/contact/**", "/about/contact/**"})
    public String contact(Model model) {
        model.addAttribute("work", List.of(
                "Разработка веб-сайтов",
                "Разработка мобильных приложений",
                "Сопровождение информационных систем"));
        return "firstapp/contact";
    }
}

package ru.college.practice.buttons;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Практика 6. Формы с кнопками.
 *
 * Три вида кнопок из задания и их обработка на сервере:
 * <ol>
 *   <li>{@code <button type="submit">} внутри формы → {@link #submit} ({@code @PostMapping}), затем redirect;</li>
 *   <li>{@code <button type="button" onclick="...">} → JavaScript-функция; одна из них отправляет
 *       запрос fetch на отдельный адрес → {@link #click};</li>
 *   <li>кнопка-ссылка {@code <a href="/buttons/about"><button>} → {@link #about} ({@code @GetMapping}).</li>
 * </ol>
 */
@Controller
public class ButtonsController {

    /** Сколько раз нажата JS-кнопка — хранится на сервере, пока приложение запущено. */
    private final AtomicInteger clicks = new AtomicInteger();

    /** Аналог ветки else в my_view: GET — показать страницу с кнопками. */
    @GetMapping({"/buttons", "/buttons/"})
    public String index() {
        return "buttons/index";
    }

    /**
     * 1. Кнопка submit. Аналог ветки {@code if request.method == 'POST': return redirect('success_page')}.
     *
     * В форме две submit-кнопки с одинаковым name="action" и разными value. Браузер отправляет
     * значение только той кнопки, которую нажали, поэтому сервер понимает, какая это была кнопка.
     *
     * После обработки POST делается redirect (схема Post/Redirect/Get): если обновить страницу
     * результата, браузер не отправит форму повторно. Данные переживают redirect благодаря
     * flash-атрибутам — они живут ровно до следующего запроса.
     */
    @PostMapping({"/buttons", "/buttons/"})
    public String submit(@RequestParam(defaultValue = "") String name,
                         @RequestParam String action,
                         RedirectAttributes redirect) {
        if ("clear".equals(action)) {
            return "redirect:/buttons";                       // кнопка «Очистить» — просто назад к форме
        }
        redirect.addFlashAttribute("name", name.isBlank() ? "аноним" : name);
        return "redirect:/buttons/success";
    }

    /** Страница успеха (success_page). Без данных из формы показывает подсказку. */
    @GetMapping({"/buttons/success", "/buttons/success/"})
    public String success() {
        return "buttons/success";
    }

    /**
     * 2. Кнопка без формы + JavaScript. Функция в браузере отправляет fetch('/buttons/click', POST),
     * сервер возвращает JSON, а страница выводит его без перезагрузки.
     * {@code @ResponseBody}: ответ — данные, а не имя шаблона; Map Spring сам превращает в JSON.
     */
    @PostMapping("/buttons/click")
    @ResponseBody
    public Map<String, Object> click() {
        return Map.of(
                "clicks", clicks.incrementAndGet(),
                "time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /** 3. Кнопка-ссылка: обычный GET-запрос на другую страницу. */
    @GetMapping({"/buttons/about", "/buttons/about/"})
    public String about(Model model) {
        model.addAttribute("clicks", clicks.get());
        return "buttons/about";
    }
}

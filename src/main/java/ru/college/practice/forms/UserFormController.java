package ru.college.practice.forms;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

/**
 * Практика 5. Формы (Постолит, гл. 6), разделы 6.1–6.2 и 6.4.
 *
 * В Django одна функция index() проверяет request.method: GET — показать форму, POST — обработать.
 * В Spring это два метода на один адрес: {@code @GetMapping} и {@code @PostMapping}.
 */
@Controller
public class UserFormController {

    /** 6.1. Пустая форма (листинг 6.2): userform = UserForm(); render(..., {"form": userform}). */
    @GetMapping({"/forms", "/forms/"})
    public String getForm(Model model) {
        model.addAttribute("form", new UserForm());
        return "forms/new";
    }

    /**
     * 6.2. Обработка POST-запроса (листинг 6.6, рис. 6.9).
     * {@code @ModelAttribute("form")} собирает объект из параметров name и age (аналог
     * request.POST.get("name")) и кладёт его в модель под именем "form" — шаблон ответа
     * берёт значения как ${form.name}.
     */
    @PostMapping({"/forms", "/forms/"})
    public String postForm(@ModelAttribute("form") UserForm form) {
        return "forms/success";
    }

    /**
     * 6.4.1–6.4.4. Настройка полей:
     * <ul>
     *   <li>widget=forms.Textarea — поле комментария в шаблоне сделано через {@code <textarea>};</li>
     *   <li>initial — форма создаётся с уже заполненными значениями (листинг 6.40);</li>
     *   <li>field_order — порядок полей передаётся списком (листинги 6.41–6.42), по умолчанию
     *       сначала возраст, потом имя. Его можно поменять: /forms/settings?order=name,age,comment;</li>
     *   <li>help_text — подсказки под полями (листинг 6.45).</li>
     * </ul>
     */
    @GetMapping({"/forms/settings", "/forms/settings/"})
    public String settings(@RequestParam(defaultValue = "age,name,comment") List<String> order, Model model) {
        model.addAttribute("form", new UserForm("Введите ФИО", 18));
        model.addAttribute("order", order);
        return "forms/settings";
    }

    /** 6.4.5. Одна и та же форма тремя способами: as_table, as_ul, as_p (листинг 6.46, рис. 6.58). */
    @GetMapping({"/forms/layout", "/forms/layout/"})
    public String layout(Model model) {
        model.addAttribute("form", new UserForm());
        return "forms/layout";
    }

    /** 6.4.6. Форма с проверкой данных на сервере. */
    @GetMapping({"/forms/validation", "/forms/validation/"})
    public String validationForm(Model model) {
        model.addAttribute("form", new ValidatedUserForm());
        return "forms/validation";
    }

    /**
     * 6.4.6. Аналог {@code if userform.is_valid(): ... else: ...}.
     * {@code @Valid} запускает проверку по аннотациям в {@link ValidatedUserForm}, результат попадает в
     * {@link BindingResult}. Он обязан идти сразу после проверяемого параметра — иначе при ошибке
     * Spring сам вернёт 400 и до метода дело не дойдёт.
     * Если есть ошибки — та же форма показывается снова, с введёнными данными и сообщениями.
     */
    @PostMapping({"/forms/validation", "/forms/validation/"})
    public String validationSubmit(@Valid @ModelAttribute("form") ValidatedUserForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "forms/validation";
        }
        return "forms/valid";
    }
}

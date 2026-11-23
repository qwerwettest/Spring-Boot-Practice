package ru.college.practice.forms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 6.3. Использование полей разных типов.
 *
 * GET /forms/fields — страница со всеми типами полей, POST /forms/fields — показывает, что пришло
 * на сервер и во что Spring превратил каждое значение.
 */
@Controller
public class FieldsController {

    /** Варианты для ChoiceField: choices=((1, "English"), (2, "German"), (3, "French")) — табл. 6.1. */
    static final Map<Integer, String> LANGUAGES = new TreeMap<>(Map.of(1, "English", 2, "German", 3, "French"));

    /** Варианты для TypedMultipleChoiceField из листинга 6.37. TreeMap — чтобы порядок был по ключу. */
    static final Map<Integer, String> CITIES = new TreeMap<>(Map.of(
            1, "Москва", 2, "Воронеж", 3, "Курск", 4, "Томск"));

    /** Формат DurationField: "DD HH:MM:SS", например "2 1:10:20" — 2 дня 1 час 10 минут 20 секунд. */
    private static final Pattern DURATION = Pattern.compile("(\\d+) (\\d{1,2}):(\\d{2}):(\\d{2})");

    @GetMapping({"/forms/fields", "/forms/fields/"})
    public String fields(Model model) {
        model.addAttribute("fields", new FieldsForm());
        model.addAttribute("languages", LANGUAGES);
        model.addAttribute("cities", CITIES);
        model.addAttribute("files", listDirectory(Path.of(System.getProperty("user.dir"))));
        return "forms/fields";
    }

    /**
     * BindingResult без {@code @Valid}: здесь он нужен, чтобы ошибки преобразования
     * (например, "abc" в поле UUID) не превращались в ответ 400, а показывались на странице.
     */
    @PostMapping({"/forms/fields", "/forms/fields/"})
    public String submit(@ModelAttribute("fields") FieldsForm f, BindingResult result, Model model) {
        Map<String, Object> values = new LinkedHashMap<>();
        put(values, "Положить товар в корзину (BooleanField)", f.getBasket());
        put(values, "Положить товар в корзину, необязательно (BooleanField)", f.getBasketOptional());
        put(values, "Вы поедете в Сочи этим летом? (NullBooleanField)", f.getVyb());
        put(values, "Имя клиента (CharField)", f.getName());
        put(values, "Электронный адрес (EmailField)", f.getEmail());
        put(values, "IP адрес (GenericIPAddressField)", f.getIpAdres());
        put(values, "Текст (RegexField)", f.getRegText());
        put(values, "Введите текст (SlugField)", f.getSlugText());
        put(values, "Введите URL (URLField)", f.getUrlText());
        put(values, "Введите UUID (UUIDField)", f.getUuidText());
        put(values, "URL не длиннее 20 символов (ComboField)", f.getComboText());
        put(values, "Выберите файл (FilePathField)", f.getFilePath());
        put(values, "Файл (FileField)", describe(f.getFile()));
        put(values, "Изображение (ImageField)", describe(f.getImage()));
        put(values, "Введите дату (DateField)", f.getDate());
        put(values, "Введите время (TimeField)", f.getTime());
        put(values, "Введите дату и время (DateTimeField)", f.getDateTime());
        put(values, "Продолжительность (DurationField)", describeDuration(f.getDuration()));
        if (f.getSplitDate() != null || f.getSplitTime() != null) {
            put(values, "Дата и время (SplitDateTimeField)", f.getSplitDate() + " " + f.getSplitTime());
        }
        put(values, "Введите целое число (IntegerField)", f.getNum());
        put(values, "Введите десятичное число (DecimalField)", f.getDecimalNum());
        put(values, "Введите число (FloatField)", f.getFloatNum());
        put(values, "Выберите язык (ChoiceField), строка", label(LANGUAGES, f.getLing()));
        put(values, "Выберите язык (TypedChoiceField), Integer", label(LANGUAGES, f.getTypedLing()));
        put(values, "Выберите языки (MultipleChoiceField)", labels(LANGUAGES, f.getLings()));
        put(values, "Выберите город (TypedMultipleChoiceField)", labels(CITIES, f.getCities()));

        model.addAttribute("values", values);
        // isBindingFailure() — значение не удалось преобразовать в тип поля (typeMismatch).
        // Стандартный текст такой ошибки длинный и на английском, поэтому выводим свой.
        model.addAttribute("errors", result.getFieldErrors().stream()
                .map(e -> "Поле %s: %s, введено «%s»".formatted(e.getField(),
                        e.isBindingFailure() ? "неверный формат значения" : e.getDefaultMessage(),
                        e.getRejectedValue()))
                .toList());
        return "forms/fields_result";
    }

    /** В таблицу результата попадают только заполненные поля. */
    private static void put(Map<String, Object> values, String label, Object value) {
        if (value != null && !(value instanceof String s && s.isBlank())) {
            values.put(label, value);
        }
    }

    /** FileField: Spring упаковывает файл в MultipartFile — аналог UploadedFile в Django. */
    private static String describe(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return "%s (%d байт, %s)".formatted(file.getOriginalFilename(), file.getSize(), file.getContentType());
    }

    /** DurationField: "2 1:10:20" → "2 дн. 1 ч 10 мин 20 с (PT49H10M20S)". */
    static String describeDuration(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        Matcher m = DURATION.matcher(text.trim());
        if (!m.matches()) {
            return "неверный формат: " + text;
        }
        Duration d = Duration.ofDays(Long.parseLong(m.group(1)))
                .plusHours(Long.parseLong(m.group(2)))
                .plusMinutes(Long.parseLong(m.group(3)))
                .plusSeconds(Long.parseLong(m.group(4)));
        return "%d дн. %d ч %d мин %d с (%s)".formatted(
                d.toDays(), d.toHoursPart(), d.toMinutesPart(), d.toSecondsPart(), d);
    }

    private static String label(Map<Integer, String> choices, Object key) {
        if (key == null || key.toString().isBlank()) {
            return null;
        }
        return choices.get(Integer.valueOf(key.toString())) + " (значение " + key + ")";
    }

    private static String labels(Map<Integer, String> choices, List<?> keys) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        return String.join(", ", keys.stream().map(k -> choices.get(Integer.valueOf(k.toString()))).toList());
    }

    /**
     * FilePathField(path=..., allow_files=True, allow_folders=True): список файлов и папок каталога.
     * В книге это C:/Python37/, здесь — папка, из которой запущено приложение.
     */
    private static List<String> listDirectory(Path dir) {
        try (Stream<Path> entries = Files.list(dir)) {
            return entries
                    .map(p -> p.getFileName() + (Files.isDirectory(p) ? "/" : ""))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }
}

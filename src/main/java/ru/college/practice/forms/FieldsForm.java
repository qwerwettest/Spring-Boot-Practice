package ru.college.practice.forms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 6.3. Все типы полей из раздела 6.3 (табл. 6.1) в одном объекте.
 *
 * На странице /forms/fields каждое поле стоит в своей маленькой форме и отправляется отдельно,
 * поэтому заполнено всегда одно-два поля, остальные — {@code null}.
 *
 * Тип Java-поля определяет, во что Spring превратит строку из запроса: "45" → Integer,
 * "2025-10-10" → LocalDate, строка UUID → UUID и т. д. Если строка не подходит,
 * в BindingResult появится ошибка typeMismatch — аналог ValidationError у полей Django.
 */
public class FieldsForm {

    // 6.3.3. BooleanField: обязательный флажок (листинг 6.10) и необязательный (листинг 6.11)
    private Boolean basket;
    private Boolean basketOptional;

    // 6.3.4. NullBooleanField: Неизвестно (null) / Да / Нет
    private Boolean vyb;

    // 6.3.5–6.3.12. Текстовые поля
    private String name;        // CharField(max_length=15)
    private String email;       // EmailField
    private String ipAdres;     // GenericIPAddressField
    private String regText;     // RegexField(regex="^[0-9][A-F][0-9]$")
    private String slugText;    // SlugField
    private String urlText;     // URLField
    private UUID uuidText;      // UUIDField — Spring сам проверит формат и создаст UUID
    private String comboText;   // ComboField(fields=[CharField(max_length=20), URLField()])

    // 6.3.13–6.3.15. Файлы
    private String filePath;    // FilePathField — выбор из списка файлов каталога
    private MultipartFile file; // FileField
    private MultipartFile image; // ImageField

    // 6.3.16–6.3.20. Дата и время. HTML-поля date/time/datetime-local присылают значения в формате
    // ISO (2025-10-10, 10:32, 2025-10-10T10:32), @DateTimeFormat говорит Spring, как их разбирать.
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate date;
    @DateTimeFormat(iso = ISO.TIME)
    private LocalTime time;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime dateTime;
    private String duration;    // DurationField, формат "DD HH:MM:SS" — разбирается в контроллере
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate splitDate; // SplitDateTimeField — два отдельных поля
    @DateTimeFormat(iso = ISO.TIME)
    private LocalTime splitTime;

    // 6.3.21–6.3.23. Числа
    private Integer num;            // IntegerField
    private BigDecimal decimalNum;  // DecimalField(decimal_places=2)
    private Double floatNum;        // FloatField

    // 6.3.24–6.3.27. Выбор из списка
    private String ling;            // ChoiceField — значение приходит строкой
    private Integer typedLing;      // TypedChoiceField(coerce=int) — Spring приводит к Integer сам
    private List<String> lings;     // MultipleChoiceField
    private List<Integer> cities;   // TypedMultipleChoiceField(coerce=int)

    public Boolean getBasket() { return basket; }
    public void setBasket(Boolean basket) { this.basket = basket; }

    public Boolean getBasketOptional() { return basketOptional; }
    public void setBasketOptional(Boolean basketOptional) { this.basketOptional = basketOptional; }

    public Boolean getVyb() { return vyb; }
    public void setVyb(Boolean vyb) { this.vyb = vyb; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIpAdres() { return ipAdres; }
    public void setIpAdres(String ipAdres) { this.ipAdres = ipAdres; }

    public String getRegText() { return regText; }
    public void setRegText(String regText) { this.regText = regText; }

    public String getSlugText() { return slugText; }
    public void setSlugText(String slugText) { this.slugText = slugText; }

    public String getUrlText() { return urlText; }
    public void setUrlText(String urlText) { this.urlText = urlText; }

    public UUID getUuidText() { return uuidText; }
    public void setUuidText(UUID uuidText) { this.uuidText = uuidText; }

    public String getComboText() { return comboText; }
    public void setComboText(String comboText) { this.comboText = comboText; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }

    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public LocalDate getSplitDate() { return splitDate; }
    public void setSplitDate(LocalDate splitDate) { this.splitDate = splitDate; }

    public LocalTime getSplitTime() { return splitTime; }
    public void setSplitTime(LocalTime splitTime) { this.splitTime = splitTime; }

    public Integer getNum() { return num; }
    public void setNum(Integer num) { this.num = num; }

    public BigDecimal getDecimalNum() { return decimalNum; }
    public void setDecimalNum(BigDecimal decimalNum) { this.decimalNum = decimalNum; }

    public Double getFloatNum() { return floatNum; }
    public void setFloatNum(Double floatNum) { this.floatNum = floatNum; }

    public String getLing() { return ling; }
    public void setLing(String ling) { this.ling = ling; }

    public Integer getTypedLing() { return typedLing; }
    public void setTypedLing(Integer typedLing) { this.typedLing = typedLing; }

    public List<String> getLings() { return lings; }
    public void setLings(List<String> lings) { this.lings = lings; }

    public List<Integer> getCities() { return cities; }
    public void setCities(List<Integer> cities) { this.cities = cities; }
}

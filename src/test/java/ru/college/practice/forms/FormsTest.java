package ru.college.practice.forms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

/** Практика 5: формы — GET/POST, типы полей, настройка полей, вид формы, валидация. */
@SpringBootTest
@AutoConfigureMockMvc
class FormsTest {

    @Autowired
    private MockMvc mvc;

    private String body(org.springframework.test.web.servlet.ResultActions actions) throws Exception {
        return actions.andReturn().getResponse().getContentAsString();
    }

    // 6.1–6.2

    @Test
    void emptyFormIsShown() throws Exception {
        mvc.perform(get("/forms"))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/new"))
                .andExpect(content().string(containsString("<label for=\"name\">Имя клиента:</label>")))
                .andExpect(content().string(containsString("<input type=\"number\" id=\"age\" name=\"age\" value=\"\">")))
                .andExpect(content().string(containsString("action=\"/forms\"")));
    }

    @Test
    void postedFormIsAnswered() throws Exception {
        mvc.perform(post("/forms").param("name", "Михаил").param("age", "45"))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/success"))
                .andExpect(content().string(containsString("<h3>Имя - Михаил, Возраст - 45</h3>")));
    }

    // 6.3

    @Test
    void fieldsPageHasAllWidgets() throws Exception {
        mvc.perform(get("/forms/fields"))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/fields"))
                .andExpect(content().string(containsString("type=\"checkbox\"")))
                .andExpect(content().string(containsString("type=\"email\"")))
                .andExpect(content().string(containsString("type=\"url\"")))
                .andExpect(content().string(containsString("pattern=\"[0-9][A-F][0-9]\"")))
                .andExpect(content().string(containsString("enctype=\"multipart/form-data\"")))
                .andExpect(content().string(containsString("type=\"datetime-local\"")))
                .andExpect(content().string(containsString("step=\"0.01\"")))
                .andExpect(content().string(containsString("<select multiple size=\"4\" required id=\"cities\" name=\"cities\">")))
                .andExpect(content().string(containsString("<option value=\"2\">Воронеж</option>")))
                .andExpect(content().string(containsString("pom.xml")));   // FilePathField
    }

    @Test
    void typedValuesAreConverted() throws Exception {
        String html = body(mvc.perform(post("/forms/fields")
                        .param("basket", "true")
                        .param("uuidText", "123e4567-e89b-12d3-a456-426655440000")
                        .param("date", "2025-10-10")
                        .param("dateTime", "2025-10-10T10:32")
                        .param("decimalNum", "12.34")
                        .param("typedLing", "2")
                        .param("cities", "2", "4"))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/fields_result")));
        assertThat(html)
                .contains("<td>true</td>", "<td>Boolean</td>")
                .contains("<td>123e4567-e89b-12d3-a456-426655440000</td>", "<td>UUID</td>")
                .contains("<td>2025-10-10</td>", "<td>LocalDate</td>")
                .contains("<td>2025-10-10T10:32</td>", "<td>LocalDateTime</td>")
                .contains("<td>12.34</td>", "<td>BigDecimal</td>")
                .contains("German (значение 2)")
                .contains("Воронеж, Томск")
                .doesNotContain("class=\"error\"");
    }

    @Test
    void nullBooleanUnknownIsEmpty() throws Exception {
        mvc.perform(post("/forms/fields").param("vyb", ""))
                .andExpect(content().string(containsString("Ни одно поле не заполнено.")));
        mvc.perform(post("/forms/fields").param("vyb", "false"))
                .andExpect(content().string(containsString("<td>false</td>")));
    }

    @Test
    void wrongUuidIsReportedNot400() throws Exception {
        mvc.perform(post("/forms/fields").param("uuidText", "abc"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Поле uuidText: неверный формат значения, введено «abc»")));
    }

    @Test
    void durationIsParsed() {
        assertThat(FieldsController.describeDuration("2 1:10:20")).isEqualTo("2 дн. 1 ч 10 мин 20 с (PT49H10M20S)");
        assertThat(FieldsController.describeDuration("2 часа")).startsWith("неверный формат");
    }

    @Test
    void fileIsUploaded() throws Exception {
        var file = new MockMultipartFile("file", "ChromeSetup-64.exe", "application/octet-stream", new byte[1024]);
        mvc.perform(multipart("/forms/fields").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ChromeSetup-64.exe (1024 байт, application/octet-stream)")));
    }

    // 6.4.1–6.4.4

    @Test
    void settingsHaveInitialValuesTextareaAndHelpText() throws Exception {
        mvc.perform(get("/forms/settings"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("value=\"Введите ФИО\"")))
                .andExpect(content().string(containsString("value=\"18\"")))
                .andExpect(content().string(containsString("<textarea")))
                .andExpect(content().string(containsString("<small>Введите возраст</small>")));
    }

    @Test
    void fieldOrderCanBeChanged() throws Exception {
        String byDefault = body(mvc.perform(get("/forms/settings")));
        assertThat(byDefault.indexOf("id=\"age\"")).isLessThan(byDefault.indexOf("id=\"name\""));

        String reversed = body(mvc.perform(get("/forms/settings").param("order", "name,age")));
        assertThat(reversed.indexOf("id=\"name\"")).isLessThan(reversed.indexOf("id=\"age\""));
        assertThat(reversed).doesNotContain("<textarea");
    }

    // 6.4.5

    @Test
    void layoutShowsThreeVariants() throws Exception {
        mvc.perform(get("/forms/layout"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h2>Форма как таблица</h2>")))
                .andExpect(content().string(containsString("<h2>Форма как список</h2>")))
                .andExpect(content().string(containsString("<h2>Форма как параграф</h2>")))
                .andExpect(content().string(containsString("<li><label for=\"name\">Имя:</label>")))
                .andExpect(content().string(containsString("<p><label for=\"age\">Возраст:</label>")));
    }

    // 6.4.6

    @Test
    void emptyFormFailsValidation() throws Exception {
        mvc.perform(post("/forms/validation").param("name", "").param("age", "").param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/validation"))
                .andExpect(model().attributeHasFieldErrors("form", "name", "age", "email"))
                .andExpect(content().string(containsString("Введите имя")))
                .andExpect(content().string(containsString("class=\"field-error\"")));
    }

    @Test
    void outOfRangeValuesFailValidation() throws Exception {
        mvc.perform(post("/forms/validation").param("name", "Я").param("age", "200").param("email", "info@"))
                .andExpect(view().name("forms/validation"))
                .andExpect(content().string(containsString("Имя должно содержать от 2 до 20 символов")))
                .andExpect(content().string(containsString("Возраст не может быть больше 120")))
                .andExpect(content().string(containsString("Некорректный электронный адрес")))
                // введённые данные не теряются
                .andExpect(content().string(containsString("value=\"200\"")));
    }

    @Test
    void notANumberAgeShowsRussianMessage() throws Exception {
        mvc.perform(post("/forms/validation").param("name", "Михаил").param("age", "abc").param("email", "a@b.ru"))
                .andExpect(view().name("forms/validation"))
                .andExpect(content().string(containsString("Неверный формат значения")));
    }

    @Test
    void validFormIsAccepted() throws Exception {
        mvc.perform(post("/forms/validation").param("name", "Михаил").param("age", "45").param("email", "info@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("forms/valid"))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(not(containsString("class=\"error\""))))
                .andExpect(content().string(containsString("Имя - Михаил, Возраст - 45, Email - info@example.com")));
    }
}

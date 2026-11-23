package ru.college.practice.buttons;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** Практика 6: кнопка submit, JS-кнопка с запросом к серверу, кнопка-ссылка. */
@SpringBootTest
@AutoConfigureMockMvc
class ButtonsTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void pageHasThreeKindsOfButtons() throws Exception {
        mvc.perform(get("/buttons"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<form method=\"post\" action=\"/buttons\">")))
                .andExpect(content().string(containsString("<button type=\"submit\" name=\"action\" value=\"send\">Отправить</button>")))
                .andExpect(content().string(containsString("<button type=\"button\" onclick=\"myJavaScriptFunction()\">Нажми меня</button>")))
                .andExpect(content().string(containsString("<a href=\"/buttons/about\">")));
    }

    @Test
    void submitButtonRedirectsToSuccessWithName() throws Exception {
        mvc.perform(post("/buttons").param("name", "Анна").param("action", "send"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/buttons/success"))
                .andExpect(flash().attribute("name", "Анна"));
    }

    @Test
    void emptyNameBecomesAnonymous() throws Exception {
        mvc.perform(post("/buttons").param("name", "").param("action", "send"))
                .andExpect(flash().attribute("name", "аноним"));
    }

    @Test
    void clearButtonReturnsToForm() throws Exception {
        mvc.perform(post("/buttons").param("name", "Анна").param("action", "clear"))
                .andExpect(redirectedUrl("/buttons"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    void successPageShowsFlashData() throws Exception {
        mvc.perform(get("/buttons/success").flashAttr("name", "Анна"))
                .andExpect(content().string(containsString("Форма отправлена. Здравствуйте, Анна!")));
        // без отправки формы (например, после обновления страницы) данных нет
        mvc.perform(get("/buttons/success"))
                .andExpect(content().string(containsString("Нет данных формы")));
    }

    @Test
    void jsButtonGetsJsonFromServer() throws Exception {
        mvc.perform(post("/buttons/click"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.clicks").isNumber())
                .andExpect(jsonPath("$.time").isString());
    }

    @Test
    void linkButtonOpensAnotherPage() throws Exception {
        mvc.perform(get("/buttons/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вы перешли по кнопке-ссылке")));
    }
}

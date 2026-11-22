package ru.college.practice.firstapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/** Проверка маршрутов практики 3 без запуска сервера. */
@WebMvcTest({FirstAppController.class, RedirectController.class, StatusController.class})
class FirstAppRoutingTest {

    @Autowired
    private MockMvc mvc;

    private void expectHtml(String url, String html) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(html));
    }

    // 4.1–4.3: страницы «/», «/about/**», «/contact/**» в практике 4 переведены на шаблоны —
    // их маршруты проверяются в TemplatesTest.

    @Test
    void unknownPageIs404() throws Exception {
        mvc.perform(get("/abracadabra")).andExpect(status().isNotFound());
    }

    // 4.4–4.5

    @Test
    void productById() throws Exception {
        expectHtml("/products/5/", "<h2>Продукт № 5</h2>");
        expectHtml("/products/5", "<h2>Продукт № 5</h2>");
    }

    @Test
    void productWithoutIdUsesDefault() throws Exception {
        expectHtml("/products/", "<h2>Продукт № 1</h2>");
        expectHtml("/products", "<h2>Продукт № 1</h2>");
    }

    @Test
    void productIdMustBeDigits() throws Exception {
        mvc.perform(get("/products/abc/")).andExpect(status().isNotFound());
    }

    @Test
    void userByIdAndCyrillicName() throws Exception {
        expectHtml("/users/3/Виктор/", "<h2>Пользователь</h2><h3>id: 3 Имя: Виктор</h3>");
    }

    @Test
    void userNameMustNotContainDigits() throws Exception {
        mvc.perform(get("/users/3/Viktor7/")).andExpect(status().isNotFound());
    }

    // 4.6

    @Test
    void queryStringParameters() throws Exception {
        expectHtml("/user/?name=Tom&age=38", "<h2>Имя: Tom Возраст: 38</h2>");
        expectHtml("/user/", "<h2>Имя: Undefined Возраст: 0</h2>");
    }

    @Test
    void htmlInParametersIsEscaped() throws Exception {
        expectHtml("/user?name=<b>x</b>", "<h2>Имя: &lt;b&gt;x&lt;/b&gt; Возраст: 0</h2>");
    }

    // 4.7.1

    @Test
    void temporaryRedirect() throws Exception {
        mvc.perform(get("/redirect/contact"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/about"));
    }

    @Test
    void permanentRedirect() throws Exception {
        mvc.perform(get("/redirect/details"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", "/"));
    }

    // 4.7.2

    @Test
    void accessStatusCodes() throws Exception {
        mvc.perform(get("/access/25")).andExpect(status().isOk());
        mvc.perform(get("/access/15")).andExpect(status().isForbidden());
        mvc.perform(get("/access/200")).andExpect(status().isBadRequest());
    }
}

package ru.college.practice.firstapp;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** Практика 4: шаблоны Thymeleaf, передача данных, статические файлы, базовый шаблон. */
@SpringBootTest
@AutoConfigureMockMvc
class TemplatesTest {

    @Autowired
    private MockMvc mvc;

    private void expectPage(String url, String viewName, String... fragments) throws Exception {
        var result = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName));
        for (String fragment : fragments) {
            result.andExpect(content().string(containsString(fragment)));
        }
    }

    // 5.1, 5.5 — шаблон в папке приложения, CSS и картинка

    @Test
    void homeUsesStaticFiles() throws Exception {
        expectPage("/home", "firstapp/home",
                "<h1>Домашняя страница Spring!</h1>",
                "href=\"/css/styles.css\"",
                "src=\"/images/image1.jpg\"");
    }

    @Test
    void staticFilesAreServed() throws Exception {
        mvc.perform(get("/css/styles.css"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("img {width: 250px;}")));
        mvc.perform(get("/images/image1.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }

    // 5.3 — простые данные

    @Test
    void simpleDataIsInserted() throws Exception {
        expectPage("/data", "firstapp/index_app1",
                "<h1>Передача параметров в шаблон Thymeleaf</h1>",
                "<p>Загружен шаблон templates/firstapp/index_app1.html</p>");
    }

    // 5.2 + 5.4 — ModelAndView (аналог TemplateResponse) и сложные данные

    @Test
    void complexDataIsInserted() throws Exception {
        expectPage("/complex", "index",
                "<h1>Персональные данные</h1>",
                "Имя: <span>Максим</span> Age: <span>30</span>",
                "ул. <span>Виноградная</span>, д. <span>23</span>,",
                "кв. <span>45</span>",
                "Владеет языками: <span>Английский</span>, <span>Немецкий</span>",
                "<li>Испанский</li>");
    }

    // 5.5.1 — встроенные и внутренние стили

    @Test
    void stylesPage() throws Exception {
        expectPage("/styles", "firstapp/styles",
                "<style>h1 {color: green;}</style>",
                "<h1 style=\"color: red;\">");
    }

    // 5.5.3 — TemplateView и extra_context

    @Test
    void aboutIsTemplateViewOnBaseTemplate() throws Exception {
        expectPage("/about", "firstapp/about",
                "<title>about</title>",
                "<h1>Сведения о компании</h1>",
                "Связка шаблонов about.html и base.html",
                "<div>Подвал страницы</div>");
    }

    @Test
    void aboutStillMatchesAnyPathStartingWithAbout() throws Exception {
        // маршрут из практики 3 (re_path(r'^about')) продолжает работать
        expectPage("/about/", "firstapp/about", "<h1>Сведения о компании</h1>");
        expectPage("/about/team", "firstapp/about", "<h1>Сведения о компании</h1>");
    }

    @Test
    void contactGetsExtraContext() throws Exception {
        expectPage("/contact/", "firstapp/contact",
                "<h1>Контактные данные компании</h1>",
                "<li>Разработка веб-сайтов</li>",
                "<li>Сопровождение информационных систем</li>",
                "<div>Подвал страницы</div>");
    }

    @Test
    void contactWithoutTitleKeepsDefaultFromBase() throws Exception {
        expectPage("/contact", "firstapp/contact", "<title>Заголовок</title>");
    }

    @Test
    void moreSpecificAboutContactRouteStillWins() throws Exception {
        // раздел 4.3 практики 3
        expectPage("/about/contact/", "firstapp/contact", "<h1>Контактные данные компании</h1>");
    }

    // 5.5.5 — базовый шаблон

    @Test
    void indexExtendsBaseTemplate() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("firstapp/index"))
                .andExpect(content().string(allOf(
                        containsString("<title>Index</title>"),
                        containsString("<h1>Главная страница</h1>"),
                        containsString("<section>Связка шаблонов index.html и base.html</section>"),
                        containsString("<div>Подвал страницы</div>"),
                        // сам base.html не должен «протекать» своим заголовком по умолчанию
                        not(containsString("<title>Заголовок</title>")))));
    }

    // 5.6.1 — текущие дата и время

    @Test
    void nowPageShowsCurrentDate() throws Exception {
        String today = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        expectPage("/now", "firstapp/now", "Дата: <span>" + today + "</span>");
    }
}

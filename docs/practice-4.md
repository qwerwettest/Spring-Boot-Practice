# Практика 4 — Шаблоны

**Основа:** А. Постолит «Python, Django и PyCharm», глава 5 (разделы 5.1–5.6.1).
**Реализация:** Java 17, Spring Boot + **Thymeleaf** — по согласованию с преподавателем вместо Django.

Код:
[`TemplatesController.java`](../src/main/java/ru/college/practice/firstapp/TemplatesController.java),
[`WebConfig.java`](../src/main/java/ru/college/practice/firstapp/WebConfig.java),
шаблоны — [`src/main/resources/templates`](../src/main/resources/templates),
статические файлы — [`src/main/resources/static`](../src/main/resources/static),
тесты — [`TemplatesTest.java`](../src/test/java/ru/college/practice/firstapp/TemplatesTest.java).

## Соответствие Django → Spring

| Django (книга) | Spring Boot + Thymeleaf |
|---|---|
| папка `templates`, настройка `TEMPLATES` / `DIRS` в `settings.py` | папка `src/main/resources/templates` — подключается автоматически |
| `render(request, "firstapp/home.html")` | `@Controller`, метод возвращает `"firstapp/home"` |
| `TemplateResponse(request, "index.html", data)` | `return new ModelAndView("index")` + `addObject(...)` |
| `context=data` (словарь) | параметр `Model model`, `model.addAttribute("header", ...)` |
| `{{ header }}` | `th:text="${header}"` |
| `{{ user.name }}`, `{{ address.0 }}` | `${user.name}`, `${address[0]}` |
| `{% for x in list %}` | `th:each="x : ${list}"` |
| папка `static`, `STATICFILES_DIRS` | папка `src/main/resources/static` — раздаётся автоматически |
| `{% load static %}` + `{% static 'css/styles.css' %}` | `th:href="@{/css/styles.css}"` |
| `TemplateView.as_view(template_name=...)` | `registry.addViewController(...).setViewName(...)` |
| `extra_context={"work": work}` | метод контроллера с `Model` |
| `{% extends "firstapp/base.html" %}` + `{% block %}` | `th:replace="~{firstapp/base :: layout(...)}"` + `th:fragment` |
| `{% now "d.m.Y" %}` | `${#temporals.format(#temporals.createNow(), 'dd.MM.yyyy')}` |

Шаблон Thymeleaf — обычный HTML-файл. Атрибуты `th:*` браузер игнорирует, поэтому файл можно открыть
и без сервера, а при обработке на сервере Thymeleaf подставляет в него данные.

## Страницы

| Адрес | Шаблон | Раздел книги |
|---|---|---|
| http://localhost:8080/home | `firstapp/home.html` | 5.1 шаблон в папке приложения, 5.5 CSS и картинка |
| http://localhost:8080/data | `firstapp/index_app1.html` | 5.3 передача простых данных |
| http://localhost:8080/complex | `index.html` | 5.2 `TemplateResponse`, 5.4 сложные данные |
| http://localhost:8080/styles | `firstapp/styles.html` | 5.5.1 встроенные и внутренние стили |
| http://localhost:8080/about | `firstapp/about.html` | 5.5.3 `TemplateView`, 5.5.5 базовый шаблон |
| http://localhost:8080/contact | `firstapp/contact.html` | 5.5.3 `extra_context`, 5.5.5 базовый шаблон |
| http://localhost:8080/ | `firstapp/index.html` | 5.5.5 базовый шаблон |
| http://localhost:8080/now | `firstapp/now.html` | 5.6.1 текущие дата и время |

Как и в книге, страницы **Главная**, **О сайте** и **Контакты** из практики 3 теперь отдаются через
шаблоны, а не строками. Их адреса не изменились: `/about/**` по-прежнему ловит любой адрес,
начинающийся с `/about`, а `/about/contact/` ведёт на «Контакты».

---

## 5.1. Создание и использование шаблонов

В Django папку `templates` нужно прописать в `settings.py` (`'DIRS': [TEMPLATE_DIR]`). В Spring Boot
папка `src/main/resources/templates` подключается автоматически, как только в проекте есть Thymeleaf.

```java
@Controller
public class TemplatesController {

    @GetMapping("/home")
    public String home() {
        return "firstapp/home";     // → templates/firstapp/home.html
    }
}
```

Отличие от практики 3: там был `@RestController`, и возвращаемая строка шла прямо в ответ.
У `@Controller` строка — это **имя шаблона**. Расширение `.html` и папка `templates/` добавляются
автоматически. Шаблоны разложены по подпапке `firstapp`, как в книге, чтобы шаблоны разных частей
сайта не смешивались.

## 5.2. Класс TemplateResponse

Аналог `TemplateResponse` — объект `ModelAndView`: имя шаблона и данные в одном объекте.

```java
@GetMapping("/complex")
public ModelAndView complex() {
    ModelAndView mv = new ModelAndView("index");
    mv.addObject("header", "Персональные данные");
    ...
    return mv;
}
```

## 5.3. Передача данных в шаблоны (листинги 5.9–5.10)

```java
@GetMapping("/data")
public String data(Model model) {
    model.addAttribute("header", "Передача параметров в шаблон Thymeleaf");
    model.addAttribute("message", "Загружен шаблон templates/firstapp/index_app1.html");
    return "firstapp/index_app1";
}
```

```html
<h1 th:text="${header}">Заголовок</h1>
<p th:text="${message}">Сообщение</p>
```

`th:text` заменяет текст внутри тега значением из модели. Текст «Заголовок» виден, только если
открыть файл без сервера. `th:text` автоматически экранирует HTML, как и `{{ }}` в Django.

**Результат:**

> # Передача параметров в шаблон Thymeleaf
> Загружен шаблон templates/firstapp/index_app1.html

## 5.4. Передача в шаблон сложных данных (листинги 5.11–5.13)

```java
mv.addObject("header", "Персональные данные");                        // обычная переменная
mv.addObject("langs", List.of("Английский", "Немецкий", "Испанский")); // список (в книге — массив)
mv.addObject("user", new User("Максим", 30));                          // объект (в книге — словарь)
mv.addObject("address", List.of("Виноградная", 23, 45));               // в книге — кортеж
```

Вместо словаря `{"name": ..., "age": ...}` используется Java-запись `record User(String name, int age)`.
В шаблоне обращение к полям выглядит так же: `${user.name}`.

```html
<p>Имя: <span th:text="${user.name}"></span> Age: <span th:text="${user.age}"></span></p>
<p>Адрес: ул. <span th:text="${address[0]}"></span>, д. <span th:text="${address[1]}"></span>,
    кв. <span th:text="${address[2]}"></span></p>
<p>Владеет языками: <span th:text="${langs[0]}"></span>, <span th:text="${langs[1]}"></span></p>
```

В Django к элементам списка обращаются через точку (`address.0`), в Thymeleaf — через квадратные
скобки (`address[0]`).

**Результат:**

> # Персональные данные
> Имя: Максим Age: 30
> Адрес: ул. Виноградная, д. 23, кв. 45
> Владеет языками: Английский, Немецкий

Дополнительно на странице выведен весь список языков через `th:each`, это аналог `{% for %}`.

## 5.5. Статичные файлы

### 5.5.1. Основы каскадных таблиц стилей (листинги 5.16–5.18)

Страница `/styles` показывает приоритет стилей:

```html
<head>
    <style>h1 {color: green;}</style>                 <!-- внутренний стиль -->
</head>
<body>
    <h1>Заголовок с внутренним стилем — зелёный</h1>
    <h1 style="color: red;">Заголовок со встроенным стилем — красный</h1>   <!-- встроенный -->
</body>
```

Встроенный стиль (атрибут `style`) имеет приоритет над внутренним (`<style>` в `<head>`), поэтому
второй заголовок красный, хотя для всех `h1` задан зелёный цвет.

### 5.5.2. Использование статичных файлов (листинги 5.19–5.23)

Структура как в книге (рис. 5.25): папка `static` с подпапками `css` и `images`.

```
src/main/resources/
├── static/
│   ├── css/styles.css
│   └── images/image1.jpg
└── templates/
    ├── index.html
    └── firstapp/
        ├── base.html, index.html, about.html, contact.html
        ├── home.html, index_app1.html, styles.html, now.html
```

`static/css/styles.css`:

```css
body h1 {color: red;}
body h2 {color: green;}
img {width: 250px;}
```

`templates/firstapp/home.html`:

```html
<link th:href="@{/css/styles.css}" rel="stylesheet">
...
<h1>Домашняя страница Spring!</h1>
<h2>templates/firstapp/home.html</h2>
<img th:src="@{/images/image1.jpg}" alt="Море и яхта">
```

`{% load static %}` и `STATICFILES_DIRS` не нужны: Spring Boot сам раздаёт содержимое `static/`
от корня сайта, то есть файл `static/css/styles.css` доступен по адресу `/css/styles.css`.
`@{...}` строит правильную ссылку с учётом адреса приложения.

**Результат:** красный заголовок `h1`, зелёный `h2`, картинка шириной 250 px.

### 5.5.3. Класс TemplateView (листинги 5.24–5.27)

`TemplateView.as_view(template_name="firstapp/about.html")` отдаёт шаблон без отдельной функции.
В Spring то же делает **view controller** в классе конфигурации:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about/**").setViewName("firstapp/about");
    }
}
```

Если шаблону нужны данные (`extra_context`), используется обычный метод контроллера с `Model`:

```java
@GetMapping({"/contact/**", "/about/contact/**"})
public String contact(Model model) {
    model.addAttribute("work", List.of(
            "Разработка веб-сайтов",
            "Разработка мобильных приложений",
            "Сопровождение информационных систем"));
    return "firstapp/contact";
}
```

*(Страница книги с листингом 5.27 в скриншоты попала не полностью, поэтому список видов работ
придуман для примера.)*

### 5.5.4. Конфигурация шаблонов

В Django расположение шаблонов настраивается переменной `TEMPLATES` в `settings.py`. В Spring Boot
настройки по умолчанию уже подходят (`templates/`, расширение `.html`). При необходимости они
меняются в `application.properties`:

```properties
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false      # уже задано в проекте: правки шаблонов видны без перезапуска
spring.thymeleaf.encoding=UTF-8
```

### 5.5.5. Расширение шаблонов на основе базового шаблона (листинги 5.32–5.35)

**Базовый шаблон** `firstapp/base.html`: фрагмент с тремя параметрами — аналог блоков
`title`, `header`, `content`.

```html
<html th:fragment="layout(title, header, content)">
<head>
    <meta charset="UTF-8">
    <title th:replace="${title}">Заголовок</title>
</head>
<body>
    <h1 th:replace="${header}"></h1>
    <div th:replace="${content}"></div>
    <div>Подвал страницы</div>
</body>
</html>
```

**Страница на его основе** `firstapp/index.html` — аналог `{% extends %}`:

```html
<html th:replace="~{firstapp/base :: layout(~{::title}, ~{::h1}, ~{::section})}">
<head>
    <title>Index</title>
</head>
<body>
    <h1>Главная страница</h1>
    <section>Связка шаблонов index.html и base.html</section>
</body>
</html>
```

Thymeleaf берёт `base.html` и подставляет в него `<title>`, `<h1>` и `<section>` этой страницы.
Подвал общий для всех страниц и задан только в базовом шаблоне.

**Значение блока по умолчанию:** в Django для блока `title` можно задать текст по умолчанию.
В Thymeleaf страница передаёт вместо параметра знак `_` («ничего не делать»), и в базовом шаблоне
остаётся его собственный текст. Так сделано в `contact.html`, поэтому вкладка этой страницы
называется «Заголовок».

**Результат на главной (/):**

> # Главная страница
> Связка шаблонов index.html и base.html
> Подвал страницы

**Результат на /about:**

> # Сведения о компании
> "Интеллектуальные программные системы"
> Связка шаблонов about.html и base.html
> Подвал страницы

## 5.6.1. Тег для вывода текущих даты и времени

В Django: `{% now "d.m.Y H:i" %}`. В Thymeleaf — утилита `#temporals`:

```html
<p>Дата: <span th:text="${#temporals.format(#temporals.createNow(), 'dd.MM.yyyy')}"></span></p>
<p>Время: <span th:text="${#temporals.format(#temporals.createNow(), 'HH:mm')}"></span></p>
<p>День недели: <span th:text="${#temporals.dayOfWeekName(#temporals.createNow())}"></span></p>
```

| Django | Thymeleaf / Java | Значение |
|---|---|---|
| `d` | `dd` | день месяца, 01–31 |
| `m` | `MM` | месяц, 01–12 |
| `Y` | `yyyy` | год, 4 цифры |
| `H` | `HH` | часы, 00–23 |
| `i` | `mm` | минуты |
| `l` | `EEEE` | день недели словом |

**Результат:**

> # Текущие дата и время
> Дата: 22.11.2026
> Время: 13:19
> День недели: воскресенье

---

## Проверка

Автотесты — 12 проверок в `TemplatesTest`:
- каждая страница отдаёт нужный шаблон с нужными данными;
- CSS и картинка доступны по адресам `/css/styles.css` и `/images/image1.jpg`;
- базовый шаблон подставляет заголовок, содержимое и подвал;
- значение по умолчанию для `title`;
- маршруты `/about/...` и `/about/contact/` из практики 3 продолжают работать;
- текущая дата.

```bash
./mvnw test -Dtest=TemplatesTest
```

Ручная проверка: запустить `SpringBootPracticeApplication` и открыть адреса из таблицы «Страницы».

*Изображение `image1.jpg` нарисовано для проекта программно (море, яхта, облака) вместо фотографии
из книги.*

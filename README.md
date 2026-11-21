# Spring-Boot-Practice

Практические и лабораторные работы по дисциплине (преподаватель Водолазкина Н.), выполненные на Java
вместо Python/Django.

## Стек

- Java 17+
- Spring Boot (Spring MVC, Thymeleaf, Bean Validation, DevTools)
- Maven (через Maven Wrapper, отдельная установка Maven не нужна)

## Запуск

```bash
./mvnw spring-boot:run      # Linux / macOS / Git Bash
mvnw.cmd spring-boot:run    # Windows
```

Приложение откроется на http://localhost:8080

Тесты: `./mvnw test`

## Структура репозитория

### Ветка `main` — практики (веб, по книге А. Постолита «Python, Django и PyCharm»)

Каждая практика — отдельный коммит в `main`:

| Практика | Тема | Аналог в Spring |
|---|---|---|
| 3 | Представления и маршрутизация (гл. 4) | `@Controller`, `@GetMapping`, `@PathVariable`, `@RequestParam`, redirect |
| 4 | Шаблоны (гл. 5) | Thymeleaf, `Model`, статические файлы, базовый шаблон |
| 5 | Формы (гл. 6) | `th:object`, `@PostMapping`, `@Valid` |
| 6 | Формы с кнопками | submit / JS / кнопка-ссылка |

### Отдельные ветки — лабораторные работы (вариант 5)

Ветки создаются от первого коммита `main`:

| Ветка | Тема |
|---|---|
| `lab-4.1-loops` | Алгоритмы циклической структуры |
| `lab-5.1-arrays` | Обработка одномерных массивов |
| `lab-6-matrices` | Обработка двумерных массивов |

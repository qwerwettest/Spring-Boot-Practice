package ru.college.practice.firstapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Практика 4. Раздел 5.5.3 — класс TemplateView.
 *
 * В Django TemplateView.as_view(template_name="firstapp/about.html") позволяет отдать шаблон
 * без отдельной функции-представления. В Spring то же самое делает «view controller»:
 * адрес сразу связывается с шаблоном, без метода в контроллере.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // path('about/', TemplateView.as_view(template_name="firstapp/about.html"))
        // «/about/**» — как re_path(r'^about') из практики 3: /about, /about/, /about/что-угодно
        registry.addViewController("/about/**").setViewName("firstapp/about");

        // 5.5.1 — приоритет встроенных и внутренних стилей (листинги 5.16–5.18)
        registry.addViewController("/styles").setViewName("firstapp/styles");

        // 5.6.1 — тег для вывода текущих даты и времени
        registry.addViewController("/now").setViewName("firstapp/now");
    }
}

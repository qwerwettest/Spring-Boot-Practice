package ru.college.practice.forms;

/**
 * Аналог класса формы из листинга 6.1:
 * <pre>
 * class UserForm(forms.Form):
 *     name = forms.CharField()
 *     age = forms.IntegerField()
 * </pre>
 * В Django форма сама рисует HTML и проверяет данные. В Spring это просто объект с полями:
 * Spring заполняет его из параметров запроса (name=...&amp;age=...) через сеттеры, а Thymeleaf
 * читает значения через геттеры ({@code th:field="*{name}"} → {@code getName()}).
 *
 * {@code Integer}, а не {@code int}: пустое поле превращается в {@code null}, а не в ошибку.
 * Поле {@code comment} добавлено в разделе 6.4.1 (виджет Textarea).
 */
public class UserForm {
    private String name;
    private Integer age;
    private String comment;

    public UserForm() {
    }

    public UserForm(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

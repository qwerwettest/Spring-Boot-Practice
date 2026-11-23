package ru.college.practice.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 6.4.6. Форма с правилами проверки. В Django правила задаются параметрами полей:
 * <pre>
 * name  = forms.CharField(min_length=2, max_length=20)
 * age   = forms.IntegerField(min_value=1, max_value=120)
 * email = forms.EmailField()
 * </pre>
 * В Spring — аннотациями Bean Validation (пакет jakarta.validation). Проверку запускает
 * {@code @Valid} в контроллере, текст ошибки задаётся в {@code message}.
 */
public class ValidatedUserForm {

    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 20, message = "Имя должно содержать от 2 до 20 символов")
    private String name;

    @NotNull(message = "Введите возраст")
    @Min(value = 1, message = "Возраст не может быть меньше 1")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    private Integer age;

    @NotBlank(message = "Введите электронный адрес")
    @Email(message = "Некорректный электронный адрес")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

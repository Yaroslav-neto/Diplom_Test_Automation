## Настройка окружения
    1. ОС Windows с установленным JDK, Docker Desktop, Google Chrome, DBeaver
    2. Запустить Docker Desktop
    3. Используя терминал запустить Docker compose командой "docker compose up"
    4. Открыть второй терминал и в нем запустить jar файл командой "java -jar ./artifacts/aqa-shop.jar"
    
### Запуск автотестов
    1. Открыть третий терминал и в нем запустить тесты - ./gredlew test
    2. Открыть четвертый терминал и в нем запустить генерацию отчета Allure - ./gradlew allureServe

#### Информация по ходу проведения тестов
    1. Установлены тайминги для проверки отображения видимых объектов(для ожидания ответа сервера 13 сек и для проверки
        отображения объектов которых быть не должно 5 сек).

### Завершение тестирования:
    1. Открыть окно четвертого терминала и ввести комбинацию клавиш CTRL+C, далее Y чтобы завершить процесс
        формирование отчета. 
    2. Открыть окно второго терминала и ввести комбинацию клавиш CTRL+C чтобы остановить jar файл.
    3. Открыть окно первого терминала и ввести комбинацию клавиш CTRL+C чтобы остановить Docker, 
        далее ввести docker-compose down для выгрузки контейнера.
[![Java CI with Gradle](https://github.com/Yaroslav-neto/Diplom_Test_Automation/actions/workflows/manual.yml/badge.svg)](https://github.com/Yaroslav-neto/Diplom_Test_Automation/actions/workflows/manual.yml)

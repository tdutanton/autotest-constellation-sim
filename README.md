# autotest-constellation-sim

Проект для автотестирования проекта **ConstellationSim**.

Набор автотестов для black-box проверки REST API системы ConstellationSim. Проверяет корректность
обработки запросов, валидность ответов и соблюдение контракта эндпоинтов.

## Запуск

Порядок запуска тестов:

1. Запустить сервис ConstellationSim (дополнительно про запуск сервиса указано в **README.md** в
   репозитории ConstellationSim):  
   Находясь в репозитории ConstellationSim выполнить команду

```bash
docker compose up -d server
```

Команда запустит только необходимые для работы основного сервиса контейнеры.  
После запуска server можно ознакомиться с api-doc через swagger по адресу:

**http://localhost:8080/swagger-ui/index.html**

2. Запустить тесты в этом репозитории:

Только тесты:

```bash
gradlew test
```

Allure отчет:

```bash
gradlew allureServe
```

Запустить сразу и тесты и открытие Allure отчета (пользовательская цель сборки добавлена в
build.gradle.kts):

```bash
gradlew testAndReport
```

## Allure отчет

Ниже скриншот из Allure отчета (результат команды gradlew allureServe):

![allure](/readme-resources/allure.JPG)

![allure-suites](/readme-resources/allure-suites.JPG)

### Автор: Anton Evgenev. tg: @tdutanton

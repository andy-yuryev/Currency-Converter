# Конвертер валют

## Запуск приложения
Для запуска необходимо создать базу данных "converter" и задать настройки для доступа в файле application.properties (порт, имя пользователя, пароль). 
Скрипт создания базы данных находится в файле resources/db/migration/V1__Init_DB.sql.

- Сборка

```
mvnw clean install
```

- Запуск
```
java -jar target/currency-converter-0.0.1-SNAPSHOT.jar
```

По умолчанию приложение работает на порту 8080.
# AutoMag - Автомобильная доска объявлений

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## Описание проекта

**AutoMag** — это веб-приложение для размещения и поиска объявлений о продаже автомобилей. Проект разрабатывается в рамках курса [Job4j](https://job4j.ru/) и представляет собой практическую работу по изучению стека технологий Java/Spring Boot.

Основная идея проекта — создание площадки, на которой пользователи могут:

- Регистрироваться и авторизоваться
- Создавать объявления о продаже автомобилей
- Просматривать список объявлений
- Управлять своими объявлениями (редактировать, удалять, закрывать продажу)

## Содержание

- [Описание проекта](#описание-проекта)
- [Содержание](#содержание)
- [Установка и запуск](#установка-запуск)
  - [Предварительные требования](#предварительные-требования)
  - [Клонирование репозитория](#клонирование-репозитория)
  - [Настройка базы данных](#настройка-базы-данных)
  - [Сборка проекта](#сборка-проекта)
  - [Запуск приложения](#запуск-приложения)
- [Структура проекта](#структура-проекта)
- [База данных](#база-данных)
  - [Схема данных](#схема-данных)
  - [Миграции Liquibase](#миграции-liquibase)
- [Функциональность](#функциональность)

## Установка и запуск

### Предварительные требования

Перед запуском приложения необходимо:

1. Установить JDK 17+
2. Установить Apache Maven
3. Установить и настроить PostgreSQL
4. Создать пустую базу данных `cars`

### Клонирование репозитория

```bash
git clone https://github.com/username/job4j_cars.git
cd job4j_cars
```

### Настройка базы данных

1. Подключитесь к PostgreSQL:

```bash
psql -U postgres
```

2. Создайте базу данных:

```sql
CREATE DATABASE cars;
```

3. Выйдите из psql:

```sql
\q
```

4. При необходимости отредактируйте параметры подключения в файле `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/cars
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Сборка проекта

Для компиляции проекта выполните:

```bash
mvn clean compile
```

Для полной сборки с тестами:

```bash
mvn clean package
```

Для пропуска тестов:

```bash
mvn clean package -DskipTests
```

### Запуск приложения

После успешной сборки запустите приложение:

```bash
mvn spring-boot:run
```

или

```bash
java -jar target/social-0.0.1-SNAPSHOT.jar
```

Приложение будет доступно по адресу: http://localhost:8080

## Структура проекта

```
job4j_cars/
├── pom.xml                                    # Maven конфигурация
├── checkstyle.xml                            # Правила проверки стиля кода
├── README.md                                 # Документация проекта
└── src/
    ├── main/
    │   ├── java/
    │   │   └── ru/
    │   │       └── job4j/
    │   │           └── App.java              # Главный класс приложения
    │   └── resources/
    │       ├── application.properties        # Конфигурация Spring Boot
    │       └── db/
    │           ├── dbchangelog.xml          # Мастер-файл миграций
    │           └── scripts/                  # SQL-скрипты миграций
    │               ├── 001_ddl_create_auto_users_table.sql
    │               └── 002_ddl_create_auto_posts_table.sql
    └── test/
        └── java/
            └── ru/
                └── job4j/                    # Тестовые классы
```

## База данных

### Схема данных

В текущей версии определены две основные таблицы:

#### Таблица `auto_user`

| Колонка | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Идентификатор пользователя |
| login | VARCHAR(100) | NOT NULL, UNIQUE | Логин пользователя |
| password | VARCHAR(255) | NOT NULL | Хеш пароля |

#### Таблица `auto_post`

| Колонка | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Идентификатор объявления |
| description | TEXT | NOT NULL | Описание объявления |
| created | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Дата создания |
| auto_user_id | INTEGER | NOT NULL, FK | Ссылка на пользователя |

#### ER-диаграмма

```
┌──────────────────┐         ┌──────────────────┐
│    auto_user     │         │    auto_post      │
├──────────────────┤         ├──────────────────┤
│ * id (PK)        │──┐      │ * id (PK)        │
│   login          │  │      │   description    │
│   password       │  │      │   created        │
└──────────────────┘  │      │ * auto_user_id(FK)│
                      └──────│                  │
                             └──────────────────┘
```

### Миграции Liquibase

Миграции базы данных управляются через **Liquibase**. Все миграции находятся в директории `src/main/resources/db/scripts/`.

#### Файл миграции: 001_ddl_create_auto_users_table.sql

```sql
CREATE TABLE auto_user (
   id SERIAL PRIMARY KEY,
   login VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL
);
```

#### Файл миграции: 002_ddl_create_auto_posts_table.sql

```sql
CREATE TABLE auto_post (
   id SERIAL PRIMARY KEY,
   description TEXT NOT NULL,
   created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   auto_user_id INTEGER NOT NULL,
   CONSTRAINT fk_auto_post_auto_user
       FOREIGN KEY (auto_user_id)
           REFERENCES auto_user(id)
           ON DELETE CASCADE
);
```

#### Мастер-файл миграций (dbchangelog.xml)

```xml
<databaseChangeLog>
    <includeAll path="scripts" relativeToChangelogFile="true"/>
</databaseChangeLog>
```

## Функциональность

В рамках курса Job4j планируется реализовать следующую функциональность:

### Для пользователей (неавторизованных)

- Регистрация нового аккаунта
- Авторизация на сайте
- Просмотр списка всех объявлений
- Просмотр деталей объявления
- Фильтрация и поиск объявлений

### Для пользователей (авторизованных)

- Создание нового объявления
- Редактирование своих объявлений
- Удаление своих объявлений
- Закрытие объявления (продажа)
- Просмотр своих объявлений
- Выход из аккаунта

### Для администраторов (опционально)

- Управление пользователями
- Удаление любых объявлений
- Модерация контента

## Конфигурация

Основные настройки приложения находятся в файле `src/main/resources/application.properties`:

```properties
# Имя приложения
spring.application.name=cars

# Подключение к базе данных
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/cars
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=postgres

# Конфигурация Liquibase
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/dbchangelog.xml
spring.liquibase.url=jdbc:postgresql://127.0.0.1:5432/cars
spring.liquibase.user=postgres
spring.liquibase.password=postgres
```

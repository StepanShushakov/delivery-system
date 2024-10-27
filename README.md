# Delivery System

## Описание проекта

Система автоматизации службы корпоративной логистики интернет-магазина, которая позволяет управлять заказами от момента их оплаты до доставки. Проект построен с использованием микросервисной архитектуры, обеспечивая масштабируемость и гибкость.

### Архитектура проекта

Проект состоит из нескольких микросервисов, каждый из которых выполняет отдельную задачу:

- **Order Service** — управление заказами, отслеживание их статусов.
- **Payment Service** — обработка и проверка платежей.
- **Inventory Service** — проверка наличия товаров на складе для выполнения заказа.
- **Delivery Service** — организация доставки заказов.
- **Authentication Service** — управление пользователями, выдача JWT-токенов для аутентификации.
- **API Gateway** — маршрутизация запросов между сервисами и аутентификация.
- **Discovery and Configuration Service** — обнаружение и настройка сервисов.

### Основные возможности

- **Асинхронная обработка заказов** с использованием Apache Kafka.
- **Поддержка состояния заказов** для отслеживания этапов выполнения:
    - `registered`, `paid`, `payment_failed`, `invented`, `inventment_failed`, `delivered`, `delivery_failed`, `unexpected_failure`.
- **Аутентификация с использованием JWT** для защиты и контроля доступа к сервисам через API Gateway.
- **Управление транзакциями** с использованием паттерна SAGA для обработки распределённых транзакций и отката.

### Основные технологии

- **Java, Spring Boot, Spring Cloud**
- **Apache Kafka** — брокер сообщений для асинхронного взаимодействия между сервисами.
- **PostgreSQL** — база данных, отдельная для каждого сервиса.
- **Docker Compose** — для развертывания и управления сервисами.

## Установка и запуск

### Шаг 1: Запуск инфраструктуры
1. Выполните следующую команду в корневой директории проекта:
    ```bash
    docker-compose up
    ```
   Это запустит контейнеры для PostgreSQL и Apache Kafka.

### Шаг 2: Запуск сервисов
1. Запустите сервисы **Discovery и Configuration** для настройки взаимодействия сервисов.
2. Запустите **Authentication Service** для аутентификации пользователей.
3. Запустите **API Gateway** для маршрутизации запросов.

### Шаг 3: Запуск бизнес-сервисов
- Запустите **Order**, **Payment**, **Inventory** и **Delivery** сервисы.
- Убедитесь, что сервисы подключены к Kafka и API доступны через Gateway.

## API документация

Документация API доступна через Swagger UI по адресу: `http://<gateway_host>/swagger-ui.html` после запуска всех сервисов.

## Тестирование

Проект включает следующие тесты:
- **Юнит-тесты** для каждого модуля.
- **Интеграционные тесты** для проверки взаимодействия между сервисами и базой данных.

## Основные требования и особенности

- Проект следует принципам SOLID и использует шаблон проектирования SAGA для управления транзакциями.
- Проект поддерживает обработку отката транзакций в случае ошибок.
- Все сервисы взаимодействуют асинхронно с использованием Kafka, за исключением разрешённых синхронных взаимодействий через REST API.

## Дополнительные ресурсы

- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Apache Kafka](https://kafka.apache.org/)
- [Swagger/OpenAPI](https://swagger.io/)

---

# Документация Проекта 3: Анализатор логов

## Постановка задачи

Проект представляет собой программу-анализатор логов, которая принимает NGINX лог-файлы, а также необязательные
параметры временного интервала и формат вывода. Программа выполняет фильтрацию, анализирует логи, собирает статистику и
генерирует отчет

## Описание

### 1. Входные данные

___Источник___: *URL* или *glob-выражение* для локальных файлов

___Параметры___:

- Временные фильтры
    - `FROM` - начальная дата
    - `TO` - конечная дата
- Фильтры по полям
    - `ADDRESS` - по IP-адресу
    - `METHOD` - по HTTP методу
    - `STATUS` - по HTTP статусу
    - `AGENT` - по клиентскому устройству
- Формат отчета
    - `MARKDOWN` - в формате Markdown
    - `ADOC` - в формате AsciiDoc

---

### 2. Этапы работы

**Формирование фильтров**: аргументы преобразуются в предикаты для фильтрации записей

**Обработка данных**: чтение входного потока, фильтрация записей и сбор статистики. В статистику включаются:

- Общие показатели:
    - Общее количество запросов
    - Средний размер тела ответа
- Количество запросов, сделанных каждым пользователем
- Частота использования запрашиваемых путей
- Частота использования HTTP-методов
- Частота появления каждого HTTP-статуса
- Количество запросов и ошибок по часам и дням

**Генерация отчета**: После сбора статистики программа формирует отчет в выбранном формате (`MARKDOWN` или `ADOC`).
Отчет содержит следующие разделы:

- ___Общая информация___:
    - название источника/файла
    - количество запросов
    - средний размер ответа
    - 95% перцентиль размера ответа сервера
    - 50% перцентиль размера ответа сервера
- ___Фильтры___: перечень фильтров (в формате `название - значение`)
- ___Запрашиваемые ресурсы___: топ-5 самых запрашиваемых ресурсов (в формате `ресурс - кол-во`)
- ___Коды ответа___: частота популярных HTTP-статусов (в формате `код - название - количество`). Редкие статусы
  группируются под меткой `Less common`
- ___Методы запросов___: Количество запросов по каждому HTTP-методу (в формате `метод - кол-во`)
- ___Самые активные пользователи___:
    - количество уникальных пользователей
    - топ 5 самых активных пользователей (в формате `IP пользователя - количество запросов`)
- ___Активность за день___:
    - День с наибольшей активностью
    - Корреляция между количеством запросов и количеством ошибок за этот день
    - Таблица с почасовой статистикой запросов и ошибок за этот день (в формате
      `Время - Запросы - График запросов - Ошибки - График ошибок - Ошибки (%)`)

---

## Примеры запуска

пример cli аргументов для запуска программы

```
--path "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs"  
--filter-field "METHOD"  
--filter-value "GET"  
--to "2015-06-01"  
--format "MARKDOWN"  
```

вывод в консоль

``` logs
03:40:34.366 INFO  [main           ] backend.academy.filter.LogFilter    -- Фильтр успешно добавлен: METHOD - GET
03:40:34.394 INFO  [main           ] backend.academy.filter.LogFilter    -- Фильтр успешно добавлен: TO - 2015-06-01T00:00
03:40:34.403 INFO  [main           ] .source.discovery.InputTypeDetector -- Ввод распознан как URL: 'https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs'
03:40:34.408 INFO  [main           ] d.academy.manager.StatisticsManager -- Начало чтения данных из источника: 'https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs'
03:40:36.383 INFO  [main           ] d.academy.manager.StatisticsManager -- Завершено чтение данных из источника: 'https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs'
03:40:36.498 INFO  [main           ] ckend.academy.manager.ReportManager -- Создание отчета успешно завершено. Файл: 'report_06-Dec_03-40-36-395.md'. Источник: 'https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs'
```

[ссылочка](report_06-Dec_03-34-19-965.md) на сгенерированный отчет

---
вот еще один примерчик для запуска (через glob выражение) (предварительно в проекте был сделан файлик
`src/main/resources/log/logs.log`)

```
--path "src/main/resources/**/**.log"
--format "ADOC"
```

вывод в консоль

``` logs
03:48:44.658 INFO  [main           ] .source.discovery.InputTypeDetector -- Ввод распознан как PATH: 'src/main/resources/**/**.log'
03:48:44.764 DEBUG [main           ] d.academy.manager.DataReaderManager -- Обнаружено файлов: 1
03:48:44.770 INFO  [main           ] d.academy.manager.StatisticsManager -- Начало чтения данных из источника: 'file:///C:/[...]/backend_academy_2024_project_3-java-m-i-k-o-r/src/main/resources/log/logs.log'
03:48:44.833 INFO  [main           ] d.academy.manager.StatisticsManager -- Завершено чтение данных из источника: 'file:///C:/[...]/backend_academy_2024_project_3-java-m-i-k-o-r/src/main/resources/log/logs.log'
03:48:44.897 INFO  [main           ] ckend.academy.manager.ReportManager -- Создание отчета успешно завершено. Файл: 'report_06-Dec_03-48-44-849.adoc'. Источник: 'file:///C:/[...]/backend_academy_2024_project_3-java-m-i-k-o-r/src/main/resources/log/logs.log'
```

[ссылочка](report_06-Dec_03-48-44-849.adoc) на сгенерированный отчет

---

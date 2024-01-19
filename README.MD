1. Создаем базу данных
2. Создаем файл docker-compose.yml
version: '3.7'
services:
mysql:
image: mysql:8.0.18 // это образ mysql, хорошо подходит для учебного jar файла
restart: always // перезапуск базы, если она упадет
ports:
- '3306:3306'
volumes:
- ./schema:/docker-entrypoint-initdb.d //описание томов (расположение папок в контейнере), если не сделать эту настройку база данных останется пустой
environment:
- MYSQL_RANDOM_ROOT_PASSWORD=yes
- MYSQL_DATABASE=app
- MYSQL_USER=app
- MYSQL_PASSWORD=pass
3. Создаем файл schema.sql
4. Запускаем БД (docker-compose up)
5. Открываем dbeaver
6. Запускаем jar файл
7. Если jar файл был запущен один раз, то нужно удалять содержимое колонок, потом перезапускать. user удалять самым последним
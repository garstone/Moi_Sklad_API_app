# Moi_Sklad_API_app
API для расчета прибыльности.

Суть приложения - расчет себестоимости по FIFO – метод учета по цене первой поступившей партии.

В приложении используются сервлеты, слой DAO отвечает за соединение с PostgreSQL БД, слой Service проверяет корректность данных, выполняется бизнес-логика и вызывается DAO. В сервлетах принимаются только POST запросы с данными в формате JSON, на выход отправляется также JSON. Сервисы и сервлеты покрыты тестами. 

## Установка
Чтобы запустить приложение, Вам необходимы:
* Apache Tomcat 9
* PostgreSQL

1. Скопировать Moi_Sklad_API_app.war из /target в $TOMCAT-HOME$/webapps
2. В $TOMCAT-HOME$\webapps\Moi_Sklad_API_app\WEB-INF\classes изменить название БД, которая будет использоваться в приложении, логин и пароль.
2. Перезапустить Tomcat.
3. Запустить PostgreSQL.
4. Приложение доступно по адресу localhost:$tomcat-port$/
  
## API

https://moisklad.docs.apiary.io/

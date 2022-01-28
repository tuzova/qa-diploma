## Процедура запуска авто-тестов

#### Запустить ПО:  

- IntelliJ IDEA (Ultimate)  
- Docker Desktop 

#### В терминале IDE осуществить шаги:  

1. Запустить контейнер MySQL:  
- `docker-compose up -d`  
2. Создать подключение к MySQL:  
- на вкладке `Database` нажать (![The Add icon](https://resources.jetbrains.com/help/img/idea/2021.3/icons.general.add.svg)) > `Data Source` >  `MySQL`  
- указать детали подключения:  
  - User: **app**
  - Password: **pass**  
  - Database: **app**
- нажать `Test Connection` и дождаться успешного подключения: **Successful**  
- нажать `OK`
3. Запустить gate-simulator:  
- `docker run -p 9999:9999 -d app-gate-simulator`   
4. Запустить SUT:   
- `java -jar artifacts/aqa-shop.jar`   
5. Запустить авто-тесты:  
- `gradlew test` или `Ctrl + Shift + F10`

## Процедура запуска авто-тестов

#### Запустить ПО:  

- IntelliJ IDEA (Ultimate)  
- Docker Desktop 

#### В терминале IDE осуществить шаги: 

| Шаг                                               | Реализация                                                   |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| Запустить контейнер MySQL                                       | **docker-compose up -d** |  
| Создать подключение к MySQL                                       | вкладкa **Database** нажать ![The Add icon](https://resources.jetbrains.com/help/img/idea/2021.3/icons.general.add.svg) > **Data Source** >  **MySQL** > User: **app** > Password: **pass** > Database: **app** > **Test Connection** > **Successful** > OK |
| Запустить gate-simulator                                       | **docker run -p 9999:9999 -d app-gate-simulator** |  
| Запустить SUT                                       | **java -jar artifacts/aqa-shop.jar** |
| Запустить авто-тесты                                       | **gradlew clean test** |

# Тестовое задание Java (ТЗ + описание решения)

Требуется написать программу, для ведения личных трат.
## Запуск

```
docker-compose up

mvn clean spring-boot:run
```

## Суть задачи:

Есть категории трат, в каждой категории может быть один или несколько MCC. MCC являяется уникальным и может быть только
в одной категории. Категории могут быть вложены одна в другую, много-ко-многим, без циклов. Категории содержат
транзакции.

Категории и транзакции должны поддерживать операции создания, удаления, изменения и показания статистики месяца или категории. При подсчете
статистики категории учитываются транзации подкатегорий. 

Данные желательно сохранять между запусками программы.

## Выполнение
Сервер реализует REST API в соответствии с ТЗ.
В качестве фреймворка для решения данной задачи был выбран `Spring Boot`, система сборки `Maven`. Написаны тесты для каждого запроса

В качестве уровня хранения данных использована база данных `PostgreSQL`

В докере развернута база данных и `Liquibase`, где происходит начальная инициализация базы данных

Конфигурация приложения возможна в файле application.properties

Посмотреть доступные конечные точки можно на http://localhost:8080/swagger-ui/index.html#/. Точки разделены по типу запроса (category, transaction), для удобства добавлены теги Category API, Transaction API

За основу было взято предложенное в ТЗ описание, но измененное в соответствии с задачами которые необходимо было выполнить

api.yaml файл можно найти в папке resources. Код писался как API First, поэтому все интерфейсы контроллером и модели были сгенерированы автоматически через плагин в maven

````
<plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.openapitools</groupId>
                <artifactId>openapi-generator-maven-plugin</artifactId>
                <version>${openapi-generator-version}</version>
                <executions>
                    <execution>
                        <id>generate-api</id>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <configuration>
                            <inputSpec>./src/main/resources/api.yaml</inputSpec>
                            <modelPackage>model</modelPackage>
                            <apiPackage>api</apiPackage>
                            <generatorName>spring</generatorName>
                            <packageName>generated</packageName>
                            <configOptions>
                                <serializableModel>true</serializableModel>
                                <interfaceOnly>true</interfaceOnly>
                                <useSpringBoot3>true</useSpringBoot3>
                                <useBeanValidation>true</useBeanValidation>
                            </configOptions>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
````



# Song Genre Predictor with Spark - Big Data Analytics Assignment
A simple Spring-boot application with few API routes to get a prediction of song genre given lyrics.

## Student Details:
| Name               | Index   |
|--------------------|---------|
| Abeykoon A.M.K.H.K | 248201V |

---

### Pop vs. Heavy Metal vs. Other Genres (Coming soon)
Given verse from verse1 recognize genre.

Strategy:
* Collect raw data set of verse1 (~65k sentences in total):
    * Abba, Ace of base, Backstreet Boys, Britney Spears, Christina Aguilera, Madonna, etc.
    * Black Sabbath, In Flames, Iron Maiden, Metallica, Moonspell, Nightwish, Sentenced, etc.
* Create training set, i.e. label (0|1) + features
* Train logistic regression


### Configuration
All available configuration properties are spread out via 3 files:
* application.properties - contains business logic specific stuff
* spark.properties - contains Spark specific stuff

All properties are self explanatory, but few the most important ones are listed explicitly below.

#### Application Properties
| Name | Type | Default value | Description |
| ---- | ---- | ------------- | ----------- |
| server.port | Integer | 9090 | The port to listen for incoming HTTP requests |

#### Spark Properties
| Name | Type | Default value | Description |
| ---- | ---- | ------------- | ----------- |
| spark.master | String | spark://127.0.0.1:7077 | The URL of the Spark master. For development purposes, you can use `local[n]` that will run Spark on n threads on the local machine without connecting to a cluster. For example, `local[2]`. |
|spark.distributed-libraries | String | | Path to distributed library that should be loaded into each worker of a Spark cluster. |

#### Sample configuration for a local development environment
Create *application.properties* (for instance, in your user home directory) and override any of the described properties.
For instance, minimum set of values that should be specified for your local environment is listed below:

```
spark.distributed-libraries=<path_to_your_repo>/spark-distributed-library/build/libs/spark-distributed-library-1.0-SNAPSHOT-all.jar

verse1.training.set.directory.path=<path_to_your_repo>/training-set/verse1/
verse1.model.directory.path=<path_to_your_repo>/training-set/verse1/model
lyrics.pipeline=<Selection from 5 possible pipelines: LogisticRegressionPipeline, NaiveBayesBagOfWordsPipeline, NaiveBayesTFIDFPipeline, RandomForestPipeline and FeedForwardNeuralNetworkPipeline>

```

### Run

From your favourite IDE plese run `ApplicationConfiguration` main method.
This will use default configuration bundled in the source code.

In order to run the application with custom configuration please add spring.config.location parameter that corresponds to directory that contains your custom *application.properties* (in our example your user home directory). Or just enumerate them explicitly, for instance:
```
spring.config.location=/Users/<your user>/application.properties
```



### Kafka  Demo

## Introduction
This Java project demonstrates the usage of Apache Kafka for building various components like Kafka Producer, Kafka Consumer, Kafka Streaming, and integration with OpenSearch. It includes implementations of a simple Kafka Producer with key and callback, a cooperative Kafka Consumer, a Kafka Consumer with graceful shutdown, and a Kafka and OpenSearch integration using data obtained from the Wikimedia API. The project also assumes the usage of a Dockerized OpenSearch service for storing and retrieving data.


## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Configuration](#configuration)
- [Version Compatibility](#contributing)
- [Contributing](#contributing)
- [Contact](#contact)

## Features
- Basics of Kafka Java programming;
	- consumer
		- simple consumer
		- cooperative consumer<br>
			Reassigning a small subset of the partitions from one consumer to another
		- shutdown consumer<br>
			Ensure we have code in place to respond to termination signals. <br>
				Please pay attention to the exception of hook execution like: <br>
				1. No guarantee for the execution of the shutdown hooks. <br>
				2. when the application is terminated normally the shutdown hooks are called <br>
				3. Before completion, the shutdown hooks can be stopped forcefully <br>
				 4. There can be more than one shutdown hook, but there execution order is not guaranteed. <br>
				 5. Within shutdown hooks, it is not allowed to unregister or register the shutdown hooks <br>

	- producer
		- simple producer
		- callback producer<br>
			For performing the callbacks, the user needs to implement a callback function. This function is implemented for asynchronously handling the request completion.
		- key producer<br>
			Producer can choose to send a key with the message
If the key is null send data to the partition by round robin method.
A key is typically sent if you need message ordering for a specific field.

	- Wikimedia Producer;
	- OpenSearch Consumer;
	- Kafka Streams Sample Application;

## Installation

To set up the project, follow these steps:

1. Install Apache Kafka by following the official [installation guide](https://kafka.apache.org/documentation/#quickstart).
2. Set up Docker and install OpenSearch using the official [OpenSearch Docker guide](https://opensearch.org/docs/latest/).
3. Clone the repository:
To get started with this project, you can clone the repository by running the following command:
	git clone https://github.com/AlirezaAslani/kafka-demo
4. Build the project using your Maven build tool.


## Configuration
To use this code, please pay attention to the variables such as bootstrap_servers and others. I used the Properties object in my code because someone might need to run each code separately for testing purposes. I believe it enhances readability, especially for those who are new to Kafka. <br>
<br>
**Note:**<br>
However, in a real project, it is advisable to create a separate properties file.

## Version Compatibility

| Java  | Maven |
| ------------- | ------------- |
| 1.8.0_321  | 3.8.6  |

## Contributing
Contributions are welcome! If you have any ideas, suggestions, or bug fixes, feel free to submit a pull request. 

## Contact
For any inquiries or support, please contact alireza.a.eng@gmail.com.

This is just a template, so make sure to customize it with the appropriate details specific to your project.

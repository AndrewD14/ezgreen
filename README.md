# EZ Green
The EZ Green application helps make managing your greenhouse easy.

The application currently uses ReactJS for the frontend and Springboot for the backend.

## Getting started
The code runs on Java 17 and Spring boot. To work with the code, you will need also Lombok installed in your environment.

Make sure to have the following installed:
* Java 17 _AdoptOpenJDK._ [adoptium](https://adoptium.net/)
* Java IDE environment. Spring Tool Suite works well for this project: [STS](https://spring.io/tools)
* Lombok installed for your environment: [Lombok](https://projectlombok.org/download)

Once you have the required tools installed, clone the repo and then import it as a **Maven** project.

## Setting up the environment (_You will need to match the variable exactly_)
To run the application, you will need to configure some environment variables:
* TBD

You need to create a build process in your IDE with the commands: clean install

__*You need to do at least 1 build process to get the website to run from the JAR.*__

## Development
The server port must match port in the .env file you put in the src/main/resource/ui folder.

You have 2 ways to run this app:
1. You can run the app as a whole. Just kick up the maven program as a Springboot app in your IDE. The port is reference from the application.properties. It is to note that you must have a build first if you want to access the UI because the app will then run from a production build that would have been copied to the resouces/static folder. __*The methods will fail if you edit or create as the username is not populated this way!*__
2. The second option is to spin up the app in the IDE for Java as a Springboot app. Then open a CLI and navigate to the UI folder and run the command __npm start__ to spin the React app separately. This will allow hot swaps to work for both. *You will need to have created the .env file to ensure this works.*

.env sample:
```
REACT_APP_SERVER_PORT
```

## Deployment
When ready to deploy the code to AWS, the current process is manual. For now, just run the __maven build__ command and then upload the JAR file that is created.
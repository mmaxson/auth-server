

Database:
    passwords are encrypted with https://www.dailycred.com/article/bcrypt-calculator  (round 4)

Build:
    mvn flyway:migrate (mvn flyway:repair after a failure; 
    mvn flyway:clean, mvn flyway:migrate to run all scripts from start


    mvn clean package docker:build
    docker build -t marku/auth-server target/docker
    docker push marku/auth-server
    docker pull marku/auth-server





Run:


    Shell:

        Env vars: LOG_APPENDER=Console-Appender

    Docker:

        docker run -d -p  8888:8888 -e 3306:3306   -e "LOG_APPENDER=Console-Appender"  -e SECURITY_USER_PASSWORD=marku -e SPRING_PROFILES_ACTIVE=aws -e CLOUD_CONFIG_USER_PASSWORD=marku -e CLOUD_CONFIG_IP=35.165.250.58 -e CLOUD_CONFIG_PORT=80   marku/auth-server --name auth-server
              CLOUD_CONFIG_IP may need to be changed
# Steer Project Manager - Backend
## About
This is a Spring Boot backend for our university final project. 
You can manage project, track spending, add salaries, manage work and how much work can user have assigned.

## Setup 
1. You will need maven, postgresql database and clerk instance
2. Edit postgresql connection in application.properties
3. Edit clerk configuration in application.properties, you will find all information you need on clerk dashboard, go see clerk docs for more information
4. If you want email service to work also configure email properties in application.properties
5. Run `mvn clean install` to build the project
6. After that you can start the project by running the main class, make sure you are using correct java version specified in pom.xml

Alternatively you can use and SQL database but we used postgresql so we can't guarantee it will work with other databases.

## Clerk Webhooks
To make sure user data syncs with clerk on local machine you will need to create webhooks on clerk dashboard.
And you will also need either ngrok or some other tunneling service to make your localhost available to clerk.
1. Go to clerk dashboard and create 3 webhooks for user.created, user.updated and user.deleted, each has it's own endpoint, [server_url]/api/clerk/webhook/user-created, [server_url]/api/clerk/webhook/user-updated and [server_url]/api/clerk/webhook/user-deleted
2. On webhooks you will need to set API key header, secret is specified in application.properties as `api.key.clerk` and header name is X-API

## Mapper generation
If you modify any mappers or create new mapper you will need to regenerate them by running `mvn clean install`

## Swagger
Swagger ui can be found on: [server_url]/api/swagger-ui/index.html, default is http://localhost:8080/api/swagger-ui/index.html
Yaml specification is on: [server_url]/api/docs.yaml, default is http://localhost:8080/api/docs.yaml

## Docs
You can find gitbook documentation on: https://steer-1.gitbook.io/steer
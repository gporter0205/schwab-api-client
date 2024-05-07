# Charles Schwab Java API Client

Spring Boot Java rest client for the [Charles Schwab API](https://developer.schwab.com/).  
Currently, only the happy path of the Schwab Market Data API is implemented.  I'll be adding
the Accounts and Trading API endpoints as well as better error handling in the future.

* Javadocs and Source is included when building the jar or by downloading it as a dependency from Maven.
* [How-To Guide](https://github.com/gporter0205/schwab-api-client/wiki/home) on the Wiki shows specifics on how to use this Java API.
* [Simple Client Example](https://github.com/gporter0205/schwab-client-example) - Simple Spring Boot Java and Maven example project.

## Required Information

You'll need a Schwab API client ID and client secret to get started. See [User Registration](https://developer.schwab.com/user-guides/get-started/user-registration) for help creating a Schwab developer account and [Create an App](https://developer.schwab.com/user-guides/apis-and-apps/create-an-app) for help with creating an app on the [Schwab Developer Portal](https://developer.schwab.com/).

## Build

To build the jar from a commandline, run the following:

```bash
git clone https://github.com/gporter0205/schwab-api-client.git
cd schwab-api-client
mvn clean install
```
You do not need to build the project to use it. The latest release is available on Maven Central,
so just include the dependency in your Maven pom or Gradle build file. 

## Integration Tests
Integration tests do require a client app ID and refresh token to run.

To run unit tests, you will need create the file *src/test/resources/application-unittest.yml* 
and fill in the necessary Schwab properties:

```yml
schwab-api:
  oauth2:
    userId: <ANY-USERID-LIKE-STRING>
    refreshToken: <VALID-REFRESH-TOKEN>
    refreshExpiration: <yyyy-mm-ddThh:mm:ss.ffffff>
    clientId: <VALID-CLIENT-ID>
    clientSecret: <VALID-CLIENT-SECRET>
    redirect-uri: <VALID-REDIRECT-URI>
```

## POJO `otherfields` Property
In order to keep additions to the Schwab API from being breaking, and given that some of the documentation is out of sync with the actual responses, 
an `otherfields` Map is contained in most model classes where you can get any new or undocumented fields using code similar
to the following:

```java
QuoteResponse quote = schwabMarketDataClient.fetchQuote("msft");
String someField = (String)quote.getOtherFields().get("someField"))
```

## Error Handling

If a Refresh Token is invalid or expired, an InvalidRefreshTokenException is thrown.  This is the trigger that should
start whatever your flow is to retrieve a refresh token.

Certain fetch methods throw custom exceptions in specific situations.  For example, fetchQuote will throw a
SymbolNotFoundException if (obviously) the symbol is not valid and the API can't return a quote.  See the JavaDocs for
specific exception info.
 
## Logging
The API uses the Lombok @Slf4j annotation to instantiate a log object.

## TODO
* Many, many things....

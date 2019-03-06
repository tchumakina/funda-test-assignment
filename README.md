# funda-test-assignment
UI automation project for testing quick search component using Java and Selenium WebDriver

## Requirements:
- Windows
- JDK8+
- Maven
- Latest Chrome or Firefox version

### Tests can be run against Chrome or Firefox browsers. 
- Clone this project locally
- Open terminal and navigate to the directory with project
#### To run tests in Chrome:
> _by default tests are executed against Chrome browser_

`mvn test`

or

`mvn test -Dbrowser=chrome`

#### To run tests in Firefox:
`mvn test -Dbrowser=firefox`

or

`mvn test -Dbrowser=ff`

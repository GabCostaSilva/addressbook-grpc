## Compile
`mvn clean install`
## Run server
`mvn clean install exec:java -Dexec.mainClass=com.addressbook.AddressBookServer`
## Run client
`mvn clean package exec:java -Dexec.mainClass=com.addressbook.AddressBookClient`

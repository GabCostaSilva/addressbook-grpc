read USERNAME
if [ -z "$USERNAME" ]; then
  USERNAME=$USER;
fi
java -cp target/addressbook-1.0-SNAPSHOT-jar-with-dependencies.jar -Dusername="$USERNAME" com.chat.ChatClient

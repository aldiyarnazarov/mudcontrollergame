# mudcontrollergame
Compile the code:

javac -d out src/com/example/mud/controller/MUDController.java \
    src/com/example/mud/player/Player.java \
    src/com/example/mud/room/Room.java \
    src/com/example/mud/item/Item.java
    
Run the game:

java -cp out com.example.mud.controller.MUDController

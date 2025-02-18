package com.example.mud.controller;

import java.util.Scanner;
import com.example.mud.player.Player;
import com.example.mud.room.Room;
import com.example.mud.item.Item;

public class MUDController {

    private final Player player;
    private boolean running;
    private final Scanner scanner;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
    }

    public void runGameLoop() {
        System.out.println("Welcome to the MUD game! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
        System.out.println("Goodbye!");
    }

    public void handleInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("Invalid command. Try 'pick up <itemName>'.");
                }
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            System.out.println(currentRoom.describe());
        } else {
            System.out.println("You are in an undefined space!");
        }
    }

    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Specify a direction: forward, back, left, right.");
            return;
        }

        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You move " + direction + ".");
            lookAround();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item != null) {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up " + itemName + ".");
        } else {
            System.out.println("No item named " + itemName + " here!");
        }
    }

    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room");
        System.out.println("move <forward|back|left|right> - Move in a specified direction");
        System.out.println("pick up <itemName> - Pick up an item");
        System.out.println("inventory - List items you are carrying");
        System.out.println("help - Show this menu");
        System.out.println("quit or exit - End the game");
    }

    public static void main(String[] args) {
        Room startRoom = new Room("A small stone chamber", "The walls are damp and cold.");
        Room nextRoom = new Room("A dark hallway", "It's eerily silent here.");
        startRoom.setConnectedRoom("forward", nextRoom);

        Item sword = new Item("sword", "A sharp-looking sword.");
        startRoom.addItem(sword);

        Player player = new Player(startRoom);
        MUDController controller = new MUDController(player);
        controller.runGameLoop();
    }
}
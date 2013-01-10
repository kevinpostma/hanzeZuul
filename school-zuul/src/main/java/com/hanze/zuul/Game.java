package com.hanze.zuul;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
    * Create and play a game.
    */
    public static void main(String [] args) {
        Game game = new Game();
        game.play();
    }
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            if (processCommand(command).equals("quit")) {
                finished = true;                
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private String printWelcome()
    {
        String welcome = "\n";
        welcome += "Welcome to the World of Zuul!" + "\n";
        welcome += "World of Zuul is a new, incredibly boring adventure game." + "\n";
        welcome += "Type '" + CommandWord.HELP + "' if you need help." + "\n";
        welcome += "\n";
        welcome += currentRoom.getLongDescription();
        System.out.print(welcome);
        return welcome;
        
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return "true" If the command ends the game, response otherwise.
     */
    private String processCommand(Command command) 
    {
        String response = "";

        CommandWord commandWord = command.getCommandWord();

        if(commandWord == CommandWord.UNKNOWN) {
            response = "I don't know what you mean...";
            System.out.println(response);
            return response;
        }

        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            response = quit(command);
        }
        // else command not recognised.
        return response;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private String printHelp() 
    {
        String help = "You are lost. You are alone. You wander" + "\n";
        help += "around at the university." + "\n";
        help += "\n";
        help += "Your command words are:" +  "\n";
        help += parser.showCommands();
        System.out.println(help);
        return help;
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private String goRoom(Command command) 
    {
        String room = "";
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            room = "Go where?";
            System.out.println(room);
            return room;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            room = "There is no door!";
        }
        else {
            currentRoom = nextRoom;
            room = currentRoom.getLongDescription();
        }
        System.out.println(room);
        return room;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return "quit", if this command quits the game, a confirmation question otherwise.
     */
    private String quit(Command command) 
    {
        String response = "quit";
        if(command.hasSecondWord()) {
            response = "Quit what?";
            System.out.println(response);
        }
        return response;
    }

}

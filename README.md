# 🗣️ Multi-threaded Chat Application

A Java-based chat application that implements multi-threading and the Command Pattern for efficient message handling and extensible command processing.

## 📋 Project Requirements

1. All users connected to the server must be able to communicate with each other
2. The following chat commands must be implemented:
   - Entry (`/join|{name}`): Users must enter their name when first connecting
   - Message (`/message|{content}`): Send messages to all users
   - Name Change (`/change|{name}`): Change user's name
   - User List (`/users`): Display list of all connected users
   - Exit (`/exit`): Disconnect from the chat server

## 🌟 Key Features

- Real-time chat communication with multiple clients
- Multi-threaded message handling (separate read/write handlers)
- Command-based message processing
- Extensible command system using Command Pattern
- Null Object Pattern implementation
- Thread-safe session management

## 🔧 Core Architecture

### Thread Separation
The application separates read and write operations into different threads to prevent blocking:

```java
public class Client {
    public void start() throws IOException {
        readHandler = new ReadHandler(input, this);
        writeHandler = new WriteHandler(output, this);
        
        Thread readThread = new Thread(readHandler, "readHandler");
        Thread writeThread = new Thread(writeHandler, "writeHandler");
        
        readThread.start();
        writeThread.start();
    }
}
```

### Command Pattern Implementation
Commands are handled through a flexible command system:

```java
public class CommandManager{
    private final Map<String, Command> commands;
    private final Command defaultCommand = new DefaultCommand();
    
    public CommandManager(SessionManager sessionManager) {
        commands = new HashMap<>();
        commands.put("/join", new JoinCommand(sessionManager));
        commands.put("/message", new MessageCommand(sessionManager));
        commands.put("/change", new ChangeCommand(sessionManager));
        commands.put("/users", new UsersCommand(sessionManager));
        commands.put("/exit", new ExitCommand());
    }
    
    @Override
    public void execute(String message, Session session) throws IOException {
        String[] args = message.split(DELIMITER);
        String key = args[0];
        Command command = commands.getOrDefault(key, defaultCommand);
        command.execute(args, session);
    }
}


```
```
 ├── CommandManager.java
 └── command/
      ├── Command.java
      ├── DefaultCommand.java
      ├── JoinCommand.java
      ├── MessageCommand.java
      ├── ChangeCommand.java
      ├── UsersCommand.java
      └── ExitCommand.java      
```

## 💭 Command Usage Examples

```bash
/join|John           # Join chat as John
/message|Hello all!  # Send "Hello all!" to everyone
/change|Johnny      # Change username to Johnny
/users              # List all connected users
/exit               # Leave chat
```

## 🎯 Learning Outcomes

1. **Thread Management**
   - Implemented separate read and write handlers to prevent infinite waiting
   - Enhanced application responsiveness by avoiding blocking operations

2. **Design Patterns**
   - Utilized Command Pattern for extensible command processing
   - Implemented Null Object Pattern to handle invalid commands gracefully
   - Minimized code changes when adding new features

3. **Concurrent Programming**
   - Learned thread synchronization techniques
   - Implemented thread-safe session management
   - Handled concurrent client connections effectively

## 🔒 Thread Safety

The application ensures thread safety through:
- Synchronized methods in SessionManager
- Atomic operations for critical sections
- Immutable command objects
- Thread-safe collections

## 🚀 Future Improvements

- Add private messaging functionality
- Implement chat rooms
- Add file sharing capabilities
- Enhance error handling
- Add user authentication

## 📚 Technical Stack

- Java
- Socket Programming
- Multi-threading
- Design Patterns (Command, Null Object)

## 🌐 Network Architecture

```
Client 1 ─┐
Client 2 ─┼── Server (Port: 12346)
Client 3 ─┘
```

## 📁 Project Structure

```
src/
├── util/
│   └── MyLogger.java
│
└── chatroom/
    ├── client/
    │   ├── ClientMain.java
    │   ├── Client.java
    │   ├── ReadHandler.java
    │   └── WriteHandler.java
    │
    └── server/
        ├── ServerMain.java
        ├── Server.java
        ├── Session.java
        ├── SessionManager.java
        ├── CommandManager.java
        │
        └── command/
            ├── Command.java
            ├── DefaultCommand.java
            ├── JoinCommand.java
            ├── MessageCommand.java
            ├── ChangeCommand.java
            ├── UsersCommand.java
            └── ExitCommand.java
```


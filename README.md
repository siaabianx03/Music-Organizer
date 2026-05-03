# Music Organizer Final Project

This is a simple Java GUI application for organizing music. It uses Java Swing and lets the user add songs, enter song titles and artists, manage playlists, and search for songs.

## How to Run

Open a terminal in this folder and run:

```bash
javac MusicOrganizerApp.java
java MusicOrganizerApp
```

After running the second command, a window named **Music Organizer** will open.

## Features

- Add music with song title, artist, and genre
- Create playlists
- Add music to playlists
- View all songs
- View all playlists
- Search songs by title or artist
- Remove music from a playlist
- Simple GUI using Java Swing components such as `JFrame`, `JPanel`, `JTextField`, `JButton`, `JList`, and `JComboBox`

## Topics Used From the PDFs

- **Variables:** Used for song title, artist, genre, menu choices, and indexes.
- **Conditional Statements for Strings:** Used in the search function with `contains()` and `toLowerCase()`.
- **Methods:** Used to separate each GUI feature, such as `addSong()`, `createPlaylist()`, `searchSongs()`, and `refreshDisplay()`.
- **Classes and Objects:** Used through `Song`, `Playlist`, and `MusicLibrary` objects.
- **Encapsulation:** Class fields are private and accessed through getters, setters, and class methods.
- **Inheritance:** `Song` extends the abstract `MusicItem` class.
- **Abstraction:** `MusicItem` is an abstract class with an abstract `getDisplayText()` method.
- **Recursion:** The search feature uses `recursiveSearch()` to check songs one by one.
- **Exception Handling:** `try-catch` and a custom `InvalidInputException` handle invalid user input.
- **GUI Programming:** The program uses Java Swing to create a window, buttons, text fields, combo boxes, and lists.

## Reporting Guide

You can explain the program in this order:

1. The `main()` method starts the program and opens the GUI window.
2. The user types information into text fields and clicks buttons.
3. The program stores songs inside an `ArrayList<Song>`.
4. The program stores playlists inside an `ArrayList<Playlist>`.
5. The `Song` class represents one music item.
6. The `Playlist` class groups songs together.
7. The `MusicLibrary` class manages all songs, playlists, and searching.
8. The search function compares the keyword with the song title and artist.
9. The `refreshDisplay()` method updates the GUI lists and combo boxes after changes.
10. Exception handling prevents the program from crashing when the user enters invalid input.

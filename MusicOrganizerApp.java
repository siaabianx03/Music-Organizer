import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/*
 * Music Organizer GUI
 * A simple Java Swing final project for organizing songs and playlists.
 *
 * Topics shown from the provided lessons:
 * - Variables and data types
 * - Conditional statements for Strings
 * - Methods
 * - Classes and objects
 * - Encapsulation
 * - Inheritance
 * - Abstraction
 * - Recursion
 * - Exception handling
 */
public class MusicOrganizerApp extends JFrame {
    /*
     * Spotify-inspired color palette:
     * blue for buttons, dark colors for the background, and white text.
     */
    private static final Color ACCENT_BLUE = new Color(30, 144, 255);
    private static final Color BACKGROUND_BLACK = new Color(18, 18, 18);
    private static final Color PANEL_DARK = new Color(24, 24, 24);
    private static final Color FIELD_DARK = new Color(40, 40, 40);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_GRAY = new Color(179, 179, 179);

    private MusicLibrary library;

    private JTextField titleField;
    private JTextField artistField;
    private JTextField genreField;
    private JTextField playlistField;
    private JTextField searchField;

    private JComboBox<String> songComboBox;
    private JComboBox<String> playlistComboBox;
    private JComboBox<String> removeSongComboBox;

    private DefaultListModel<String> songListModel;
    private DefaultListModel<String> playlistListModel;
    private DefaultListModel<String> searchListModel;

    public static void main(String[] args) {
        /*
         * SwingUtilities.invokeLater makes sure the GUI runs properly
         * on Java's Event Dispatch Thread.
         */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MusicOrganizerApp().setVisible(true);
            }
        });
    }

    public MusicOrganizerApp() {
        library = new MusicLibrary();
        seedSampleData();

        setTitle("Music Organizer");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_BLACK);

        add(createInputPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);

        refreshDisplay();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(PANEL_DARK);
        panel.setBorder(createTitleBorder("Music and Playlist Controls"));

        JPanel songPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        songPanel.setBackground(PANEL_DARK);
        titleField = new JTextField();
        artistField = new JTextField();
        genreField = new JTextField();
        JButton addSongButton = new JButton("Add Music");

        styleTextField(titleField);
        styleTextField(artistField);
        styleTextField(genreField);
        styleButton(addSongButton);

        songPanel.add(createLabel("Title:"));
        songPanel.add(titleField);
        songPanel.add(createLabel("Artist:"));
        songPanel.add(artistField);
        songPanel.add(createLabel("Genre:"));
        songPanel.add(genreField);
        songPanel.add(addSongButton);

        JPanel playlistPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        playlistPanel.setBackground(PANEL_DARK);
        playlistField = new JTextField();
        JButton createPlaylistButton = new JButton("Create Playlist");

        styleTextField(playlistField);
        styleButton(createPlaylistButton);

        playlistPanel.add(createLabel("Playlist Name:"));
        playlistPanel.add(playlistField);
        playlistPanel.add(createPlaylistButton);
        playlistPanel.add(createLabel(""));

        JPanel managePanel = new JPanel(new GridLayout(1, 5, 5, 5));
        managePanel.setBackground(PANEL_DARK);
        songComboBox = new JComboBox<String>();
        playlistComboBox = new JComboBox<String>();
        JButton addToPlaylistButton = new JButton("Add To Playlist");
        removeSongComboBox = new JComboBox<String>();
        JButton removeButton = new JButton("Remove From Playlist");

        styleComboBox(songComboBox);
        styleComboBox(playlistComboBox);
        styleComboBox(removeSongComboBox);
        styleButton(addToPlaylistButton);
        styleButton(removeButton);

        managePanel.add(songComboBox);
        managePanel.add(playlistComboBox);
        managePanel.add(addToPlaylistButton);
        managePanel.add(removeSongComboBox);
        managePanel.add(removeButton);

        addSongButton.addActionListener(event -> addSong());
        createPlaylistButton.addActionListener(event -> createPlaylist());
        addToPlaylistButton.addActionListener(event -> addSongToPlaylist());
        removeButton.addActionListener(event -> removeSongFromPlaylist());
        playlistComboBox.addActionListener(event -> refreshRemoveSongComboBox());

        panel.add(songPanel);
        panel.add(playlistPanel);
        panel.add(managePanel);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBackground(BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        songListModel = new DefaultListModel<String>();
        playlistListModel = new DefaultListModel<String>();

        JList<String> songList = new JList<String>(songListModel);
        JList<String> playlistList = new JList<String>(playlistListModel);

        JScrollPane songScrollPane = new JScrollPane(songList);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);

        styleList(songList);
        styleList(playlistList);
        styleScrollPane(songScrollPane, "All Songs");
        styleScrollPane(playlistScrollPane, "Playlists");

        panel.add(songScrollPane);
        panel.add(playlistScrollPane);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PANEL_DARK);
        panel.setBorder(createTitleBorder("Search Song"));

        JPanel searchInputPanel = new JPanel(new BorderLayout(5, 5));
        searchInputPanel.setBackground(PANEL_DARK);
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        styleTextField(searchField);
        styleButton(searchButton);

        searchInputPanel.add(createLabel("Title or Artist: "), BorderLayout.WEST);
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        searchListModel = new DefaultListModel<String>();
        JList<String> searchList = new JList<String>(searchListModel);
        JScrollPane searchScrollPane = new JScrollPane(searchList);

        styleList(searchList);
        styleScrollPane(searchScrollPane, "Search Results");

        searchButton.addActionListener(event -> searchSongs());

        panel.add(searchInputPanel, BorderLayout.NORTH);
        panel.add(searchScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_WHITE);
        return label;
    }

    private void styleTextField(JTextField textField) {
        textField.setBackground(FIELD_DARK);
        textField.setForeground(TEXT_WHITE);
        textField.setCaretColor(TEXT_WHITE);
        textField.setBorder(BorderFactory.createLineBorder(TEXT_GRAY));
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT_BLUE);
        button.setForeground(BACKGROUND_BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(FIELD_DARK);
        comboBox.setForeground(TEXT_WHITE);
    }

    private void styleList(JList<String> list) {
        list.setBackground(FIELD_DARK);
        list.setForeground(TEXT_WHITE);
        list.setSelectionBackground(ACCENT_BLUE);
        list.setSelectionForeground(BACKGROUND_BLACK);
    }

    private void styleScrollPane(JScrollPane scrollPane, String title) {
        scrollPane.setBackground(PANEL_DARK);
        scrollPane.getViewport().setBackground(FIELD_DARK);
        scrollPane.setBorder(createTitleBorder(title));
    }

    private TitledBorder createTitleBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_GRAY), title);
        border.setTitleColor(TEXT_WHITE);
        return border;
    }

    private void addSong() {
        try {
            String title = readRequiredText(titleField, "Song title");
            String artist = readRequiredText(artistField, "Artist");
            String genre = readRequiredText(genreField, "Genre");

            Song song = new Song(title, artist, genre);
            library.addSong(song);

            titleField.setText("");
            artistField.setText("");
            genreField.setText("");

            refreshDisplay();
            showMessage("Song added successfully!");
        } catch (InvalidInputException error) {
            showError(error.getMessage());
        }
    }

    private void createPlaylist() {
        try {
            String playlistName = readRequiredText(playlistField, "Playlist name");
            library.createPlaylist(playlistName);

            playlistField.setText("");

            refreshDisplay();
            showMessage("Playlist created successfully!");
        } catch (InvalidInputException error) {
            showError(error.getMessage());
        }
    }

    private void addSongToPlaylist() {
        try {
            int songIndex = songComboBox.getSelectedIndex();
            int playlistIndex = playlistComboBox.getSelectedIndex();

            library.addSongToPlaylist(songIndex, playlistIndex);

            refreshDisplay();
            showMessage("Song added to playlist successfully!");
        } catch (InvalidInputException error) {
            showError(error.getMessage());
        }
    }

    private void removeSongFromPlaylist() {
        try {
            int playlistIndex = playlistComboBox.getSelectedIndex();
            int songIndex = removeSongComboBox.getSelectedIndex();

            Playlist playlist = library.getPlaylistAt(playlistIndex);
            playlist.removeSong(songIndex);

            refreshDisplay();
            showMessage("Song removed from playlist successfully!");
        } catch (InvalidInputException error) {
            showError(error.getMessage());
        }
    }

    private void searchSongs() {
        try {
            String keyword = readRequiredText(searchField, "Search keyword");
            ArrayList<Song> results = library.search(keyword);

            searchListModel.clear();

            if (results.isEmpty()) {
                searchListModel.addElement("No matching songs found.");
                return;
            }

            for (Song song : results) {
                searchListModel.addElement(song.getDisplayText());
            }
        } catch (InvalidInputException error) {
            showError(error.getMessage());
        }
    }

    private String readRequiredText(JTextField field, String fieldName) throws InvalidInputException {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be blank.");
        }

        return text;
    }

    private void refreshDisplay() {
        refreshSongList();
        refreshPlaylistList();
        refreshComboBoxes();
        refreshRemoveSongComboBox();
    }

    private void refreshSongList() {
        songListModel.clear();

        if (library.getSongs().isEmpty()) {
            songListModel.addElement("No songs added yet.");
            return;
        }

        for (int i = 0; i < library.getSongs().size(); i++) {
            Song song = library.getSongs().get(i);
            songListModel.addElement((i + 1) + ". " + song.getDisplayText());
        }
    }

    private void refreshPlaylistList() {
        playlistListModel.clear();

        if (library.getPlaylists().isEmpty()) {
            playlistListModel.addElement("No playlists created yet.");
            return;
        }

        for (int i = 0; i < library.getPlaylists().size(); i++) {
            Playlist playlist = library.getPlaylists().get(i);
            playlistListModel.addElement((i + 1) + ". " + playlist.getName()
                    + " (" + playlist.getSongs().size() + " song/s)");

            for (int j = 0; j < playlist.getSongs().size(); j++) {
                Song song = playlist.getSongs().get(j);
                playlistListModel.addElement("   " + (j + 1) + ". " + song.getDisplayText());
            }
        }
    }

    private void refreshComboBoxes() {
        songComboBox.removeAllItems();
        playlistComboBox.removeAllItems();

        for (Song song : library.getSongs()) {
            songComboBox.addItem(song.getDisplayText());
        }

        for (Playlist playlist : library.getPlaylists()) {
            playlistComboBox.addItem(playlist.getName());
        }
    }

    private void refreshRemoveSongComboBox() {
        removeSongComboBox.removeAllItems();

        int playlistIndex = playlistComboBox.getSelectedIndex();

        if (playlistIndex < 0 || playlistIndex >= library.getPlaylists().size()) {
            return;
        }

        Playlist selectedPlaylist = library.getPlaylists().get(playlistIndex);

        for (Song song : selectedPlaylist.getSongs()) {
            removeSongComboBox.addItem(song.getDisplayText());
        }
    }

    private void seedSampleData() {
        library.addSong(new Song("Until I Found You", "Stephen Sanchez", "Pop"));
        library.addSong(new Song("Pasilyo", "Sunkissed Lola", "OPM"));
        library.createPlaylist("Favorites");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Music Organizer", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

/*
 * Abstraction:
 * This abstract class gives a general structure for music-related items.
 * A Song is a more specific type of MusicItem.
 */
abstract class MusicItem {
    private String title;

    public MusicItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract String getDisplayText();
}

/*
 * Inheritance:
 * Song extends MusicItem, so Song gets the title property
 * and also provides its own version of getDisplayText().
 */
class Song extends MusicItem {
    private String artist;
    private String genre;

    public Song(String title, String artist, String genre) {
        super(title);
        this.artist = artist;
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String getDisplayText() {
        return getTitle() + " by " + artist + " [" + genre + "]";
    }
}

/*
 * Encapsulation:
 * Playlist keeps its ArrayList private and controls access using methods.
 */
class Playlist {
    private String name;
    private ArrayList<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(int index) throws InvalidInputException {
        if (index < 0 || index >= songs.size()) {
            throw new InvalidInputException("Choose a valid song from the selected playlist.");
        }

        songs.remove(index);
    }
}

/*
 * MusicLibrary is the main class that manages all songs and playlists.
 */
class MusicLibrary {
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;

    public MusicLibrary() {
        songs = new ArrayList<Song>();
        playlists = new ArrayList<Playlist>();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void createPlaylist(String name) {
        playlists.add(new Playlist(name));
    }

    public Playlist getPlaylistAt(int index) throws InvalidInputException {
        if (index < 0 || index >= playlists.size()) {
            throw new InvalidInputException("Choose a valid playlist.");
        }

        return playlists.get(index);
    }

    public void addSongToPlaylist(int songIndex, int playlistIndex) throws InvalidInputException {
        if (songIndex < 0 || songIndex >= songs.size()) {
            throw new InvalidInputException("Choose a valid song.");
        }

        Playlist playlist = getPlaylistAt(playlistIndex);
        playlist.addSong(songs.get(songIndex));
    }

    public ArrayList<Song> search(String keyword) {
        ArrayList<Song> results = new ArrayList<Song>();
        recursiveSearch(keyword.toLowerCase(), 0, results);
        return results;
    }

    /*
     * Recursion:
     * This method searches one song, then calls itself to check the next song.
     * It stops when index reaches the end of the song list.
     */
    private void recursiveSearch(String keyword, int index, ArrayList<Song> results) {
        if (index >= songs.size()) {
            return;
        }

        Song currentSong = songs.get(index);
        String title = currentSong.getTitle().toLowerCase();
        String artist = currentSong.getArtist().toLowerCase();

        // Conditional statement for Strings using contains().
        if (title.contains(keyword) || artist.contains(keyword)) {
            results.add(currentSong);
        }

        recursiveSearch(keyword, index + 1, results);
    }
}

/*
 * Custom exception class for validation errors.
 */
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

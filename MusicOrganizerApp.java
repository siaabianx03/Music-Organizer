import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
/* Music Organizer GUI
 * Lessons: variables, methods, classes/objects, encapsulation, inheritance,
 * abstraction, recursion, string conditions, and exception handling.
 */
public class MusicOrganizerApp extends JFrame {
    static final Color BLUE = new Color(30, 144, 255), BLACK = new Color(18, 18, 18);
    static final Color PANEL = new Color(24, 24, 24), FIELD = new Color(40, 40, 40);
    static final Color WHITE = Color.WHITE, GRAY = new Color(179, 179, 179);
    MusicLibrary library = new MusicLibrary();
    JTextField title = new JTextField(), artist = new JTextField(), genre = new JTextField();
    JTextField playlist = new JTextField(), search = new JTextField();
    JComboBox<String> songBox = new JComboBox<String>(), playlistBox = new JComboBox<String>();
    JComboBox<String> removeBox = new JComboBox<String>();
    DefaultListModel<String> songList = new DefaultListModel<String>();
    DefaultListModel<String> playlistList = new DefaultListModel<String>();
    DefaultListModel<String> searchList = new DefaultListModel<String>();
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicOrganizerApp().setVisible(true));
    }
    public MusicOrganizerApp() {
        library.createPlaylist("Favorites");
        setTitle("Music Organizer");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BLACK);
        add(topPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);
        add(bottomPanel(), BorderLayout.SOUTH);
        refresh();
    }
    JPanel topPanel() {
        JPanel p = panel(new GridLayout(3, 1, 5, 5), "Music and Playlist Controls");
        JButton add = button("Add Music"), make = button("Create Playlist");
        JButton addTo = button("Add To Playlist"), remove = button("Remove From Playlist");
        p.add(row(label("Title:"), field(title), label("Artist:"), field(artist), label("Genre:"), field(genre), add));
        p.add(row(label("Playlist Name:"), field(playlist), make, label("")));
        p.add(row(combo(songBox), combo(playlistBox), addTo, combo(removeBox), remove));
        add.addActionListener(e -> addSong());
        make.addActionListener(e -> addPlaylist());
        addTo.addActionListener(e -> addToPlaylist());
        remove.addActionListener(e -> removeSong());
        playlistBox.addActionListener(e -> refreshRemoveBox());
        return p;
    }
    JPanel centerPanel() {
        JPanel p = panel(new GridLayout(1, 2, 10, 10), null);
        p.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        p.add(scroll(list(songList), "All Songs"));
        p.add(scroll(list(playlistList), "Playlists"));
        return p;
    }
    JPanel bottomPanel() {
        JPanel p = panel(new BorderLayout(5, 5), "Search Song");
        JButton go = button("Search");
        p.add(row(label("Title or Artist:"), field(search), go), BorderLayout.NORTH);
        p.add(scroll(list(searchList), "Search Results"), BorderLayout.CENTER);
        go.addActionListener(e -> searchSongs());
        return p;
    }
    void addSong() {
        try {
            library.addSong(new Song(required(title, "Song title"), required(artist, "Artist"), required(genre, "Genre")));
            clear(title, artist, genre);
            refresh();
            msg("Song added successfully!");
        } catch (InvalidInputException e) { err(e.getMessage()); }
    }
    void addPlaylist() {
        try {
            library.createPlaylist(required(playlist, "Playlist name"));
            clear(playlist);
            refresh();
            msg("Playlist created successfully!");
        } catch (InvalidInputException e) { err(e.getMessage()); }
    }
    void addToPlaylist() {
        try {
            library.addSongToPlaylist(songBox.getSelectedIndex(), playlistBox.getSelectedIndex());
            refresh();
            msg("Song added to playlist!");
        } catch (InvalidInputException e) { err(e.getMessage()); }
    }
    void removeSong() {
        try {
            library.getPlaylistAt(playlistBox.getSelectedIndex()).removeSong(removeBox.getSelectedIndex());
            refresh();
            msg("Song removed from playlist!");
        } catch (InvalidInputException e) { err(e.getMessage()); }
    }
    void searchSongs() {
        try {
            searchList.clear();
            ArrayList<Song> found = library.search(required(search, "Search keyword"));
            if (found.isEmpty()) searchList.addElement("No matching songs found.");
            for (Song s : found) searchList.addElement(s.display());
        } catch (InvalidInputException e) { err(e.getMessage()); }
    }
    void refresh() {
        songList.clear(); playlistList.clear(); songBox.removeAllItems(); playlistBox.removeAllItems();
        if (library.getSongs().isEmpty()) songList.addElement("No songs added yet.");
        for (int i = 0; i < library.getSongs().size(); i++) {
            Song s = library.getSongs().get(i);
            songList.addElement((i + 1) + ". " + s.display());
            songBox.addItem(s.display());
        }
        for (int i = 0; i < library.getPlaylists().size(); i++) {
            Playlist p = library.getPlaylists().get(i);
            playlistBox.addItem(p.getName());
            playlistList.addElement((i + 1) + ". " + p.getName() + " (" + p.getSongs().size() + " song/s)");
            for (int j = 0; j < p.getSongs().size(); j++) playlistList.addElement("   " + (j + 1) + ". " + p.getSongs().get(j).display());
        }
        refreshRemoveBox();
    }
    void refreshRemoveBox() {
        removeBox.removeAllItems();
        int i = playlistBox.getSelectedIndex();
        if (i < 0 || i >= library.getPlaylists().size()) return;
        for (Song s : library.getPlaylists().get(i).getSongs()) removeBox.addItem(s.display());
    }
    String required(JTextField f, String name) throws InvalidInputException {
        if (f.getText().trim().isEmpty()) throw new InvalidInputException(name + " cannot be blank.");
        return f.getText().trim();
    }
    void clear(JTextField... fields) { for (JTextField f : fields) f.setText(""); }
    JPanel row(Component... c) { JPanel p = new JPanel(new GridLayout(1, c.length, 5, 5)); p.setBackground(PANEL); for (Component x : c) p.add(x); return p; }
    JPanel panel(LayoutManager l, String t) { JPanel p = new JPanel(l); p.setBackground(t == null ? BLACK : PANEL); if (t != null) p.setBorder(border(t)); return p; }
    JLabel label(String t) { JLabel l = new JLabel(t); l.setForeground(WHITE); return l; }
    JTextField field(JTextField f) { f.setBackground(FIELD); f.setForeground(WHITE); f.setCaretColor(WHITE); f.setBorder(BorderFactory.createLineBorder(GRAY)); return f; }
    JButton button(String t) { JButton b = new JButton(t); b.setBackground(BLUE); b.setForeground(BLACK); b.setFocusPainted(false); b.setBorder(BorderFactory.createLineBorder(BLUE)); return b; }
    JComboBox<String> combo(JComboBox<String> b) { b.setBackground(FIELD); b.setForeground(WHITE); return b; }
    JList<String> list(DefaultListModel<String> m) { JList<String> l = new JList<String>(m); l.setBackground(FIELD); l.setForeground(WHITE); l.setSelectionBackground(BLUE); return l; }
    JScrollPane scroll(JList<String> l, String t) { JScrollPane s = new JScrollPane(l); s.setBorder(border(t)); return s; }
    TitledBorder border(String t) { TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GRAY), t); b.setTitleColor(WHITE); return b; }
    void msg(String t) { JOptionPane.showMessageDialog(this, t); }
    void err(String t) { JOptionPane.showMessageDialog(this, t, "Input Error", JOptionPane.ERROR_MESSAGE); }
}
// Abstraction: parent class. Encapsulation: title is private.
abstract class MusicItem {
    private String title;
    public MusicItem(String title) { this.title = title; }
    public String getTitle() { return title; }
    public abstract String display();
}
// Inheritance: Song extends MusicItem.
class Song extends MusicItem {
    private String artist, genre;
    public Song(String title, String artist, String genre) { super(title); this.artist = artist; this.genre = genre; }
    public String getArtist() { return artist; }
    public String display() { return getTitle() + " by " + artist + " [" + genre + "]"; }
}
class Playlist {
    private String name;
    private ArrayList<Song> songs = new ArrayList<Song>();
    public Playlist(String name) { this.name = name; }
    public String getName() { return name; }
    public ArrayList<Song> getSongs() { return songs; }
    public void addSong(Song song) { songs.add(song); }
    public void removeSong(int i) throws InvalidInputException {
        if (i < 0 || i >= songs.size()) throw new InvalidInputException("Choose a valid song.");
        songs.remove(i);
    }
}
class MusicLibrary {
    private ArrayList<Song> songs = new ArrayList<Song>();
    private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    public ArrayList<Song> getSongs() { return songs; }
    public ArrayList<Playlist> getPlaylists() { return playlists; }
    public void addSong(Song song) { songs.add(song); }
    public void createPlaylist(String name) { playlists.add(new Playlist(name)); }
    public Playlist getPlaylistAt(int i) throws InvalidInputException {
        if (i < 0 || i >= playlists.size()) throw new InvalidInputException("Choose a valid playlist.");
        return playlists.get(i);
    }
    public void addSongToPlaylist(int song, int playlist) throws InvalidInputException {
        if (song < 0 || song >= songs.size()) throw new InvalidInputException("Choose a valid song.");
        getPlaylistAt(playlist).addSong(songs.get(song));
    }
    public ArrayList<Song> search(String keyword) {
        ArrayList<Song> found = new ArrayList<Song>();
        recursiveSearch(keyword.toLowerCase(), 0, found);
        return found;
    }
    // Recursion plus string condition using contains().
    private void recursiveSearch(String key, int i, ArrayList<Song> found) {
        if (i >= songs.size()) return;
        Song s = songs.get(i);
        if (s.getTitle().toLowerCase().contains(key) || s.getArtist().toLowerCase().contains(key)) found.add(s);
        recursiveSearch(key, i + 1, found);
    }
}
class InvalidInputException extends Exception {
    public InvalidInputException(String message) { super(message); }
}

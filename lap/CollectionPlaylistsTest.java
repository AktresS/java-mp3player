package lap;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CollectionPlaylistsTest {
    static CollectionPlaylists playlists;

    @BeforeEach
    public void prepareData(){
        playlists = new CollectionPlaylists();
        playlists.CreatePlaylist("Playlist1");
        playlists.CreatePlaylist("Playlist2");
        playlists.CreatePlaylist("Playlist3");
        playlists.CreatePlaylist("Playlist4");
    }

    @org.junit.jupiter.api.Test
    void testGetPlaylistByIdSizeNullMethod() {
        int last = playlists.getPlaylists().size();

        //не можем получить плейлист так как индекс выходит за пределы коллекции плейлистов, поэтому null
        assertNull(playlists.getPlaylistById(last));
        //можем получить плейлист так как индекс входит в пределы коллекции плейлистов, поэтому значение не может быть null
        assertNotNull(playlists.getPlaylistById(last-1));
        //можем получить плейлист так как индекс входит в пределы коллекции плейлистов
        assertInstanceOf(Playlist.class, playlists.getPlaylistById(0)); //используется для проверки того, что данный объект является экземпляром ожидаемого типа класса
        //можем получить плейлист так как индекс входит в пределы коллекции плейлистов
        assertInstanceOf(Playlist.class, playlists.getPlaylistById(last-1));
    }

    @org.junit.jupiter.api.Test
    void getPlaylistByTitle() {
        int last = playlists.getPlaylists().size();

        //можем получить плейлист, так как название существует в коллекции
        assertInstanceOf(Playlist.class, playlists.getPlaylistByTitle(playlists.getTitle(0)));
        //можем получить плейлист, так как название существует в коллекции
        assertInstanceOf(Playlist.class, playlists.getPlaylistByTitle(playlists.getTitle(last-1)));
        //не можем получить плейлист, так как название не существует, поэтому null
        assertNull(playlists.getPlaylistByTitle("Meow"));
    }

    @org.junit.jupiter.api.Test
    void createPlaylist() {
        String titleExists = "Playlist1"; //плейлист с таким названием был создан ранее
        String titleNotExists = "Playlist5"; //такого плейлиста не существует

        //не можем создать плейлист, так как плейлист с таким названием уже существует, поэтому false
        assertFalse(playlists.CreatePlaylist(titleExists));
        //можем создать плейлист, так как плейлиста с таким названием не существует, поэтому true
        assertTrue(playlists.CreatePlaylist(titleNotExists));
        //не можем создать плейлист, так как плейлист с таким названием уже существует(был создан выше), поэтому false
        assertFalse(playlists.CreatePlaylist(titleNotExists));
    }

    @org.junit.jupiter.api.Test
    void removePlaylistById() {
        int last = playlists.getPlaylists().size();

        //не можем удалить плейлист с индексом ноль, поэтому false
        assertFalse(playlists.removePlaylistById(0));
        //не можем удалить плейлист, так как индекс выходит за пределы длины
        assertFalse(playlists.removePlaylistById(last));
        //можем удалить плейлист с этим индексом
        assertTrue(playlists.removePlaylistById(last-1));
    }

    @org.junit.jupiter.api.Test
    void removePlaylistByTitle() {
        int last = playlists.getPlaylists().size();

        //можем удалить плейлист так как плейлист с таким названием существует
        assertTrue(playlists.removePlaylistByTitle(playlists.getTitle(last-1)));
        //не можем удалить плейлист, потому что я так хочу
        assertFalse(playlists.removePlaylistByTitle(playlists.getTitle(0)));
        //не можем удалить плейлист так как его не существует
        assertFalse(playlists.removePlaylistByTitle("meow"));
    }

    @org.junit.jupiter.api.Test
    void addSong() {
        int last = playlists.getPlaylists().size();

        //не можем добавить трек, потому что плейлиста с таким индексом не существует
        assertFalse(playlists.addSong(-1, "Mur", "Meow", "2:45"));
        //не можем добавить трек, потому что плейлиста с таким индексом не существует
        assertFalse(playlists.addSong(last,"Mur", "Meow", "2:45"));
        //можем добавить трек, так как плейлист существует
        assertTrue(playlists.addSong(0,"Mur", "Meow", "2:45"));
        //можем добавить трек, так как плейлист существует
        assertTrue(playlists.addSong(last-1,"Mur", "Meow", "2:45"));
    }
}
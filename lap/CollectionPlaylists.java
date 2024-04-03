package lap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CollectionPlaylists {
    private final ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private Playlist currentPlaylist;
    private Song currentTrack;
    private int currentPlaylistIndex;
    private int currentTrackIndex;

    public CollectionPlaylists(){
        /*loadPlaylist();*/
    }
    public Song getCurrentTrack(){
        return currentTrack;
    }
    public void setCurrentPlaylistIndex(int currentPlaylistIndex){
        this.currentPlaylistIndex = currentPlaylistIndex;
    }

    public int getCurrentPlaylistIndex(){
        return currentPlaylistIndex;
    }

    public ArrayList<Playlist> getPlaylists(){
        return playlists;
    }

    public String getTitle(int index){
        return playlists.get(index).getTitle();
    }

    public Playlist getPlaylistById(int index){
        if (index < 0 || playlists.size()<index){
            return null;
        }
        return playlists.get(index);
    }

    public Playlist getPlaylistByTitle(String title){
        for (Playlist playlist : playlists){
            if (playlist.getTitle().equals(title)){
                return playlist;
            }
        }
        return null;
    }

    public Playlist getCurrentPlaylist(){
        return playlists.get(currentPlaylistIndex);
    }

    //Создать плейлист
    public void CreatePlaylist(String title){
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
    }

    //Включить плейлист
    public void PlayPlaylist(String title, CollectionPlaylists playlists){
        int playlistIndex;
        try{
            playlistIndex = Integer.parseInt(title);
            Playlist selectedPlaylist = playlists.getPlaylistById(playlistIndex);
            if (selectedPlaylist == null){
                System.out.println("Плейлист не найден.");
                return;
            }
            currentPlaylist = selectedPlaylist;
            currentTrackIndex = 0;

            playCurrentTrack();
        } catch (NumberFormatException e){
            Playlist selectedPlaylist = playlists.getPlaylistByTitle(title);
            if (selectedPlaylist == null){
                System.out.println("Плейлист не найден.");
                return;
            }
            currentPlaylist = selectedPlaylist;
            currentTrackIndex = 0;
            playCurrentTrack();
        }
    }
    //Включить текущий трек
    public void playCurrentTrack(){
        Song currentTrack = currentPlaylist.getPlaylist().get(currentTrackIndex);
        System.out.println("Плейлист: " + currentPlaylist.getTitle());
        System.out.println("Играет: " + currentTrack.getTitle() + ".");
    }

    //Удалить плейлист по номеру
    public void removePlaylistById(int index){
        if (index >= 1 && playlists.size()>index){
            playlists.remove(index);
        }
    }

    //Удалить плейлист по названию
    public void removePlaylistByTitle(String title){
        for (int i = 0; i < playlists.size(); i++){
            if (playlists.get(i).getTitle().equals(title)){
                playlists.remove(i);
            }
        }
    }

    //Вывести плейлисты
    public void viewPlaylists(){
        for (int i = 0; i < playlists.size(); i++){
            System.out.println(i + ". " + playlists.get(i).getTitle());
        }
        System.out.println();
    }

    //Вывести плейлист и трек
    public void viewPlaylistAndSong(){
        for (int i = 0; i < playlists.size(); i++){
            System.out.println(i + ". " + playlists.get(i).getTitle());
            for (Song song : playlists.get(i).getPlaylist()){
                System.out.println("\t" + song.getArtist() + " - " + song.getTitle() + ": " + song.getLength());
            }
        }
    }

    //Включить предыдущий трек
    public void playPreviousTrack(){
        if (currentPlaylist != null && currentTrackIndex > 0){
            currentTrackIndex--;
            playCurrentTrack();
        } else{
            System.out.println("Предыдущий трек не может быть включен!");
        }
    }

    // Включить следующий трек
    public void playNextTrack(){
        if (currentPlaylist != null && currentTrackIndex < (currentPlaylist.getPlaylist().size() - 1)){
            currentTrackIndex++;
            playCurrentTrack();
        } else{
            System.out.println("Следующий трек не может быть включен!");
        }
    }

    // Повторить текущий трек
    public void repeatCurrentTrack(){
        if (currentPlaylist != null){
            playCurrentTrack();
        } else{
            System.out.println("Текущий трек не может быть включен");
        }
    }

    //Добавить плейлист в плеер
    public void addPlaylist(Playlist currentPlaylist){
        playlists.add(currentPlaylist);
    }

    //Добавить трек в текущий плейлист
    public void addSong(int index){
        Scanner scan = new Scanner(System.in);
        Song newSong = new Song(null, null, null);

        System.out.println("Введите имя исполнителя: ");
        newSong.setArtist(scan.nextLine());
        System.out.println("Введите название трека: ");
        newSong.setTitle(scan.nextLine());
        System.out.println("Введите длину трека: ");
        newSong.setLength(scan.nextLine());

       playlists.get(index).addSong(newSong);
    }

    public void loadPlaylist(){
        try{
            File file = new File("saved.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String playlistTitle = scanner.nextLine();
                Playlist newPlaylist = new Playlist(playlistTitle);
                System.out.println(playlistTitle);
                ArrayList<Playlist> playlists1 = new ArrayList<Playlist>();

                while (scanner.hasNextLine()){
                    String song = scanner.nextLine();

                    if (song.isEmpty()){
                        break;
                    }

                    String[] info = song.split(" ");
                    String songArtist = info[0];
                    String songTitle = info[1];
                    String songLength = info[2];

                    System.out.println(songTitle + " - " + songArtist + " : " + songLength);
                    Song newSong = new Song(songArtist, songTitle, songLength);
                    newPlaylist.addSong(newSong);


                }
                System.out.println();
                playlists.add(newPlaylist);
            }

        } catch (IOException e){
            System.out.println("Ошибка: " + e);
        }

    }
}

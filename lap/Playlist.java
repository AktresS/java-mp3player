package lap;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {
    private String title;
    private ArrayList<Song> playlist;

    public Playlist(String title){
        this.title = title;
        this.playlist = new ArrayList<Song>();
    }

    //Добавить трек в плейлист
    public void addSong(Song song){
        playlist.add(song);
    }

    //
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setPlaylist(ArrayList<Song> playlist){
        this.playlist = playlist;
    }

    public ArrayList<Song> getPlaylist(){
        return playlist;
    }

    public int getSizePlaylist(){
        return playlist.size();
    }

    public String getArtist(int index){
        return playlist.get(index).getArtist();
    }

    public String getTitle(int index){
        return playlist.get(index).getTitle();
    }
     public String getLength(int index){
        return playlist.get(index).getLength();
     }

    //Убрать трек по номеру
    public void removeSongI(int index){
        playlist.remove(index);
    }

    //Убрать трек по названию
    public void removeSongT(String title){
        for(int i = 0; i < playlist.size(); i++) {
            if (getTitle(i).equals(title)) {
                playlist.remove(i);
            }
        }
    }

    //Показать весь плейлист
    public void viewPlaylist(){
        if (playlist.isEmpty()){
            System.out.println("Плейлист пуст!");
        }
        else{
            for (int i = 0; i < playlist.size(); i++){
                System.out.println(getArtist(i) + " - " + getTitle(i) + ": " + getLength(i));
            }
        }
    }

    //Сохранить плейлист
    public void savedPlaylist() {
        try{
            File file = new File("saved.txt");
            file.createNewFile();

            PrintWriter pw = new PrintWriter(file);

            pw.println(title);
            for (int i = 0; i < playlist.size(); i++){
                pw.println(getArtist(i) + " " + getTitle(i) + " " + getLength(i));
            }

            pw.close();
        } catch (IOException e){
            System.out.println("Ошибка: " + e);
        }
    }

    //Загрузить плейлист
/*    public Playlist loadPlaylist(){
        try{
            File file = new File("saved.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String playlistTitle = scanner.nextLine();
                Playlist newPlaylist = new Playlist(playlistTitle);

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
                    return newPlaylist;
                }
            }

        } catch (IOException e){
            System.out.println("Ошибка: " + e);
        }
        return null;
    }*/


}

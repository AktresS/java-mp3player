package lap;
import java.io.*;
import java.util.Scanner;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String titlePlaylist;
        int selection;
        int index;

        CollectionPlaylists playlists = new CollectionPlaylists();
        Playlist currentPlaylist = new Playlist("Мой плейлист");
        playlists.addPlaylist(currentPlaylist);

        boolean flag = false;
        while(!flag){
            System.out.println("========== Menu ==========");
            System.out.println("0 - Выйти");
            System.out.println("1 - Показать список песен");
            System.out.println("2 - Создать плейлист");
            System.out.println("3 - Включить плейлист (по названию)");
            System.out.println("4 - Включить предыдущий трек");
            System.out.println("5 - Включить следующий трек");
            System.out.println("6 - Повторить текущий трек");
            System.out.println("7 - Сохранить плейлист");
            System.out.println("8 - Удалить плейлист");
            System.out.println("9 - Добавить песню в плейлист");
            System.out.println("10 - Показать список плейлистов и треков");
            System.out.println("11 - Удалить песню из плейлиста");
            System.out.println("12 - Загрузка из файла");
            System.out.println("13 - Показать список плейлистов");

            selection = scan.nextInt();
            switch (selection){
                case 0:
                    flag = true;
                    System.out.println("Катись!");
                    break;
                case 1:
                    currentPlaylist.viewPlaylist();
                    break;
                case 2:
                    System.out.print("Введите название плейлиста: ");
                    scan.nextLine();
                    titlePlaylist = scan.nextLine();
                    playlists.CreatePlaylist(titlePlaylist);
                    break;
                case 3:
                    System.out.print("Введите название плейлиста: ");
                    scan.nextLine();
                    titlePlaylist = scan.nextLine();
                    playlists.PlayPlaylist(titlePlaylist , playlists);
                    break;
                case 4:
                    playlists.playPreviousTrack();
                    break;
                case 5:
                    playlists.playNextTrack();
                    break;
                case 6:
                    playlists.repeatCurrentTrack();
                    break;
                case 7:
                    System.out.print("Введите название плейлиста: ");
                    scan.nextLine();
                    titlePlaylist = scan.nextLine();
                    playlists.getPlaylistByTitle(titlePlaylist).savedPlaylist();
                    break;
                case 8:
                    System.out.print("Введите название плейлиста: ");
                    scan.nextLine();
                    titlePlaylist = scan.nextLine();
                    playlists.removePlaylistByTitle(titlePlaylist);
                    break;
                case 9:
                    System.out.print("Введите номер плейлиста: ");
                    scan.nextLine();
                    index = scan.nextInt();
                    playlists.addSong(index);
                    break;
                case 10:
                    playlists.viewPlaylistAndSong();
                    break;
                case 11:
                    System.out.print("Введите название плейлиста: ");
                    scan.nextLine();
                    titlePlaylist = scan.nextLine();
                    System.out.print("Введите номер трека: ");
                    index = scan.nextInt();
                    playlists.getPlaylistByTitle(titlePlaylist).removeSongI(index);
                    break;
                case 12:
                    playlists.loadPlaylist();
                    break;
                case 13:
                    playlists.viewPlaylists();
                    break;
            }
        }

    }
}

/*//Создать плейлист
        playlists.CreatePlaylist("MIIIUUU");
        Song song = playlists.getCurrentTrack();
        //Добавить трек в плейлист
        playlists.addSong(0);
        //Показать список песен
        currentPlaylist.viewPlaylist();
        //Загрузка из файла
        playlists.loadPlaylist();
        //Показать список плейлистов
        playlists.viewPlaylists();

        //Включить плейлист
        System.out.print("Введите название плейлиста: ");
        titlePlaylist = scan.nextLine();
            //Первый трек
        playlists.PlayPlaylist(titlePlaylist , playlists);
        //Следующий трек
        playlists.playNextTrack(titlePlaylist, playlists);
        //Предыдущий трек
        playlists.playPreviousTrack(titlePlaylist, playlists);
        //Следующий трек
        playlists.playNextTrack(titlePlaylist, playlists);
        //Повторить трек
        playlists.repeatCurrentTrack(titlePlaylist, playlists);

        //Показать список плейлистов
        playlists.viewPlaylists();

        //Удалить плейлист
        playlists.removePlaylistById(3);
        //Показать список плейлистов
        playlists.viewPlaylists();


        //Показать плейлист и треки в нем
        playlists.viewPlaylistAndSong();
        //Удалить песню из плейлиста
        playlists.getPlaylistByTitle(titlePlaylist).removeSongI(1);
        playlists.viewPlaylistAndSong(); */
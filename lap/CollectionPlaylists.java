package lap;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CollectionPlaylists extends PlaybackListener {
    private  static final Object playSignal = new Object();
    private MusicPlayerGUI musicPlayerGUI;
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private Playlist currentPlaylist;
    private Song currentTrack;
    public Song getCurrentSong(){
        return currentTrack;
    }
    private int currentPlaylistIndex;
    private int currentTrackIndex;

    private ArrayList<Song> playlist;
    private AdvancedPlayer advancedPlayer;
    private boolean isPaused;
    private boolean songFinished;
    private boolean pressedNext, pressedPrev;
    private int currentFrame;

    public  void setCurrentFrame(int frame){
        currentFrame = frame;
    }
    private  int currentTimeInMilli;
    public void setCurrentTimeInMilli(int timeInMilli){
        currentTimeInMilli = timeInMilli;
    }
    public CollectionPlaylists(){
    }

    public CollectionPlaylists(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
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
        if (index < 0 || playlists.size()<=index){
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

    public void loadSong(Song song){
        currentTrack = song;
        playlist = null;

        if(!songFinished)
            stopSong();

        if(currentTrack != null){
            currentFrame = 0;

            currentTimeInMilli = 0;

            musicPlayerGUI.setPlaybackSliderValue(0);
            playCurrentSong();
        }
    }

    public void loadPlaylist(File playlistFile){
        playlist = new ArrayList<>();

        try{
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String songPath;
            while((songPath = bufferedReader.readLine()) != null){
                Song song = new Song(songPath);

                playlist.add(song);
            }

            bufferedReader.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(playlist.size() > 0){
            musicPlayerGUI.setPlaybackSliderValue(0);
            currentTimeInMilli = 0;

            currentTrack = playlist.get(0);

            currentFrame = 0;

            musicPlayerGUI.enablePauseButtonDisablePlayButton();
            musicPlayerGUI.updateSongTitleAndArtist(currentTrack);
            musicPlayerGUI.updatePlaybackSlider(currentTrack);

            playCurrentSong();
        }
    }

    public  void pauseSong(){
        if (advancedPlayer != null){
            isPaused = true;

            stopSong();
        }
    }

    public void stopSong(){
        if (advancedPlayer != null){
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void nextSong(){
        if(playlist == null) return;

        pressedNext = true;

        if(!songFinished)
            stopSong();

        if(currentPlaylistIndex + 1 > playlist.size() - 1) {
            currentPlaylistIndex = 0;
        } else {
            currentPlaylistIndex++;
        };

        currentTrack = playlist.get(currentPlaylistIndex);

        currentFrame = 0;

        currentTimeInMilli = 0;

        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentTrack);
        musicPlayerGUI.updatePlaybackSlider(currentTrack);

        playCurrentSong();
    }

    public void prevSong(){
        if(playlist == null) return;

        if(currentPlaylistIndex - 1 < 0) return;
        pressedPrev = true;

        if(!songFinished)
            stopSong();

        currentPlaylistIndex--;
        currentTrack = playlist.get(currentPlaylistIndex);

        currentFrame = 0;

        currentTimeInMilli = 0;

        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentTrack);
        musicPlayerGUI.updatePlaybackSlider(currentTrack);

        playCurrentSong();
    }

    public void playCurrentSong(){
        if(currentTrack == null) return;
        try{
            FileInputStream fileInputStream = new FileInputStream(currentTrack.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);

            startMusicThread();

            startPlaybackSliderThread();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private  void startMusicThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isPaused){
                        synchronized (playSignal){
                            isPaused = false;
                            playSignal.notify();
                        }
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    }else{
                        advancedPlayer.play();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startPlaybackSliderThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isPaused){
                    try{
                        synchronized (playSignal){
                            playSignal.wait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                while (!isPaused && !songFinished && !pressedNext && !pressedPrev){
                    try {
                        currentTimeInMilli++;

                        int calculatedFrame = (int) ((double) currentTimeInMilli * 1.27 * currentTrack.getFrameRatePerMilliseconds() );

                        musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                        Thread.sleep(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt){
        System.out.println("Playback Started");
        songFinished = false;
        pressedNext = false;
        pressedPrev = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt){
        System.out.println("Playback Finished");
        if(isPaused){
            currentFrame += (int) ((double)evt.getFrame() * currentTrack.getFrameRatePerMilliseconds());
        } else{
            if(pressedNext || pressedPrev) return;
            songFinished = true;

            if(playlist == null){
                musicPlayerGUI.enablePlayButtonDisablePauseButton();
            }else{
                if(currentPlaylistIndex == playlist.size() - 1){
                    musicPlayerGUI.enablePlayButtonDisablePauseButton();
                }else{
                    nextSong();
                }
            }
        }
    }

    //Создать плейлист
    public boolean CreatePlaylist(String title){
        for (Playlist playlist : playlists){
            if (playlist.getTitle().equals(title)){
                return false;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        return true;
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
    public boolean removePlaylistById(int index){
        if (index < 1 || playlists.size() <= index){
            return false;
        }
        playlists.remove(index);
        return true;
    }

    //Удалить плейлист по названию
    public boolean removePlaylistByTitle(String title){
        for (int i = 1; i < playlists.size(); i++){
            if (playlists.get(i).getTitle().equals(title)){
                playlists.remove(i);
                return true;
            }
        }
        return false;
    }
    public void removeFromPlaylist(Integer pIndex, Integer tIndex) {
        for (Integer i = 1; i < playlists.size(); i++) {
            if (i.equals(pIndex)) {
                if (tIndex < playlists.get(i).getPlaylist().size())
                    playlists.get(i).deleteSong(playlists.get(i).getTrackAtIndex(tIndex - 1));
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
    public boolean playPreviousTrack(){
        if (currentPlaylist != null && currentTrackIndex > 0){
            currentTrackIndex--;
            playCurrentTrack();
            return true;
        } else{
            System.out.println("Предыдущий трек не может быть включен!");
            return false;
        }
    }

    // Включить следующий трек
    public boolean playNextTrack(){
        if (currentPlaylist != null && currentTrackIndex < (currentPlaylist.getPlaylist().size() - 1)){
            currentTrackIndex++;
            playCurrentTrack();
            return true;
        } else{
            System.out.println("Следующий трек не может быть включен!");
            return false;
        }
    }

    // Повторить текущий трек
    public boolean repeatCurrentTrack(){
        if (currentPlaylist != null){
            playCurrentTrack();
            return true;
        } else{
            System.out.println("Текущий трек не может быть включен");
            return false;
        }
    }

    //Добавить плейлист в плеер
    public void addPlaylist(Playlist currentPlaylist){
        playlists.add(currentPlaylist);
    }

    //Добавить трек плейлист
    public boolean addSong(int index, String artist, String track, String lenght){
        if (index < 0 || playlists.size() <= index) {
            return false;
        }
        Scanner scan = new Scanner(System.in);
        Song newSong = new Song(null, null, null);

        newSong.setArtist(artist);
        newSong.setTitle(track);
        newSong.setLength(lenght);

        playlists.get(index).addSong(newSong);
        return true;
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

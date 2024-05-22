package lap;


import com.mpatric.mp3agic.Mp3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;


import javax.naming.ldap.ExtendedRequest;
import java.io.File;

public class Song {
    private String artist;
    private String title;
    private String length;
    private String filePath;
    private Mp3File mp3File;
    private double frameRatePerMilliseconds;

    public  Song(String filePath){
        this.filePath = filePath;
        try {
            mp3File = new Mp3File(filePath);
            frameRatePerMilliseconds = (double) mp3File.getFrameCount() / mp3File.getLengthInMilliseconds();
            length = convertToSingLenghtFormat();

            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            Tag tag = audioFile.getTag();
            if (tag!= null){
                title = tag.getFirst(FieldKey.TITLE);
                artist = tag.getFirst(FieldKey.ARTIST);
            }else{
                title = "N/A";
                artist = "N/A";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String convertToSingLenghtFormat(){
        long minutes = mp3File.getLengthInSeconds() / 60;
        long seconds = mp3File.getLengthInSeconds() % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        return formattedTime;
    }

    private boolean isValidLengthFormat(String length){
        return length.matches("\\d{1,2}:\\d{2}");
    }
    public Song(String artist, String title, String length){
        this.artist = artist;
        this.title = title;
        this.length = length;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return artist;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setLength(String length){
        if (isValidLengthFormat(length)){
            this.length = length;
        }
        else{
            System.out.println("Некорректный формат длины трека. Используйте: минуты:секунды");
        }
    }

    public String getLength(){
        return length;
    }

    public String getFilePath() {
        return filePath;
    }

    public Mp3File getMp3File(){
        return mp3File;
    }

    public double getFrameRatePerMilliseconds(){ return  frameRatePerMilliseconds; }

}

package lap;

public class Song {
    private String artist;
    private String title;
    private String length;

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
        return this.artist;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
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
        return this.length;
    }

}

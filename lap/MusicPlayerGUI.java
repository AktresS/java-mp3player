package lap;
//Импортирует пакет javax.swing, который содержит классы
// для создания графического интерфейса в Java.

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//Объявляет публичный класс MusicPlayerGUI, который наследует от JFrame.
// JFrame является классом из Swing, представляющим основное окно приложения.
public class MusicPlayerGUI extends JFrame {

    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private CollectionPlaylists musicPlayer;

    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;


    //Объявляет конструктор класса MusicPlayerGUI.
    // Конструктор вызывается при создании объекта этого класса.
    public MusicPlayerGUI(){

        //Вызывает конструктор суперкласса JFrame с параметром "Music Player".
        // Это устанавливает заголовок окна.
        super("Music Player");

        //Устанавливает размер окна (ширина 500 пикселей, высота 700 пикселей).
        setSize(400, 600);

        //Устанавливает операцию по умолчанию при закрытии окна. В данном случае, это завершение программы.
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Центрирует окно относительно экрана. null указывает,
        // что окно будет центрировано относительно экрана, а не относительно другого компонента.
        setLocationRelativeTo(null);

        //Устанавливает возможность изменения размера окна.
        // В данном случае, окно будет фиксированного размера (нельзя изменять размеры окна).
        setResizable(false);

        //Устанавливает для окна компоновку null, что означает,
        // что вы сами будете управлять расположением компонентов, задавая их координаты вручную.
        setLayout(null);

        //установки цвета фона контейнера содержимого (content pane) в объекте JFrame.
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new CollectionPlaylists(this);
        jFileChooser = new JFileChooser();

        jFileChooser.setCurrentDirectory(new File("lap/assets"));

        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents(){
        //Вызывает другой метод addToolbar, который добавляет панель инструментов (toolbar) к интерфейсу.
        addToolbar();

        JLabel songImage = new JLabel(loadImage("lap/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider) e.getSource();

                int frame = source.getValue();

                musicPlayer.setCurrentFrame(frame);

                musicPlayer.setCurrentTimeInMilli((int) (frame / (1.27 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));

                musicPlayer.playCurrentSong();

                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        addPlaybackBtns();
    }

    private void addToolbar(){
        //Создает новый объект JToolBar, который является панелью инструментов для размещения кнопок и других компонентов.
        JToolBar toolBar = new JToolBar();
        //Устанавливает положение и размер панели инструментов.
        // Панель будет располагаться в верхней части окна (координаты x=0, y=0) и иметь ширину окна и высоту 20 пикселей.
        toolBar.setBounds(0,0, getWidth(), 20);
        //Отключает возможность перетаскивания панели инструментов. По умолчанию панели инструментов могут быть перемещены пользователем.
        toolBar.setFloatable(false);

        //Создает строку меню, которая будет добавлена на панель инструментов.
        JMenuBar menuBar = new JMenuBar();
        //Добавляет строку меню на панель инструментов.
        toolBar.add(menuBar);

        //Создает меню с заголовком "Song".
        JMenu songMenu = new JMenu("Song");
        //Добавляет меню "Song" в строку меню.
        menuBar.add(songMenu);

        //Создает пункт меню с заголовком "Load Song".
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    Song song = new Song(selectedFile.getPath());

                    musicPlayer.loadSong(song);

                    updateSongTitleAndArtist(song);

                    updatePlaybackSlider(song);

                    enablePauseButtonDisablePlayButton();
                }
            }
        });
        //Добавляет пункт меню "Load Song" в меню "Song".
        songMenu.add(loadSong);

        //Создает меню с заголовком "Playlist".
        JMenu playlistMenu = new JMenu("Playlist");
        //Добавляет меню "Playlist" в строку меню.
        menuBar.add(playlistMenu);

        //Создает пункт меню с заголовком "Create Playlist".
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MusicPlaylistDiolog(MusicPlayerGUI.this).setVisible(true);
            }
        });

        //Добавляет пункт меню "Create Playlist" в меню "Playlist".
        playlistMenu.add(createPlaylist);

        //Создает пункт меню с заголовком "Load Playlist".
        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");

        loadPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Playlist", "txt"));
                jFileChooser.setCurrentDirectory(new File("lap/assets"));

                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    musicPlayer.stopSong();

                    musicPlayer.loadPlaylist(selectedFile);
                }
            }
        });

        //Добавляет пункт меню "Load Playlist" в меню "Playlist".
        playlistMenu.add(loadPlaylist);

        JMenuItem viewPlaylist = new JMenuItem("View Playlist");
        viewPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MusicPlaylistDiolog2(MusicPlayerGUI.this).setVisible(true);
            }
        });
        playlistMenu.add(viewPlaylist);

        JMenuItem removeFromPlaylist = new JMenuItem("Remove From Playlist");
        removeFromPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInputNumberPlaylist = JOptionPane.showInputDialog("Number playlist", "1");
                String userInputNumberSong = JOptionPane.showInputDialog("Number song", "1");

                if (userInputNumberPlaylist != null && userInputNumberSong != null && userInputNumberPlaylist.matches("\\d+") && userInputNumberSong.matches("\\d+")) {
                    int playlistIndex = Integer.parseInt(userInputNumberPlaylist) - 1;
                    int songIndex = Integer.parseInt(userInputNumberSong) - 1;

                    // Assuming playlists are stored as files in a directory
                    File playlistDirectory = new File("lap/assets");
                    File[] playlistFiles = playlistDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

                    if (playlistFiles != null && playlistIndex >= 0 && playlistIndex < playlistFiles.length) {
                        File selectedPlaylist = playlistFiles[playlistIndex];

                        try {
                            // Read the playlist file
                            List<String> lines = new ArrayList<>();
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedPlaylist));

                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                lines.add(line);
                            }
                            bufferedReader.close();

                            // Check if the song index is valid
                            if (songIndex >= 0 && songIndex < lines.size()) {
                                // Remove the song from the list
                                lines.remove(songIndex);

                                // Write the updated list back to the file
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(selectedPlaylist));
                                for (String songPath : lines) {
                                    bufferedWriter.write(songPath);
                                    bufferedWriter.newLine();
                                }
                                bufferedWriter.close();

                                JOptionPane.showMessageDialog(null, "Successfully removed the song from the playlist!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid song number.");
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error processing the playlist.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid playlist number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Please enter valid numbers for the playlist and song.");
                }
            }
        });
        playlistMenu.add(removeFromPlaylist);

        JMenuItem deletePlaylist = new JMenuItem("Delete Playlist");
        deletePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInputNumberPlaylist = JOptionPane.showInputDialog("Enter the playlist number to delete", "1");

                if (userInputNumberPlaylist != null){
                    int playlistIndex = Integer.parseInt(userInputNumberPlaylist) - 1;

                    File playlistDirectory = new File("lap/assets");
                    File[] playlistFiles = playlistDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

                    if(playlistFiles != null && playlistIndex >= 0 && playlistIndex <= playlistFiles.length) {
                        for (int i = 0; i < playlistFiles.length; i++)
                            System.out.println(playlistFiles[i].getName());
                        System.out.println(playlistFiles[playlistIndex].delete());
                        JOptionPane.showMessageDialog(null, "Playlist successfully deleted");
                    }else {
                        JOptionPane.showMessageDialog(null, "Invalid playlist number.");
                    }
         /*               if (musicPlayer.removePlaylistById(playlistIndex)) {
                            JOptionPane.showMessageDialog(null, "Playlist successfully deleted");
                        } else {
                            System.out.println(playlistIndex);
                            System.out.println(musicPlayer.getPlaylists().size());
                            JOptionPane.showMessageDialog(null, "Invalid playlist number.");
                        }*/
                    }
                }

        });
        playlistMenu.add(deletePlaylist);

        //Добавляет панель инструментов (вместе со всеми добавленными на нее элементами) в основное окно приложения.
        add(toolBar);
    }

    private void addPlaybackBtns(){
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth()-10, 80);
        playbackBtns.setBackground(null);

        JButton prevButton = new JButton(loadImage("lap/assets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.prevSong();
            }
        });
        playbackBtns.add(prevButton);

        JButton playButton = new JButton(loadImage("lap/assets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePauseButtonDisablePlayButton();

                musicPlayer.playCurrentSong();
            }
        });
        playbackBtns.add(playButton);

        JButton pauseButton = new JButton(loadImage("lap/assets/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePlayButtonDisablePauseButton();

                musicPlayer.pauseSong();
            }
        });
        playbackBtns.add(pauseButton);

        JButton nextButton = new JButton(loadImage("lap/assets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.nextSong();
            }
        });
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song){
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
    }

    public void updatePlaybackSlider(Song song){
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        JLabel lavbelBeginning = new JLabel("00:00");
        lavbelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
        lavbelBeginning.setForeground(TEXT_COLOR);

        JLabel labelEnd = new JLabel(song.getLength());
        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
        labelEnd.setForeground(TEXT_COLOR);

        labelTable.put(0, lavbelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);
        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    public void enablePauseButtonDisablePlayButton(){
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(false);
        playButton.setEnabled(false);

        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton(){
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(true);
        playButton.setEnabled(true);

        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private  ImageIcon loadImage(String imagePath){
        try{
            BufferedImage image = ImageIO.read(new File(imagePath));

            return new ImageIcon(image);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

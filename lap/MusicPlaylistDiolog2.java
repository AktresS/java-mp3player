package lap;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class MusicPlaylistDiolog2 extends JDialog {
    private MusicPlayerGUI musicPlayerGUI;
    private ArrayList<String> songPath;
    public MusicPlaylistDiolog2(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
        songPath = new ArrayList<>();

        setTitle("Playlist");
        setSize(600,400);
        setResizable(false);
        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(this.musicPlayerGUI);

        addDiologComponents();
    }

    private void addDiologComponents(){
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
        songContainer.setBounds((int) (getWidth()*0.035), 10, (int)(getWidth()*0.90), (int) (getHeight()*0.75));
        add(songContainer);

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Playlist", "txt"));
        jFileChooser.setCurrentDirectory(new File("lap/assets"));

        int result = jFileChooser.showOpenDialog(this);
        File selectedFile = jFileChooser.getSelectedFile();

        if (result == JFileChooser.APPROVE_OPTION && selectedFile != null){
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    JLabel filePathLabel = new JLabel(line);
                    filePathLabel.setFont(new Font("Dialog", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    songPath.add(line);
                    songContainer.add(filePathLabel);
                }
                bufferedReader.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(180, (int)(getHeight()*0.80), 100, 25);
        addSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("lap/assets"));
                int result = jFileChooser.showOpenDialog(MusicPlaylistDiolog2.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Dialog", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    songPath.add(filePathLabel.getText());

                    songContainer.add(filePathLabel);

                    songContainer.revalidate();
                }


            }
        });
        add(addSongButton);

        JButton saveSongButton = new JButton("Save");
        saveSongButton.setBounds(335, (int)(getHeight()*0.80), 100, 25);
        saveSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
        saveSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(result == JFileChooser.APPROVE_OPTION){
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        for(String songPath : songPath){
                            bufferedWriter.write(songPath+"\n");
                        }
                        bufferedWriter.close();

                        JOptionPane.showMessageDialog(MusicPlaylistDiolog2.this, "Successfully Created Playlist!");

                        MusicPlaylistDiolog2.this.dispose();
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }

            }
        });
        add(saveSongButton);
    }
}


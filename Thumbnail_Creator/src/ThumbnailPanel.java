import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ThumbnailPanel extends JFrame{
	private JPanel panel = new JPanel();
	private ArrayList<File> fileList;
	public ThumbnailPanel(){
		initUI();
		fileList = new ArrayList<File>();
	}
	private void initUI(){
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JButton addFilesButton = new JButton("Add files to be convorted");
		panel.add(addFilesButton);
		
		JLabel converterLable = new JLabel("Converting the following files");
		panel.add(converterLable);
		
		final JTextArea fileBox = new JTextArea("");
		fileBox.setEditable(false);
		JScrollPane scroll = new JScrollPane(fileBox);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);
		
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
		JLabel sizeLable = new JLabel("Enter new size: ");
		sizePanel.add(sizeLable);
		
		JLabel widthLable = new JLabel("Width: ");
		sizePanel.add(widthLable);
		
		final JTextField widthBox = new JTextField(24);
		widthBox.setBackground(Color.WHITE);
		sizePanel.add(widthBox);
		
		JLabel heightLable = new JLabel("Height: ");
		sizePanel.add(heightLable);
		
		final JTextField heightBox = new JTextField(24);
		heightBox.setBackground(Color.WHITE);
		sizePanel.add(heightBox);
		
		panel.add(sizePanel);
		
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
		JLabel fileTypeLable = new JLabel("Enter the file type you want to create after converting images: ");
		typePanel.add(fileTypeLable);
		
		final JTextField fileTypeBox = 	new JTextField(24);
		fileTypeBox.setBackground(Color.WHITE);
		typePanel.add(fileTypeBox);
		
		panel.add(typePanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton convertButton = new JButton("Create Thumbnails!");
		buttonPanel.add(convertButton);
		JButton clearButton = new JButton("Clear the file list");
		buttonPanel.add(clearButton);
		
		panel.add(buttonPanel);
		
		addFilesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showOpenDialog(ThumbnailPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File files[] = fc.getSelectedFiles();
					for(int i=0;i<files.length;i++){
						File file= files[i];
						fileList.add(file);
						String fileName = file.getName();
						String currentText = fileBox.getText();
						if(currentText.length() !=0){
							fileBox.setText(currentText+", "+fileName);
						}else{
							fileBox.setText(fileName);
						}
					}
				}
			}
		});
		convertButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(widthBox.getText().length()==0|| heightBox.getText().length() == 0|| fileTypeBox.getText().length()==0){
					JOptionPane.showMessageDialog(null, "Either the width, height, or file type field is empty.");
					
				}else if(fileList.isEmpty()){
					JOptionPane.showMessageDialog(null, "Add some image to convert.");
				}else{
					int width = Integer.parseInt(widthBox.getText());
					int height = Integer.parseInt(heightBox.getText());
					String fileType = fileTypeBox.getText();
					File[] fileArr = new File[fileList.size()];
					fileArr = fileList.toArray(fileArr);
					ImageConverter imageConverter = new ImageConverter(fileArr, width, height, fileType);
					ConversionThread thread = new ConversionThread(imageConverter);
					thread.start();
				}
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileList.clear();
				fileBox.setText("");
			}
		});
		add(panel);
		pack();
		setTitle("Thumbnails Creator");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public class ConversionThread extends Thread{
		ImageConverter imageConverter;
		public ConversionThread(ImageConverter imageConverter){
			this.imageConverter = imageConverter;
		}
		public void run(){
			String result = imageConverter.convertImages();
			JOptionPane.showMessageDialog(null, result);
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}

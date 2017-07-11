package com.lionmobi;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Console {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Console window = new Console();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Console() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("翻译导入工具");

		JButton btnNewButton = new JButton("选择EXCEL");
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 22));
		
		btnNewButton.setBounds(21, 10, 200, 40);
		frame.getContentPane().add(btnNewButton);

		JButton btnRoot = new JButton("选择项目资源根目录");
		btnRoot.setFont(new Font("宋体", Font.PLAIN, 22));
		btnRoot.setBounds(21, 97, 235, 40);
		frame.getContentPane().add(btnRoot);

		JLabel excelPath = new JLabel("");
		excelPath.setBounds(21, 59, 392, 15);
		frame.getContentPane().add(excelPath);

		JLabel rootPath = new JLabel("");
		rootPath.setBounds(21, 147, 392, 15);
		frame.getContentPane().add(rootPath);

		JButton btnTranslate = new JButton("Translate");
		btnTranslate.setFont(new Font("宋体", Font.PLAIN, 20));
		btnTranslate.setBounds(163, 202, 143, 40);
		frame.getContentPane().add(btnTranslate);

		btnTranslate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String root = rootPath.getText();
				String excel = excelPath.getText();
				if("".equals(excel)){
					JOptionPane.showConfirmDialog(null, "EXCEL文件地址不能为空", "", JOptionPane.CLOSED_OPTION);
					return;
				}
				if("".equals(root)){
					JOptionPane.showConfirmDialog(null, "资源路径不能为空", "", JOptionPane.CLOSED_OPTION);
					return;
				}
				try {
					Main.runTask(excel, root);
					JOptionPane.showConfirmDialog(null, "操作完成", "", JOptionPane.CLOSED_OPTION);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "选择EXCEL文件");
				File file = jfc.getSelectedFile();
				excelPath.setText(file.getAbsolutePath());
			}
		});
		
		btnRoot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "选择项目资源根目录");
				File file = jfc.getSelectedFile();
				rootPath.setText(file.getAbsolutePath());	
			}
		});
	}
}

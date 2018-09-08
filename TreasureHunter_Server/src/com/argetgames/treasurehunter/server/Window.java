package com.argetgames.treasurehunter.server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultCaret;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.ScrollPaneConstants;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

public class Window extends JPanel {

	/**
	 * Create the panel.
	 */
	public JTextArea dataArea, connectionArea;
	public JList clients;
	private DefaultCaret caretData, caretConnection;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	
	
	public Window() {
		setSize(new Dimension(550, 450));
		setPreferredSize(new Dimension(550, 450));
		setMinimumSize(new Dimension(550, 450));
		setBackground(Color.LIGHT_GRAY);
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_2.setBounds(10, 11, 255, 281);
		add(scrollPane_2);
		
		clients = new JList();
		scrollPane_2.setViewportView(clients);
		clients.setBorder(null);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{clients}));
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(285, 11, 255, 281);
		add(scrollPane);
		
		connectionArea = new JTextArea();
		connectionArea.setBorder(null);
		scrollPane.setViewportView(connectionArea);
		connectionArea.setLineWrap(true);
		connectionArea.setFocusTraversalKeysEnabled(false);
		connectionArea.setFocusable(false);
		connectionArea.setEditable(false);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(10, 303, 530, 136);
		add(scrollPane_1);
		
		dataArea = new JTextArea();
		scrollPane_1.setViewportView(dataArea);
		dataArea.setLineWrap(true);
		dataArea.setFocusTraversalKeysEnabled(false);
		dataArea.setFocusable(false);
		dataArea.setEditable(false);
		caretData = (DefaultCaret)dataArea.getCaret();
		caretConnection = (DefaultCaret)connectionArea.getCaret();
		caretConnection.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		caretData.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
}

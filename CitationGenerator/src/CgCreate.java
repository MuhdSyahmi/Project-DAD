import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class CgCreate extends JFrame {

	private JPanel contentPane;
	private JTextField textField_url;
	private JTextField textField_at;
	private JTextField textField_author;
	private JLabel lblInsertYourWebsite;
	private JButton btnClose;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CgCreate frame = new CgCreate();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CgCreate() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 317);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 51, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWebsiteUrl = new JLabel("Website URL:");
		lblWebsiteUrl.setForeground(new Color(255, 255, 255));
		lblWebsiteUrl.setBounds(29, 75, 107, 14);
		contentPane.add(lblWebsiteUrl);
		
		JLabel lblWebsiteArticleTitle = new JLabel("Website Article Title:");
		lblWebsiteArticleTitle.setForeground(new Color(255, 255, 255));
		lblWebsiteArticleTitle.setBounds(29, 108, 126, 14);
		contentPane.add(lblWebsiteArticleTitle);
		
		JLabel lblWebsiteAuthor = new JLabel("Website Author:");
		lblWebsiteAuthor.setForeground(new Color(255, 255, 255));
		lblWebsiteAuthor.setBounds(29, 141, 107, 14);
		contentPane.add(lblWebsiteAuthor);
		
		textField_url = new JTextField();
		textField_url.setBounds(174, 72, 226, 20);
		contentPane.add(textField_url);
		textField_url.setColumns(10);
		
		textField_at = new JTextField();
		textField_at.setBounds(174, 105, 226, 20);
		contentPane.add(textField_at);
		textField_at.setColumns(10);
		
		textField_author = new JTextField();
		textField_author.setBounds(174, 138, 226, 20);
		contentPane.add(textField_author);
		textField_author.setColumns(10);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "addWebsite"));
						params.add(new BasicNameValuePair("websiteURL", textField_url.getText()));
						params.add(new BasicNameValuePair("websiteARTICLETITLE", textField_at.getText()));
						params.add(new BasicNameValuePair("websiteAUTHOR", textField_author.getText()));
				
						String strUrl = "http://localhost/apacg/apacgWebService.php";
						JSONObject jsonObj = makeHttpRequest(strUrl, "POST", params);
				
						try {
							JOptionPane.showMessageDialog(contentPane, jsonObj.getString("respond"));
						} catch (HeadlessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					public JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jObj = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jObj = new JSONObject(json);
							
						}	catch(JSONException e) {
							try {
								JSONArray jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jObj;
					}
				});
				thread.start();
			
			}
		});
		btnSave.setBounds(174, 177, 226, 23);
		contentPane.add(btnSave);
		
		lblInsertYourWebsite = new JLabel("Insert your website details:");
		lblInsertYourWebsite.setForeground(new Color(255, 255, 255));
		lblInsertYourWebsite.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblInsertYourWebsite.setBounds(29, 31, 198, 14);
		contentPane.add(lblInsertYourWebsite);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnClose.setBounds(303, 232, 97, 25);
		contentPane.add(btnClose);
	}

}

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
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;

public class CgGenerate extends JFrame {

	private JPanel contentPane;
	private JLabel lblUpdateYourWebsite;
	private JLabel lblSearchForYour;
	private JLabel lblWebsiteUrl_1;
	private JTextField textField_searchurl;
	private JButton btnGenerate;
	private JTextArea textArea;
	private JButton btnClose;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CgGenerate frame = new CgGenerate();
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
	public CgGenerate() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 329);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 51, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUpdateYourWebsite = new JLabel("Your Citation:");
		lblUpdateYourWebsite.setForeground(new Color(255, 255, 255));
		lblUpdateYourWebsite.setBounds(32, 121, 198, 14);
		contentPane.add(lblUpdateYourWebsite);
		
		lblSearchForYour = new JLabel("Search for your website:");
		lblSearchForYour.setForeground(new Color(255, 255, 255));
		lblSearchForYour.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSearchForYour.setBounds(31, 24, 173, 14);
		contentPane.add(lblSearchForYour);
		
		lblWebsiteUrl_1 = new JLabel("Website URL:");
		lblWebsiteUrl_1.setForeground(new Color(255, 255, 255));
		lblWebsiteUrl_1.setBounds(32, 58, 96, 14);
		contentPane.add(lblWebsiteUrl_1);
		
		textField_searchurl = new JTextField();
		textField_searchurl.setBounds(140, 55, 256, 20);
		contentPane.add(textField_searchurl);
		textField_searchurl.setColumns(10);
		
		btnGenerate = new JButton("GENERATE");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "searchURL"));
						params.add(new BasicNameValuePair("websiteURL", textField_searchurl.getText()));
				
						String strUrl = "http://localhost/apacg/apacgWebService.php";
						JSONObject jsonObj = makeHttpRequest(strUrl, "POST", params);
				
						try {
							String weburl = jsonObj.getString("websiteURL");
							String webat = jsonObj.getString("websiteARTICLETITLE");
							String webauthor = jsonObj.getString("websiteAUTHOR");
							
							String cg= webauthor+" (n.d). "+webat+". Retrieved from "+weburl;
							textArea.setText(cg);
					
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jobj = null;
						//making HTTP request
						
						try {
							//check for request method
							if(method == "POST") {
								//request method is POST
								//defaultHttpClient
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(url);
								httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));
								
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							
							} else if(method == "GET") {
								//request method is GET
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								url += "?" + paramString;
								HttpGet httpGet = new HttpGet(url);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader (new InputStreamReader(is, "iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							
									while((line = reader.readLine()) != null) {
										sb.append(line + "\n");
									}
									is.close();
									json = sb.toString();
									
									jobj = new JSONObject(json);
						}
						catch (JSONException e)
						{
							try {
								JSONArray jsnArr = new JSONArray(json);
								jobj = jsnArr.getJSONObject(0);
							}
							catch(JSONException e1)
							{
								e1.printStackTrace();
							}	
						} catch (Exception ee)
						{
							ee.printStackTrace();
						}
						return jobj;
					}
				});
				thread.start();
			
			
			
			}
		});
		btnGenerate.setBounds(256, 88, 140, 23);
		contentPane.add(btnGenerate);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBounds(32, 142, 364, 85);
		contentPane.add(textArea);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnClose.setBounds(299, 244, 97, 25);
		contentPane.add(btnClose);
	}

}

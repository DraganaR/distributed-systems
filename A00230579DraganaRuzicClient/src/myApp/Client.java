package myApp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class Client extends JFrame {
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		setTitle("Client");
		setSize(640,480);
		setPreferredSize(new Dimension(700, 400));
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//GET
		JPanel getPanel = new JPanel();
		JButton btnGet = new JButton("GET");
		JTextArea getTextArea= new JTextArea(10,50);
		getTextArea.setEditable(false);
		btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// define instance of closeable 
				CloseableHttpResponse response = null;
				CloseableHttpClient httpClient = null;
				try {
					// set up URI for the GET request
					URI uri = new URIBuilder().setScheme("http").setHost("localhost")
					.setPort(8080).setPath("/A00230579DraganaRuzicServer/rest/phones").build();
					
					// make an instance of the URI class
					HttpGet httpGet = new HttpGet(uri);
					
					// set header
					httpGet.setHeader("Accept", "application/xml");
					
					httpClient = HttpClients.createDefault();
					
					// execute the get
					response = httpClient.execute(httpGet);
					
					String tagName = "";
					String text = ""; 
					
					List<Phone> phones = null;
					Phone phone = null;
					
					try {
						// set up the XmlPullParser
						// set the text variable to whatever the xml gives back
						text = EntityUtils.toString(response.getEntity());
						XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
						factory.setNamespaceAware(true);
						XmlPullParser xpp = factory.newPullParser();
						
						// pushing text into pull parser
						xpp.setInput(new StringReader(text));
						
						// depending on the eventtyp - do something
						int eventType = xpp.getEventType();
						while(eventType != xpp.END_DOCUMENT) {
							if(eventType == xpp.START_DOCUMENT) {
								System.out.println("Start document");
							} else if(eventType == xpp.END_DOCUMENT) {
								System.out.println("End document");
							} else if (eventType == xpp.START_TAG) {
								System.out.println("Start tag "+xpp.getName());
								
								tagName = xpp.getName();
								// start tag = phones then make new phone list
								if(tagName.equalsIgnoreCase("phones")) {
									phones = new ArrayList<>();
								// start tag = phone then make a new phone
								} else if(tagName.equalsIgnoreCase("phone")) {
									phone = new Phone();
								} 
							} else if (eventType == xpp.END_TAG) {
								System.out.println("End tag "+xpp.getName());
								
								// end phone tag then add the current phone to the list of phones
								tagName = xpp.getName();
								if(tagName.equalsIgnoreCase("phone")) {
									phones.add(phone);
								} else if(tagName.equalsIgnoreCase("phones")) {
									System.out.println(phones.toString());
									// setting up a result
									String result = "";
									for(int i = 0; i < phones.size(); i++) {
										result += phones.get(i).toString() + "\n";
									}
									
									// printing the result out to the text area
									getTextArea.setText(result);
								}
							}
							
							else if(eventType == xpp.TEXT) {
								System.out.println("Text "+xpp.getText());
								text = xpp.getText();
								if(tagName.equalsIgnoreCase("battery")) {
									phone.setBattery(text);
								} else if(tagName.equalsIgnoreCase("make")) {
									phone.setMake(text);
								} else if(tagName.equalsIgnoreCase("model")) {
									phone.setModel(text);
								} else if(tagName.equalsIgnoreCase("id")) {
									phone.setId(Integer.parseInt(text));
								}
							}
							eventType = xpp.next();
						}
						System.out.println(text);
					} finally {
						response.close();
					}
					System.out.println("REPLY:"+text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		getPanel.add(btnGet);
		JScrollPane scroll = new JScrollPane(getTextArea);
		getPanel.add(scroll);
		getContentPane().add(getPanel);
		
		//POST
		JPanel postPanel = new JPanel();
		JButton btnPost = new JButton("POST");
		
		postPanel.add(btnPost);
		postPanel.setLayout(new FlowLayout());
		
		JLabel makePostLabel = new JLabel("Make");
		postPanel.add(makePostLabel);
		JTextField makePostTextField= new JTextField(10); 
		postPanel.add(makePostTextField);
		getContentPane().add(postPanel);
		
		JLabel modelPostLabel = new JLabel("Model");
		postPanel.add(modelPostLabel);
		JTextField modelPostTextField= new JTextField(10);
		postPanel.add(modelPostTextField);
		getContentPane().add(postPanel);
		
		JLabel batteryPostLabel = new JLabel("Battery");
		postPanel.add(batteryPostLabel);
		JTextField batteryPostTextField= new JTextField(10); 
		btnPost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				URI uri = null;
				CloseableHttpResponse response =null;
				try {
					uri = new URIBuilder()
						.setScheme("http")
						.setHost("localhost")
						.setPort(8080)
						.setPath("/A00230579DraganaRuzicServer/rest/phones").build();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				HttpPost httpPost = new HttpPost(uri);
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				CloseableHttpClient client = HttpClients.createDefault();
				
				// set up form params whatever you type in for each part of the phone
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", "0"));
				nameValuePairs.add(new BasicNameValuePair("make", makePostTextField.getText()));
				nameValuePairs.add(new BasicNameValuePair("model", modelPostTextField.getText()));
				nameValuePairs.add(new BasicNameValuePair("battery", batteryPostTextField.getText()));
				
				try {
					// set up the entity for the post
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					response = client.execute(httpPost);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(response.toString());
				
			}
		});
		postPanel.add(batteryPostTextField);
		getContentPane().add(postPanel);
		
		//PUT
		JPanel putPanel = new JPanel();
		JButton btnPut = new JButton("PUT");

		putPanel.add(btnPut);
		putPanel.setLayout(new FlowLayout());
		
		JLabel idPutLabel = new JLabel("ID");
		putPanel.add(idPutLabel);
		JTextField idPutTextField= new JTextField(10); 
		putPanel.add(idPutTextField);
		getContentPane().add(putPanel);
		
		JLabel makePutLabel = new JLabel("Make");
		putPanel.add(makePutLabel);
		JTextField makePutTextField= new JTextField(10); 
		putPanel.add(makePutTextField);
		getContentPane().add(putPanel);
		
		JLabel modelPutLabel = new JLabel("Model");
		putPanel.add(modelPutLabel);
		JTextField modelPutTextField = new JTextField(10);
		putPanel.add(modelPutTextField);
		getContentPane().add(putPanel);
		
		JLabel batteryPutLabel = new JLabel("Battery");
		putPanel.add(batteryPutLabel);
		JTextField batteryPutTextField= new JTextField(10); 
		putPanel.add(batteryPutTextField);
		
		btnPut.addActionListener(new ActionListener() {
			CloseableHttpResponse response = null;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int id = Integer.parseInt(idPutTextField.getText());
					
					URI uri = new URIBuilder()
						.setScheme("http")
						.setHost("localhost")
						.setPort(8080)
						.setPath("/A00230579DraganaRuzicServer/rest/phones/" + id).build();
				
				HttpPut httpPut = new HttpPut(uri);
				httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", "0"));
				nameValuePairs.add(new BasicNameValuePair("make", makePutTextField.getText()));
				nameValuePairs.add(new BasicNameValuePair("model", modelPutTextField.getText()));
				nameValuePairs.add(new BasicNameValuePair("battery", batteryPutTextField.getText()));
				
				CloseableHttpClient client = HttpClients.createDefault();
				httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// execute the put request
				response = client.execute(httpPut);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("Response: "+ response.toString());
			}
		});
		
		getContentPane().add(putPanel);
		
		//DELETE
		JPanel deletePanel = new JPanel();
		JButton btnDelete = new JButton("DELETE");
		deletePanel.add(btnDelete);
		JLabel idDeleteLabel = new JLabel("ID");
		deletePanel.add(idDeleteLabel);
		JTextField idDeleteTextField= new JTextField(10); 
		deletePanel.add(idDeleteTextField);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int id = Integer.parseInt(idDeleteTextField.getText());
				
				try {
				URI uri = new URIBuilder().setScheme("http").setHost("localhost")
				.setPort(8080).setPath("/A00230579DraganaRuzicServer/rest/phones/"+id).build();
			
				HttpDelete httpDelete = new HttpDelete(uri);
				httpDelete.setHeader("Accept", "application/x-www-form-urlencoded");
				
				CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpDelete);
				
				
				} catch(Exception e) {
					
				}
			}
		});
		
		deletePanel.setLayout(new FlowLayout());
		getContentPane().add(deletePanel);
		pack();
	}
}

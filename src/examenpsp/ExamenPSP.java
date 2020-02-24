package examenpsp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.SocketException;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class ExamenPSP extends JFrame{

	//Toda la parte visual
	//para el login, es usuario, usuario pass y user.
	//Pero vamos, que no va a funcionar fuera de mi local
	static JTextField cab= new JTextField();
	static JTextField cab2= new JTextField();
	static JTextField cab3= new JTextField();
	
        JTextField servidorjt = new JTextField();
        JTextField usuariojt = new JTextField();
        JTextField passwordjt = new JTextField();
        
        Label serv = new Label();
        Label userr = new Label();
        Label passw = new Label("clave:");
        
        
        
	private static JTextField campo=new JTextField();
	private static JTextField campo2=new JTextField();
	
	JButton botonCargar=new JButton("Subir fichero");
	JButton botonDescargar=new JButton("Descargar fichero");
	JButton botonBorrar=new JButton("Eliminar fichero");
	JButton botonCreaDir=new JButton("Crear carpeta");
	JButton botonDelDir=new JButton("Eliminar carpeta");
	JButton botonSalir=new JButton("Salir");
        JButton Conectar= new JButton("Conectar");
	
	JButton botonLogin=new JButton("Login");
	
	static JList listaDirec=new JList();
	
	private final Container c=getContentPane();
	
	static FTPClient cliente=new FTPClient();
	String servidor="";
	String user="usuario";
	String pass="usuario";
	boolean login;
	
	static String direcInicial="/";
	static String direcSelec=direcInicial;
	static String ficheroSelec="";
	
	
	public ExamenPSP() throws SocketException, IOException {
		super("CLIENTE BASICO FTP");
		
		JFrame panelLogin=new JFrame();
		JLabel permiso=new JLabel("Introduce usuario,contraseña y nombre del servidor");
                JLabel permiso2=new JLabel("(Por defecto recibe localhost)");
		JTextField campoUser=new JTextField();
		JPasswordField campoPass=new JPasswordField();
                JTextField campoServer=new JTextField();
		
		panelLogin.setSize(400, 400);
		panelLogin.setVisible(true);
		
		
		panelLogin.add(permiso);
                panelLogin.add(permiso2);
		panelLogin.add(campoUser);
		panelLogin.add(campoPass);
                panelLogin.add(campoServer);
		panelLogin.add(botonLogin);
		
		Dimension size=permiso.getPreferredSize();
		permiso.setBounds(60, 40,size.width, size.height);
		permiso.setVisible(true);
		
                permiso2.setBounds(100, 60,size.width, size.height);
		permiso2.setVisible(true);
                
		campoUser.setBounds(100,90,180,20);
		campoUser.setVisible(true);
		
		campoPass.setBounds(100,130,180,20);
		campoPass.setVisible(true);
                campoServer.setBounds(100,170,180,20);
		campoServer.setVisible(true);
		
		botonLogin.setBounds(120, 300, 130, 30);
		

		//Metodo que lista las direcciones, rutas
			listaDirec.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					// TODO Auto-generated method stub
					if(lse.getValueIsAdjusting()) {
						ficheroSelec="";
						
						String fic=listaDirec.getSelectedValue().toString();
						
						if(listaDirec.getSelectedIndex()==0) {
							if(!fic.equals(direcInicial)) {
                                                            try {
                                                                try {
                                                                    cliente.changeToParentDirectory(); // TODO: handle exception
                                                                } catch (IOException ex) {
                                                                    Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                                                }
                                                                direcSelec=cliente.printWorkingDirectory();
                                                                FTPFile[] ff2=null;
                                                                cliente.changeWorkingDirectory(direcSelec);
                                                                ff2=cliente.listFiles();
                                                                campo.setText("");
                                                                llenarLista(ff2,direcSelec);
                                                            } catch (IOException ex) {
                                                                Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
							}
							}else {
								if(fic.substring(0,6).equals("(DIR) ")) {
                                                                    try {
                                                                        fic=fic.substring(6); // TODO Auto-generated catch block
                                                                        String direcSelec2="";
                                                                        if(direcSelec.equals("/"))
                                                                            direcSelec2=direcSelec+fic;
                                                                        else
                                                                            direcSelec2=direcSelec+"/"+fic;
                                                                        cliente.changeWorkingDirectory(direcSelec2);
                                                                        FTPFile[] ff2=cliente.listFiles();
                                                                        campo.setText("DIRECTORIO: "+fic+", "+ff2.length+" elementos");
                                                                        direcSelec=direcSelec2;
                                                                        llenarLista(ff2,direcSelec);
                                                                    } catch (IOException ex) {
                                                                        Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                                                    }
								}else {
									ficheroSelec=direcSelec;
									if(direcSelec.equals("/"))
										ficheroSelec+=fic;
									else
										ficheroSelec+="/"+fic;
								}
							}
							campo2.setText("DIRECTORIO ACTUAL: "+direcSelec);
						}
					}
			});
			
			//Metodo para salir de la aplicacion
			botonSalir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
                                    try {
                                        // TODO Auto-generated method stub
                                        cliente.disconnect(); // TODO: handle exception
                                    } catch (IOException ex) {
                                        Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
					System.exit(0);
					
				}
			});
			
			//Boton para crear una carpeta
			botonCreaDir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String nombreCarpeta=JOptionPane.showInputDialog(null,"Introduce nombre de directorio","carpeta");
					if(!(nombreCarpeta==null)) {
                                            try {
                                                String directorio=direcSelec;
                                                if(!direcSelec.contentEquals("/"))
                                                    directorio=directorio+"/";
                                                
                                                directorio+=nombreCarpeta.trim();
                                                
                                                if (cliente.makeDirectory(directorio)) {
                                                    String m=nombreCarpeta.trim()+" => Se ha creado correctamtente";
                                                    JOptionPane.showMessageDialog(null, m);
                                                    campo.setText(m);
                                                    //cambiamos a ese directorio
                                                    cliente.changeWorkingDirectory(direcSelec);
                                                    //listamos los ficheros
                                                    FTPFile[] ff2= cliente.listFiles();
                                                    llenarLista(ff2,direcSelec);
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "No ha podido crearse el directorio");
                                                } // TODO: handle exception
                                            } catch (IOException ex) {
                                                Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                            }
					}
					
				}
			});
			
			//Boton para entrar a la aplicacion
			botonLogin.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
                                        servidor=campoServer.getText();
					user=campoUser.getText();
					pass=campoPass.getText();
					System.out.println("PASS==>"+pass);
					try {
						Login(servidor,user,pass, panelLogin);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			//Boton para eliminar una carpeta
			botonDelDir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String nombreCarpeta=JOptionPane.showInputDialog(null,"Introduce nombre de directorio a eliminar","carpeta");
					if(!(nombreCarpeta==null)) {
                                            try {
                                                String directorio=direcSelec;
                                                if(!direcSelec.contentEquals("/"))
                                                    directorio=directorio+"/";
                                                
                                                directorio+=nombreCarpeta.trim();
                                                
                                                if (cliente.removeDirectory(directorio)) {
                                                    String m=nombreCarpeta.trim()+" => Se ha eliminado correctamtente";
                                                    JOptionPane.showMessageDialog(null, m);
                                                    campo.setText(m);
                                                    cliente.changeWorkingDirectory(direcSelec);
                                                    
                                                    FTPFile[] ff2= cliente.listFiles();
                                                    llenarLista(ff2,direcSelec);
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "No ha podido eliminarse el directorio");
                                                } // TODO: handle exception
                                            } catch (IOException ex) {
                                                Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                            }
					}
				}
			});
			
			//boton para subir contenido a la carpeta actual
			botonCargar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser f=new JFileChooser();
					
					f.setFileSelectionMode(JFileChooser.FILES_ONLY);
					f.setDialogTitle("Selecciona el Fichero a SUBIR AL SERVIDOR FTP");
					
					int returnVal=f.showDialog(f, "Cargar");
					if(returnVal==JFileChooser.APPROVE_OPTION) {
						File file=f.getSelectedFile();
						String archivo=file.getAbsolutePath();
						String nombreArchivo=file.getName();
						try {
							SubirFichero(archivo,nombreArchivo);
						} catch (IOException e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
					}
				}
			});
			//Boton para descargar un archivo
			botonDescargar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					String directorio=direcSelec;
					if(!direcSelec.equals("/"))
						directorio=directorio+"/";
					if(!ficheroSelec.contentEquals("")) {
						DescargarFichero(directorio+ficheroSelec,ficheroSelec);
					}
					
				}
			});
			Conectar.addActionListener(new ActionListener() {
                        				@Override
				public void actionPerformed(ActionEvent e) {
					
                                        servidor=servidorjt.getText();
					user=usuariojt.getText();
					pass=passwordjt.getText();
					System.out.println("PASS==>"+pass);
					try {
						Login(servidor,user,pass, panelLogin);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
                        });
                        
			//boton para borrar el archivo seleccionado
			botonBorrar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
                                    try {
                                        String directorio=direcSelec;
                                        if(!direcSelec.equals("/"))
                                            directorio=directorio+"/";
                                        if(!ficheroSelec.equals(""))
                                            BorrarFichero(directorio + ficheroSelec,ficheroSelec);
                                    } catch (IOException ex) {
                                        Logger.getLogger(ExamenPSP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
				}
			});
		
	}
	
	//Proceso de logeo
	private void Login(String server,String user, String pass, JFrame panelLogin) throws IOException {
			cliente.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		cliente.connect(server);
		cliente.enterLocalPassiveMode();	
            login=cliente.login(user, pass);
		if(login) {
			panelLogin.setVisible(false);
			cliente.changeWorkingDirectory(direcInicial);
			
			FTPFile[] files=cliente.listFiles();
			
			llenarLista(files,direcInicial);
			
			campo.setText("<<ARBOL DE DIRECTORIOS CONSTRUIDO>>");
			cab.setText("Servidor FTP: "+servidor);
			cab2.setText("Usuario: "+user);
			cab3.setText("DIRECTORIO RAIZ: "+direcInicial);
			
			listaDirec.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane barraDesplazamiento=new JScrollPane(listaDirec);
			barraDesplazamiento.setPreferredSize(new Dimension(335,420));
			barraDesplazamiento.setBounds(new Rectangle(5,65,335,420));
			c.add(barraDesplazamiento);
			c.setLayout(null);
			
			
			botonCargar.setBounds(370, 65, 150, 30);
			add(botonCargar);
			
			botonDescargar.setBounds(370, 115, 150, 30);
			add(botonDescargar);
			
			botonBorrar.setBounds(370, 165, 150, 30);
			add(botonBorrar);
			
			botonCreaDir.setBounds(370, 215, 150, 30);
			add(botonCreaDir);
			
			botonDelDir.setBounds(370, 265, 150, 30);
			add(botonDelDir);
			
			botonSalir.setBounds(370, 315, 150, 30);
			add(botonSalir);
                        
                        
                        
                        
                       
                        
                        
                        
                        
                        
                        
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(580,600);
			setVisible(true);
		}
	}
	
	//Mostrar los elementos del directorio actual
	private static void llenarLista(FTPFile[] files,String direc2) throws IOException {
		if(files==null) return;
		
		DefaultListModel modeloLista=new DefaultListModel();
		
		listaDirec.setForeground(Color.blue);
		Font fuente=new Font("Courier", Font.PLAIN, 12);
		listaDirec.setFont(fuente);
		
		listaDirec.removeAll();
		
                cliente.changeWorkingDirectory(direc2); // TODO: handle exception
		
		direcSelec=direc2;
		
		modeloLista.addElement(direc2);
		
		for (int i = 0; i < files.length; i++) {
			if(!(files[i].getName()).equals(".") && !(files[i].getName()).equals("..")) {
				String f=files[i].getName();
				
				if(files[i].isDirectory()) f="(DIR) "+f;
				
				modeloLista.addElement(f);
			} 
		}
		
		try {
			listaDirec.setModel(modeloLista);
		} catch (NullPointerException n) {
			;
			// TODO: handle exception
		}
	}
	
	//Borrar el fichero en si
	private void BorrarFichero(String nombreCompleto, String nombreFichero) throws IOException {
		int seleccion=JOptionPane.showConfirmDialog(null, "Desea eliminar el fichero seleccionado?");
		if(seleccion==JOptionPane.OK_OPTION) {
                    //Metodo que lo hace
                    if(cliente.deleteFile(nombreCompleto)) {
                        String m=nombreFichero+" => Eliminado correctamente";
                        JOptionPane.showMessageDialog(null, m);
                        campo.setText(m);
                        cliente.changeWorkingDirectory(direcSelec);
                        FTPFile[] ff2=cliente.listFiles();
                        llenarLista(ff2, direcSelec);
                    }else
                        JOptionPane.showMessageDialog(null, nombreFichero+" => No se ha podido eliminar");
		}
	}
	
	//Metodo para subir un fichero
	private boolean SubirFichero(String archivo, String soloNombre) throws IOException {
		cliente.setFileType(FTP.BINARY_FILE_TYPE);
		BufferedInputStream in=new BufferedInputStream(new FileInputStream(archivo));
		boolean ok=false;
		
		cliente.changeWorkingDirectory(direcSelec);
		//Metodo de subirlo en si
		if(cliente.storeFile(soloNombre, in)) {
			String s=" "+soloNombre+" => Subido correctamente";
			campo.setText(s);
			campo2.setText("Se va a actualizar el arbol de directorios");
			JOptionPane.showMessageDialog(null, s);
			
			FTPFile[] ff2=cliente.listFiles();
			llenarLista(ff2,direcSelec);
			ok=true;
		}else
			campo.setText("No se ha podido subir el archivo: "+soloNombre);
		return ok;
	}
	
	//Para descargar
	private void DescargarFichero(String nombreCompleto, String nombreFichero) {
		String archivoyCarpetaDestino="";
		String carpetaDestino="";
		JFileChooser f=new JFileChooser();
		
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.setDialogTitle("Selecciona el Directorio donde DESCARGAR el fichero");
		
		int returnVal=f.showDialog(null, "Descargar");
		if(returnVal==JFileChooser.APPROVE_OPTION) {
			File file=f.getSelectedFile();
			carpetaDestino=(file.getAbsolutePath()).toString();
			archivoyCarpetaDestino=carpetaDestino+File.separator+nombreFichero;
			try {
				cliente.setFileType(FTP.BINARY_FILE_TYPE);
				BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(archivoyCarpetaDestino));
				//El momento de descargarlo
				if(cliente.retrieveFile(nombreCompleto, out))
					JOptionPane.showMessageDialog(null, nombreFichero+" se ha descargado con exito");
				else
					JOptionPane.showMessageDialog(null, "No se ha podido descargar: "+nombreFichero);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	
	public static void main(String[] args) throws SocketException, IOException {
		// TODO Auto-generated method stub
		new ExamenPSP();
	}

}

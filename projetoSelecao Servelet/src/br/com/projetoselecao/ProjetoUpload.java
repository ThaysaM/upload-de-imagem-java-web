package br.com.projetoselecao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.sun.xml.internal.bind.marshaller.NoEscapeHandler;


@WebServlet("/Projeto")
public class ProjetoUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	public ProjetoUpload() {
        super();
    }

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome_arquivo1 = null;
		String nome_arquivo2="";
		String extensaoDoArquivo="";
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//Verifica se foi ou n�o adicionado um arquivo no campo selecione
		boolean instancia = ServletFileUpload.isMultipartContent(request);
		if(!instancia) {
			return;
		}
		
		//Solicita��o de upload do arquivo
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		try {
			//Recebendo upload do arquivo
			List<FileItem> lista = upload.parseRequest(request);			
			Iterator <FileItem> item = lista.iterator();
			if(!item.hasNext()){
				out.println("Arquivo n�o carregado!");
				return;
			}
			while (item.hasNext()) {
				
				FileItem fileItem = item.next();				
				boolean isFormField = fileItem.isFormField();
				//Verifica se a inst�ncia 
				if(isFormField) {
					// Verifica o campo do nome do arquivo
					if(nome_arquivo1 == null) {
						if(fileItem.getFieldName().equals("file_name")){
							//Armazeno no nome do campo em nome_arquivo1
							nome_arquivo1 = fileItem.getString();
							
						}
						
					}
				}else {
					if(fileItem.getSize() > 0) {
						// Renomeando o nome do arquivo de imagem
						nome_arquivo2=fileItem.getName();//nome_da_imagem.jpg do arquivo anexado
						extensaoDoArquivo = FilenameUtils.getExtension(nome_arquivo2);
						
						Path source = Paths.get(nome_arquivo2);
						source = source.resolveSibling(nome_arquivo1+"."+extensaoDoArquivo);
						
						String novo_endereco= source.toString();					
						nome_arquivo2 = novo_endereco;
						
						// Escreve o arquivo na pasta arquivo_upload
						fileItem.write(new File("C:\\bin\\apache-tomcat-9.0.37\\webapps\\arquivo_upload\\" + nome_arquivo2));						
					}
				}
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			out.println("<script type='text/javascript'>");
			out.println("window.location.href='index.jsp?filename="+nome_arquivo2+"'");
			out.println("</script>");
			out.close();
		}
	}

}
package br.com.sigpr.entity.tarefa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Arquivo
 *
 */
@Entity
@Table( name = "TB_ARQUIVO",
		schema = "SIGPR")
		
@SequenceGenerator( name = "GENERATOR_SEQ_ARQUIVO", 
					sequenceName = "SEQ_ARQUIVO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ARQUIVO_ID",
			nullable = false,
			updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_ARQUIVO")
	private Long id;
	
	@Column(name = "ARQUIVO_FILE_NAME")
	private String fileName;
	
	@Column(name = "ARQUIVO_FILE_TYPE")
	private String fileType;
	
	@Column(name = "ARQUIVO_FILE_SIZE")
	private Long fileSize;
	
	@Column(name = "ARQUIVO_FILE_MIMETYPE")
	private String mimeType;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "ARQUIVO_FILE")
	private byte[] arquivo;
	
	@ManyToOne
	@JoinColumn(name = "ARQUIVO_ARTEFATO_ID", 
			referencedColumnName = "ARTEFATO_ID")
	private Artefato artefato;

	public Arquivo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Artefato getArtefato() {
		return artefato;
	}

	public void setArtefato(Artefato artefato) {
		this.artefato = artefato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
   
}
